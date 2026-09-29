import java.util.Scanner;

import gestion.GestorEmpleados;
import modelo.Empleado;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final GestorEmpleados gestor = new GestorEmpleados();

    public static void main(String[] args) {
        menuInicio();
        System.out.println("Hasta luego");
    }

    // Menu Inicio

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

    // Menu Registro

    private static void registrar() {
        System.out.println("\nRegistro");
        String id = leerTexto("ID de empleado: ");
        String nombre = leerTexto("Nombre: ");
        String password = leerTexto("Contraseña: ");

        if (gestor.registrar(id, nombre, password)) {
            System.out.println("Empleado registrado correctamente.");
        } else {
            System.out.println("Ya existe un empleado con ese ID.");
        }
    }

    // Menu Inicio sesión

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

    // Menu Empleados

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

    private static void ficharEntrada(Empleado empleado) {

    }

    private static void ficharSalida(Empleado empleado) {

    }

    private static void verFichajes(Empleado empleado) {

    }

    private static void verHorasTotales(Empleado empleado) {

    }

    private static boolean eliminarCuenta(Empleado empleado) {
        return true;
    }

    // Lectura de datos

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
