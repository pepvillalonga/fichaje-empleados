import java.util.Scanner;

import gestion.GestorEmpleados;
import gestion.GestorFichaje;
import java.util.List;
import modelo.Empleado;
import modelo.Fichaje;

// Programa de consola: menús y lectura de datos
public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final GestorEmpleados gestor = new GestorEmpleados();
    private static final GestorFichaje gestorFichajes = new GestorFichaje();

    // Comprueba la BD y arranca el menú principal
    public static void main(String[] args) {
        if (!gestor.hayConexion()) {
            System.out.println("No se puede conectar con la base de datos. Revisa que esté arrancada (ver README).");
            return;
        }
        try {
            menuInicio();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Hasta luego");
    }

    // Menú principal: login, registro o salir
    private static void menuInicio() {
        int opcion;
        do {
            System.out.println("\nCONTROL HORARIO");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Registrarse");
            System.out.println("0. Salir");
            opcion = leerEntero("Elige una opción: ");

            switch (opcion) {
                case 1 -> login();
                case 2 -> registrar();
                case 0 -> {
                }
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    // Pide los datos y registra un empleado nuevo
    private static void registrar() {
        System.out.println("\nRegistro");
        String id = leerTexto("ID de empleado: ");
        String nombre = leerTexto("Nombre: ");
        String password = leerTexto("Contraseña: ");

        String error = gestor.registrar(id, nombre, password);
        if (error == null) {
            System.out.println("Empleado registrado correctamente.");
        } else {
            System.out.println(error);
        }
    }

    // Pide ID y contraseña y abre el menú del empleado
    private static void login() {
        System.out.println("\nIniciar sesión");
        String id = leerTexto("ID de empleado: ");
        String password = leerTexto("Contraseña: ");

        Empleado empleado = gestor.login(id, password);
        if (empleado != null) {
            System.out.println("Bienvenido/a, " + empleado.getNombre() + ".");
            menuEmpleado(empleado);
        } else {
            System.out.println("ID o contraseña incorrectos.");
        }
    }

    // Menú del empleado con sesión iniciada
    private static void menuEmpleado(Empleado empleado) {
        int opcion;
        do {
            System.out.println("\nMENÚ DE " + empleado.getNombre().toUpperCase());
            System.out.println("1. Fichar entrada");
            System.out.println("2. Fichar salida");
            System.out.println("3. Ver mis fichajes");
            System.out.println("4. Ver horas totales");
            System.out.println("5. Eliminar mi cuenta");
            System.out.println("0. Cerrar sesión");
            opcion = leerEntero("Elige una opción: ");

            switch (opcion) {
                case 1 -> ficharEntrada(empleado);
                case 2 -> ficharSalida(empleado);
                case 3 -> verFichajes(empleado);
                case 4 -> verHorasTotales(empleado);
                case 5 -> {
                    if (eliminarCuenta(empleado)) {
                        opcion = 0;
                    }
                }
                case 0 -> System.out.println("Sesión cerrada.");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    // Registra la entrada del empleado
    private static void ficharEntrada(Empleado empleado) {
        if (gestorFichajes.ficharEntrada(empleado.getId())) {
            System.out.println("Entrada registrada.");
        } else {
            System.out.println("Ya tienes un fichaje abierto. Ficha la salida primero.");
        }
    }

    // Registra la salida del empleado
    private static void ficharSalida(Empleado empleado) {
        if (gestorFichajes.ficharSalida(empleado.getId())) {
            System.out.println("Salida registrada.");
        } else {
            System.out.println("No tienes ningún fichaje abierto. Ficha la entrada primero.");
        }
    }

    // Muestra todos los fichajes del empleado
    private static void verFichajes(Empleado empleado) {
        List<Fichaje> fichajes = gestorFichajes.fichajesDe(empleado.getId());
        if (fichajes.isEmpty()) {
            System.out.println("No tienes fichajes.");
            return;
        }
        System.out.println("\nMis fichajes");
        for (Fichaje fichaje : fichajes) {
            System.out.println(fichaje);
        }
    }

    // Muestra las horas trabajadas en formato "X h Y min"
    private static void verHorasTotales(Empleado empleado) {
        double horas = gestorFichajes.calcularTotalHorasEmpleado(empleado.getId());
        System.out.println("Horas totales trabajadas: " + Fichaje.formatearHoras(horas));
        if (gestorFichajes.buscarFichajeAbierto(empleado.getId()) != null) {
            System.out.println("(El fichaje en curso no se cuenta hasta que fiches la salida.)");
        }
    }

    // Pide confirmación y borra la cuenta. true si se ha borrado
    private static boolean eliminarCuenta(Empleado empleado) {
        String respuesta = leerTexto("¿Seguro que quieres eliminar tu cuenta? (s/n): ");
        if (!respuesta.equalsIgnoreCase("s")) {
            System.out.println("Operación cancelada.");
            return false;
        }

        if (gestor.eliminarEmpleado(empleado.getId())) {
            System.out.println("Cuenta eliminada correctamente.");
            return true;
        }
        System.out.println("No se ha podido eliminar la cuenta.");
        return false;
    }

    // Lee un texto no vacío
    private static String leerTexto(String mensaje) {
        String texto;
        do {
            System.out.print(mensaje);
            texto = sc.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("Este campo no puede estar vacío.");
            }
        } while (texto.isEmpty());
        return texto;
    }

    // Lee un número entero, repite si no es válido
    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número válido.");
            }
        }
    }
}
