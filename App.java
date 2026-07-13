// Removed package declaration to match expected default package

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App {

    // STRICT + "uuuu" (año sin era): rechaza fechas imposibles como 31/02/2026
    // en vez de "corregirlas" solas. Con "yyyy" (año de era) el modo STRICT falla siempre.
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GestorHospital hospital = new GestorHospital();
        int opcion = 0;

        do {
            mostrarMenuPrincipal();
            try {
                opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir salto de línea
            } catch (InputMismatchException e) {
                System.out.println("Error: Debe ingresar un número válido.");
                scanner.nextLine();
                continue;
            }

            switch (opcion) {
                case 1:
                    menuBuscarOIngresar(scanner, hospital);
                    break;
                case 2:
                    System.out.println("\n##### Listado General de Pacientes #####");
                    hospital.listarPacientes();
                    break;
                case 3:
                    System.out.println("\n##### Exportando datos... #####");
                    hospital.exportarA_CSV();
                    break;
                case 4:
                    menuBuscarPorNombreApellido(scanner, hospital);
                    break;
                case 5:
                    System.out.println("Saliendo del sistema hospitalario...");
                    break;
                default:
                    System.out.println("Opción inválida.");
                    break;
            }
        } while (opcion != 5);
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\n####### SISTEMA DE GESTIÓN HOSPITALARIA #######");
        System.out.println("1. Buscar / Ingresar Paciente (por DNI)");
        System.out.println("2. Listar todos los pacientes");
        System.out.println("3. Extraer datos a CSV");
        System.out.println("4. Buscar por Nombre/Apellido y guardar resultados");
        System.out.println("5. Salir");
        System.out.print("Seleccione una opción: ");
    }

    // Lee una fecha desde teclado validando el formato dd/MM/yyyy.
    // Punto 5: si el usuario escribe mal la fecha, se captura DateTimeParseException
    // y se le vuelve a pedir el dato, sin romper el programa.
    private static LocalDate leerFecha(Scanner scanner, String etiqueta) {
        LocalDate fecha = null;
        while (fecha == null) {
            System.out.print(etiqueta + " (DD/MM/AAAA): ");
            String texto = scanner.nextLine();
            try {
                fecha = LocalDate.parse(texto, FORMATO_FECHA);
            } catch (DateTimeParseException e) {
                System.out.println("Error: la fecha ingresada no es válida. Use el formato DD/MM/AAAA.");
            }
        }
        return fecha;
    }

    private static void menuBuscarOIngresar(Scanner scanner, GestorHospital hospital) {
        System.out.print("\nIngrese el DNI del paciente a buscar: ");
        String dni = scanner.nextLine();

        Paciente paciente = hospital.buscarPacientePorDni(dni);

        if (paciente == null) {
            System.out.println("El paciente no se encuentra. Procediendo a registrar nuevo ingreso...");
            System.out.print("Apellido: ");
            String apellido = scanner.nextLine();
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            LocalDate fechaNac = leerFecha(scanner, "Fecha de Nacimiento");
            System.out.print("Obra Social: ");
            String obraSocial = scanner.nextLine();
            LocalDate fechaIng = leerFecha(scanner, "Fecha de Ingreso");
            System.out.print("Observación inicial: ");
            String observacion = scanner.nextLine();

            Paciente nuevoPaciente = new Paciente(apellido, nombre, fechaNac, dni, obraSocial, fechaIng, observacion);
            hospital.agregarPaciente(nuevoPaciente);
        } else {
                System.out.println("\nPaciente encontrado.");
                paciente.mostrarDatos();

                if (paciente.getFechaEgreso() != null) {

                    System.out.print("\n¿Desea registrar un nuevo ingreso para este paciente? (S/N): ");
                    String respuesta = scanner.nextLine();

                    if (respuesta.equalsIgnoreCase("S")) {

                        LocalDate fechaIngreso = leerFecha(scanner, "Fecha de ingreso");

                        System.out.print("Observación inicial: ");
                        String observacion = scanner.nextLine();

                        paciente.registrarNuevoIngreso(fechaIngreso, observacion);

                        System.out.println("Nuevo ingreso registrado correctamente.");
                    }
                }

                subMenuPaciente(scanner, hospital, paciente);
            }
    }

    private static void subMenuPaciente(Scanner scanner, GestorHospital hospital, Paciente paciente) {
        int subOpcion = 0;
        do {
            paciente.mostrarDatos();
            System.out.println("¿Qué acción desea realizar?");
            System.out.println("1. Dar el alta (Registrar fecha de egreso)");
            System.out.println("2. Modificar Obra Social");
            System.out.println("3. Agregar/Modificar una observación");
            System.out.println("4. Ver historial del paciente");
            System.out.println("5. Guardar historial en archivo");
            System.out.println("6. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            try {
                subOpcion = scanner.nextInt();
                scanner.nextLine(); // Consumir salto de línea
            } catch (InputMismatchException e) {
                System.out.println("Error: Ingrese un número válido.");
                scanner.nextLine();
                continue;
            }

            switch (subOpcion) {
                case 1:
                    LocalDate egreso = leerFecha(scanner, "Fecha de egreso");
                    paciente.setFechaEgreso(egreso);
                    System.out.println("Alta registrada con éxito.");
                    break;
                case 2:
                    System.out.print("Ingrese la nueva Obra Social: ");
                    String nuevaObra = scanner.nextLine();
                    paciente.setObraSocial(nuevaObra);
                    System.out.println("Obra social actualizada.");
                    break;
                case 3:
                    System.out.print("Ingrese la nueva observación: ");
                    String nuevaObs = scanner.nextLine();
                    paciente.setObservacion(nuevaObs);
                    System.out.println("Observación actualizada.");
                    break;
                case 4:
                    paciente.mostrarHistorial();
                    break;
                case 5:
                    hospital.guardarHistorialEnArchivo(paciente);
                    break;
                case 6:
                    System.out.println("Regresando...");
                    break;
                default:
                    break;
            }
        } while (subOpcion != 6);
    }

    // Punto 4: filtra pacientes por nombre y/o apellido, muestra los resultados con su DNI
    // y ofrece la posibilidad de guardarlos en un archivo nuevo.
    private static void menuBuscarPorNombreApellido(Scanner scanner, GestorHospital hospital) {
        System.out.print("\nIngrese el Apellido a buscar (dejar vacío para omitir): ");
        String apellido = scanner.nextLine();
        System.out.print("Ingrese el Nombre a buscar (dejar vacío para omitir): ");
        String nombre = scanner.nextLine();

        ArrayList<Paciente> resultados = hospital.buscarPorNombreYApellido(nombre, apellido);

        if (resultados.isEmpty()) {
            System.out.println("No se encontraron pacientes con esos criterios.");
        } else {
            System.out.println("\n##### Resultados encontrados #####");
            for (Paciente p : resultados) {
                System.out.println("DNI: " + p.getDni() + " | " + p.getApellido() + ", " + p.getNombre());
            }
        }

        System.out.print("\n¿Desea guardar estos resultados en un archivo? (S/N): ");
        String respuesta = scanner.nextLine();
        if (respuesta.equalsIgnoreCase("S")) {
            hospital.guardarResultadosEnArchivo(resultados);
        }
    }
}