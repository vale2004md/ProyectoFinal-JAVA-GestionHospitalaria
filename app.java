package app;

import java.util.InputMismatchException;
import java.util.Scanner;

public class App {

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
                    System.out.println("Saliendo del sistema hospitalario...");
                    break;
                default:
                    System.out.println("Opción inválida.");
                    break;
            }
        } while (opcion != 4);
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\n####### SISTEMA DE GESTIÓN HOSPITALARIA #######");
        System.out.println("1. Buscar / Ingresar Paciente");
        System.out.println("2. Listar todos los pacientes");
        System.out.println("3. Extraer datos a CSV");
        System.out.println("4. Salir");
        System.out.print("Seleccione una opción: ");
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
            System.out.print("Fecha de Nacimiento (DD/MM/AAAA): ");
            String fechaNac = scanner.nextLine();
            System.out.print("Obra Social: ");
            String obraSocial = scanner.nextLine();
            System.out.print("Fecha de Ingreso (DD/MM/AAAA): ");
            String fechaIng = scanner.nextLine();
            System.out.print("Observación inicial: ");
            String observacion = scanner.nextLine();

            Paciente nuevoPaciente = new Paciente(apellido, nombre, fechaNac, dni, obraSocial, fechaIng, observacion);
            hospital.agregarPaciente(nuevoPaciente);
        } else {
            // Si el paciente existe, despliega el submenú de edición
            subMenuPaciente(scanner, paciente);
        }
    }

    private static void subMenuPaciente(Scanner scanner, Paciente paciente) {
        int subOpcion = 0;
        do {
            paciente.mostrarDatos();
            System.out.println("¿Qué acción desea realizar?");
            System.out.println("1. Dar el alta (Registrar fecha de egreso)");
            System.out.println("2. Modificar Obra Social");
            System.out.println("3. Agregar/Modificar una observación");
            System.out.println("4. Volver al menú principal");
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
                    System.out.print("Ingrese la fecha de egreso (DD/MM/AAAA): ");
                    String egreso = scanner.nextLine();
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
                    System.out.println("Regresando...");
                    break;
                default:
                    System.out.println("Opción inválida.");
                    break;
            }
        } while (subOpcion != 4);
    }
}
