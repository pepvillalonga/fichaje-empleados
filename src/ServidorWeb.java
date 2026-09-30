import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import gestion.GestorEmpleados;
import gestion.GestorFichaje;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.Empleado;
import modelo.Fichaje;

// Servidor web: formulario de nuevo fichaje y tabla de fichajes (http://localhost:8080)
public class ServidorWeb {

    private static final int PUERTO = 8080;
    private static final String PLANTILLA = "src/webApp/index.html";

    private static final GestorEmpleados gestorEmpleados = new GestorEmpleados();
    private static final GestorFichaje gestorFichajes = new GestorFichaje();

    // Comprueba la BD y arranca el servidor
    public static void main(String[] args) throws IOException {
        if (!gestorEmpleados.hayConexion()) {
            System.out.println("No se puede conectar con la base de datos. Revisa que esté arrancada (ver README).");
            return;
        }
        HttpServer servidor = HttpServer.create(new InetSocketAddress(PUERTO), 0);
        servidor.createContext("/", ServidorWeb::paginaInicio);
        servidor.createContext("/guardar-fichaje", ServidorWeb::guardarFichaje);
        servidor.createContext("/registrar-empleado", ServidorWeb::registrarEmpleado);
        servidor.start();
        System.out.println("Servidor arrancado en http://localhost:" + PUERTO + " (Ctrl+C para parar)");
    }

    // GET / : formularios vacíos y tabla (filtrada si viene ?id=)
    private static void paginaInicio(HttpExchange ex) throws IOException {
        try {
            Map<String, String> params = leerParametros(ex.getRequestURI().getRawQuery());
            Map<String, String> valores = valoresIniciales(params.getOrDefault("id", "").trim());
            if (params.containsKey("ok")) {
                valores.put("mensaje", mensajeOk("Fichaje guardado."));
            }
            if (params.containsKey("creado")) {
                String nuevo = params.get("creado");
                valores.put("mensajeEmpleado", mensajeOk("Empleado " + nuevo + " creado. Ya puede fichar."));
                valores.put("idEmpleado", nuevo);
            }
            enviarPagina(ex, valores);
        } catch (RuntimeException e) {
            enviar(ex, 500, "Error: " + escapar(e.getMessage()));
        }
    }

    // POST /guardar-fichaje : valida y guarda. Si va bien redirige (evita duplicar al recargar)
    private static void guardarFichaje(HttpExchange ex) throws IOException {
        if (!ex.getRequestMethod().equals("POST")) {
            redirigir(ex, "/");
            return;
        }
        try {
            Map<String, String> params = leerCuerpo(ex);
            String id = params.getOrDefault("idEmpleado", "").trim();
            String password = params.getOrDefault("password", "");
            String fecha = params.getOrDefault("fecha", "");
            String entrada = params.getOrDefault("horaEntrada", "");
            String salida = params.getOrDefault("horaSalida", "");

            String error;
            Empleado empleado = gestorEmpleados.login(id, password);
            if (empleado == null) {
                error = "ID o contraseña incorrectos.";
            } else {
                try {
                    error = gestorFichajes.registrarManual(empleado.getId(),
                            LocalDate.parse(fecha), LocalTime.parse(entrada), LocalTime.parse(salida));
                } catch (DateTimeParseException e) {
                    error = "La fecha o las horas no son válidas.";
                }
            }

            if (error == null) {
                redirigir(ex, "/?ok=1&id=" + URLEncoder.encode(empleado.getId(), StandardCharsets.UTF_8));
            } else {
                // Se vuelve a mostrar el formulario con lo que había escrito (menos la contraseña)
                Map<String, String> valores = valoresIniciales(id);
                valores.put("mensaje", mensajeError(error));
                valores.put("fecha", fecha);
                valores.put("horaEntrada", entrada);
                valores.put("horaSalida", salida);
                enviarPagina(ex, valores);
            }
        } catch (RuntimeException e) {
            enviar(ex, 500, "Error: " + escapar(e.getMessage()));
        }
    }

    // POST /registrar-empleado : crea un empleado con su contraseña
    private static void registrarEmpleado(HttpExchange ex) throws IOException {
        if (!ex.getRequestMethod().equals("POST")) {
            redirigir(ex, "/");
            return;
        }
        try {
            Map<String, String> params = leerCuerpo(ex);
            String id = params.getOrDefault("nuevoId", "").trim();
            String nombre = params.getOrDefault("nuevoNombre", "").trim();
            String password = params.getOrDefault("nuevoPassword", "");
            String repetir = params.getOrDefault("repetirPassword", "");

            String error;
            if (!password.equals(repetir)) {
                error = "Las contraseñas no coinciden.";
            } else {
                error = gestorEmpleados.registrar(id, nombre, password);
            }

            if (error == null) {
                redirigir(ex, "/?creado=" + URLEncoder.encode(id, StandardCharsets.UTF_8));
            } else {
                // Se vuelve a mostrar el formulario con lo que había escrito (menos las contraseñas)
                Map<String, String> valores = valoresIniciales("");
                valores.put("mensajeEmpleado", mensajeError(error));
                valores.put("nuevoId", id);
                valores.put("nuevoNombre", nombre);
                enviarPagina(ex, valores);
            }
        } catch (RuntimeException e) {
            enviar(ex, 500, "Error: " + escapar(e.getMessage()));
        }
    }

