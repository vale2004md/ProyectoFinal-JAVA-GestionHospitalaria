import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class GestorHospital {
    private ArrayList<Paciente> pacientes;

    public GestorHospital() {
    this.pacientes = new ArrayList<>();
    cargarPacientes();
}

    public void agregarPaciente(Paciente p) {
        pacientes.add(p);
        System.out.println("Paciente registrado con éxito.");
    }

    // Buscamos por DNI para evitar duplicados o errores por nombres idénticos
    public Paciente buscarPacientePorDni(String dni) {
        for (Paciente p : pacientes) {
            if (p.getDni().equals(dni)) {
                return p;
            }
        }
        return null;
    }

    // Punto 4: filtra por nombre y/o apellido (sin distinguir mayúsculas/minúsculas)
    // y devuelve todas las coincidencias junto con su DNI.
    public ArrayList<Paciente> buscarPorNombreYApellido(String nombre, String apellido) {
        ArrayList<Paciente> resultados = new ArrayList<>();
        for (Paciente p : pacientes) {
            boolean coincideNombre = nombre.isEmpty()
                    || p.getNombre().toLowerCase().contains(nombre.toLowerCase());
            boolean coincideApellido = apellido.isEmpty()
                    || p.getApellido().toLowerCase().contains(apellido.toLowerCase());
            if (coincideNombre && coincideApellido) {
                resultados.add(p);
            }
        }
        return resultados;
    }

    public void listarPacientes() {
        if (pacientes.isEmpty()) {
            System.out.println("No hay pacientes registrados.");
            return;
        }
        for (Paciente p : pacientes) {
            p.mostrarDatos();
        }
    }

    // Exportación a archivo CSV
    public void exportarA_CSV() {
        String nombreArchivo = "pacientes.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            // Cabecera del CSV
            writer.write("DNI,Apellido,Nombre,FechaNacimiento,ObraSocial,FechaIngreso,FechaEgreso,Observacion");
            writer.newLine();

            for (Paciente p : pacientes) {
                writer.write(p.toCsvLinea());
                writer.newLine();
            }
            System.out.println("Datos exportados exitosamente a " + nombreArchivo);
        } catch (IOException e) {
            System.out.println("Error al exportar a CSV: " + e.getMessage());
        }
    }

    // Punto 4/5: guarda el resultado de una búsqueda (nombre/apellido -> DNI) en un archivo nuevo.
    // El nombre del archivo incluye la fecha y hora real (java.time) para no pisar resultados anteriores.
    public void guardarResultadosEnArchivo(ArrayList<Paciente> resultados) {
        DateTimeFormatter formatoArchivo = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String marca = LocalDateTime.now().format(formatoArchivo);
        String nombreArchivo = "resultados_busqueda_" + marca + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            writer.write("Resultados de búsqueda");
            writer.newLine();
            writer.write("----------------------------------------");
            writer.newLine();

            if (resultados.isEmpty()) {
                writer.write("No se encontraron pacientes con esos criterios.");
                writer.newLine();
            } else {
                for (Paciente p : resultados) {
                    writer.write("DNI: " + p.getDni() + " | " + p.getApellido() + ", " + p.getNombre());
                    writer.newLine();
                }
            }
            System.out.println("Resultados guardados exitosamente en " + nombreArchivo);
        } catch (IOException e) {
            // Manejo de excepción de archivo requerido por el punto 5
            System.out.println("Error al guardar los resultados en el archivo: " + e.getMessage());
        }
    }

    // Guarda el historial completo de un paciente puntual en un archivo de texto
    public void guardarHistorialEnArchivo(Paciente p) {
        String nombreArchivo = "historial_" + p.getDni() + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            writer.write("Historial de " + p.getApellido() + ", " + p.getNombre() + " (DNI " + p.getDni() + ") | Obra Social: " + p.getObraSocial());
            writer.newLine();
            writer.write("----------------------------------------");
            writer.newLine();

            for (String evento : p.getHistorial()) {
                writer.write(evento);
                writer.newLine();
            }
            System.out.println("Historial guardado exitosamente en " + nombreArchivo);
        } catch (IOException e) {
            System.out.println("Error al guardar el historial en el archivo: " + e.getMessage());
        }
    }
    private void cargarPacientes() {
    String nombreArchivo = "pacientes.csv";
    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/uuuu");

    try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {

    // Saltar la cabecera
    reader.readLine();

    String linea;

    while ((linea = reader.readLine()) != null) {

        String[] datos = linea.split(",");

        String dni = datos[0];
        String apellido = datos[1];
        String nombre = datos[2];
        LocalDate fechaNacimiento = LocalDate.parse(datos[3], formato);
        String obraSocial = datos[4];
        LocalDate fechaIngreso = LocalDate.parse(datos[5], formato);
        String observacion = datos[7];

        Paciente paciente = new Paciente(
                apellido,
                nombre,
                fechaNacimiento,
                dni,
                obraSocial,
                fechaIngreso,
                observacion
        );

        if (!datos[6].equalsIgnoreCase("Pendiente")) {
            paciente.setFechaEgreso(LocalDate.parse(datos[6], formato));
        }

        pacientes.add(paciente);
    }

    System.out.println("Pacientes cargados correctamente desde el archivo.");

} catch (IOException e) {
    System.out.println("Error al leer el archivo: " + e.getMessage());
} catch (Exception e) {
    System.out.println("Error en el formato del archivo CSV: " + e.getMessage());
}
}
}