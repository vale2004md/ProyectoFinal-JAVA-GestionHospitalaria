import java.io.IOException;
import java.util.ArrayList;

public class GestorHospital {
    private ArrayList<Paciente> pacientes;

    public GestorHospital() {
        this.pacientes = new ArrayList<>();
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
                String linea = String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                        p.getDni(), p.getApellido(), p.getNombre(), p.getFechaNacimiento(),
                        p.getObraSocial(), p.getFechaIngreso(), p.getFechaEgreso(), p.getObservacion());
                writer.write(linea);
                writer.newLine();
            }
            System.out.println("Datos exportados exitosamente a " + nombreArchivo);
        } catch (IOException e) {
            System.out.println("Error al exportar a CSV: " + e.getMessage());
        }
    }
}