    // Valores por defecto de la página: fecha de hoy y hora actual en el formulario de fichaje
    private static Map<String, String> valoresIniciales(String filtro) {
        String ahora = LocalTime.now().withSecond(0).withNano(0).toString();
        Map<String, String> valores = new HashMap<>();
        valores.put("mensaje", "");
        valores.put("idEmpleado", filtro);
        valores.put("fecha", LocalDate.now().toString());
        valores.put("horaEntrada", ahora);
        valores.put("horaSalida", ahora);
        valores.put("filtro", filtro);
        valores.put("mensajeEmpleado", "");
        valores.put("nuevoId", "");
        valores.put("nuevoNombre", "");
        return valores;
    }

    // Rellena cada {{clave}} de la plantilla con su valor. Los mensajes ya vienen en HTML
    private static void enviarPagina(HttpExchange ex, Map<String, String> valores) throws IOException {
        String html = Files.readString(Path.of(PLANTILLA), StandardCharsets.UTF_8);
        for (Map.Entry<String, String> valor : valores.entrySet()) {
            String texto = valor.getValue();
            if (!valor.getKey().startsWith("mensaje")) {
                texto = escapar(texto);
            }
            html = html.replace("{{" + valor.getKey() + "}}", texto);
        }
        html = html.replace("{{hoy}}", LocalDate.now().toString())
                .replace("{{tabla}}", generarTabla(valores.get("filtro")));
        enviar(ex, 200, html);
    }

    private static String mensajeOk(String texto) {
        return "<p role=\"status\">✅ " + escapar(texto) + "</p>";
    }

    private static String mensajeError(String texto) {
        return "<p role=\"alert\">❌ " + escapar(texto) + "</p>";
    }

    // Tabla de fichajes, del más reciente al más antiguo. Con filtro añade el total de horas
    private static String generarTabla(String filtro) {
        List<Fichaje> fichajes;
        if (filtro.isEmpty()) {
            fichajes = gestorFichajes.todosLosFichajes();
        } else {
            fichajes = gestorFichajes.fichajesDe(filtro);
        }
        if (fichajes.isEmpty()) {
            return "<p>No hay fichajes.</p>";
        }
        Collections.reverse(fichajes);

        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
        StringBuilder tabla = new StringBuilder();
        tabla.append("<table border=\"1\">\n<tr><th>ID empleado</th><th>Fecha</th><th>Entrada</th>")
                .append("<th>Salida</th><th>Horas</th></tr>\n");
        for (Fichaje f : fichajes) {
            tabla.append("<tr><td>").append(escapar(f.getIdEmpleado()))
                    .append("</td><td>").append(f.getFecha().format(formatoFecha))
                    .append("</td><td>").append(f.getHoraEntrada().format(formatoHora));
            if (f.estaAbierto()) {
                tabla.append("</td><td>en curso</td><td>-");
            } else {
                tabla.append("</td><td>").append(f.getHoraSalida().format(formatoHora))
                        .append("</td><td>").append(Fichaje.formatearHoras(f.calculoHoras()));
            }
            tabla.append("</td></tr>\n");
        }
        if (!filtro.isEmpty()) {
            double total = gestorFichajes.calcularTotalHorasEmpleado(filtro);
            tabla.append("<tr><th colspan=\"4\">Total</th><th>")
                    .append(Fichaje.formatearHoras(total)).append("</th></tr>\n");
        }
        tabla.append("</table>");
        return tabla.toString();
    }

    // Lee los datos enviados por un formulario POST
    private static Map<String, String> leerCuerpo(HttpExchange ex) throws IOException {
        String cuerpo = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        return leerParametros(cuerpo);
    }

    // Convierte "a=1&b=2" en un mapa {a=1, b=2}
    private static Map<String, String> leerParametros(String texto) {
        Map<String, String> params = new HashMap<>();
        if (texto == null || texto.isEmpty()) {
            return params;
        }
        for (String par : texto.split("&")) {
            String[] partes = par.split("=", 2);
            String clave = URLDecoder.decode(partes[0], StandardCharsets.UTF_8);
            String valor = partes.length > 1 ? URLDecoder.decode(partes[1], StandardCharsets.UTF_8) : "";
            params.put(clave, valor);
        }
        return params;
    }

    // Evita que un texto del usuario se interprete como HTML
    private static String escapar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    // Redirección 303 a otra página
    private static void redirigir(HttpExchange ex, String url) throws IOException {
        ex.getResponseHeaders().set("Location", url);
        ex.sendResponseHeaders(303, -1);
        ex.close();
    }

    // Envía una respuesta HTML
    private static void enviar(HttpExchange ex, int codigo, String html) throws IOException {
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        ex.sendResponseHeaders(codigo, bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }
}
