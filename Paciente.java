import java.time.LocalDate;
//import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Paciente {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/uuuu");
    //private static final DateTimeFormatter FORMATO_HISTORIAL = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String apellido;
    private String nombre;
    private LocalDate fechaNacimiento;
    private String dni;
    private String obraSocial;
    private LocalDate fechaIngreso;
    private LocalDate fechaEgreso; // null mientras el paciente sigue internado
    private String observacion;

    private List<String> historial; // registro de eventos con fecha/hora real

    // Constructor
    public Paciente(String apellido, String nombre, LocalDate fechaNacimiento, String dni,
                     String obraSocial, LocalDate fechaIngreso, String observacion) {
        this.apellido = apellido;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.dni = dni;
        this.obraSocial = obraSocial;
        this.fechaIngreso = fechaIngreso;
        this.fechaEgreso = null; // Por defecto al ingresar no tiene alta
        this.observacion = observacion;

        this.historial = new ArrayList<>();
        registrarEvento("Ingreso registrado. " + " Observación: " + observacion);
    }

    // Getters y Setters
    public String getApellido() { return apellido; }
    public String getNombre() { return nombre; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getDni() { return dni; }
    public String getObraSocial() { return obraSocial; }

    // public String getObraSocial() { return obraSocial; }
    public void setObraSocial(String obraSocial) {
        registrarEvento("Obra social modificada: '" + this.obraSocial + "' -> '" + obraSocial + "'");
        this.obraSocial = obraSocial;
    }

    public LocalDate getFechaIngreso() { return fechaIngreso; }

    public LocalDate getFechaEgreso() { return fechaEgreso; }
    public void setFechaEgreso(LocalDate fechaEgreso) {
        this.fechaEgreso = fechaEgreso;
        registrarEvento("Alta registrada. Fecha de egreso: " + fechaEgreso.format(FORMATO_FECHA));
    }

    public String getObservacion() { return observacion; }

    public void registrarNuevoIngreso(LocalDate fechaIngreso, String observacion) {
    this.fechaIngreso = fechaIngreso;
    this.fechaEgreso = null;
    this.observacion = observacion;

    registrarEvento("Nuevo ingreso. Fecha de ingreso: "
            + fechaIngreso.format(FORMATO_FECHA)
            + ". Observación: "
            + observacion);
}
    public void setObservacion(String observacion) {
        registrarEvento("Nueva observación: " + observacion);
        this.observacion = observacion;
    }

    public List<String> getHistorial() { return historial; }

    // Agrega una entrada al historial con fecha y hora reales (java.time)
    private void registrarEvento(String descripcion) {
       // String marcaTiempo = LocalDateTime.now().format(FORMATO_HISTORIAL);
        historial.add( descripcion );
    }

    // Devuelve "Pendiente" si el paciente sigue internado (fechaEgreso == null)
    private String textoFechaEgreso() {
        return (fechaEgreso == null) ? "Pendiente" : fechaEgreso.format(FORMATO_FECHA);
    }

    // Método para mostrar los datos de forma limpia
    public void mostrarDatos() {
        System.out.println("\n=== ESTADO DEL PACIENTE ===");
        System.out.println("DNI: " + dni);
        System.out.println("Paciente: " + apellido + ", " + nombre);
        System.out.println("F. Nacimiento: " + fechaNacimiento.format(FORMATO_FECHA));
        System.out.println("Obra Social: " + obraSocial);
        System.out.println("Fecha Ingreso: " + fechaIngreso.format(FORMATO_FECHA));
        System.out.println("Fecha Egreso: " + textoFechaEgreso());
        System.out.println("Observación: " + observacion);
        System.out.println("===========================");
    }

    // Muestra el historial completo de eventos del paciente
    public void mostrarHistorial() {
        System.out.println("\n=== HISTORIAL DE " + apellido + ", " + nombre + " (DNI " + dni + ")" + " | Obra Social: " + obraSocial +"   ===");
        if (historial.isEmpty()) {
            System.out.println("Sin eventos registrados.");
        } else {
            for (String evento : historial) {
                System.out.println(evento);
            }
        }
        System.out.println("===========================");
    }

    // Representación en una línea para el CSV
    public String toCsvLinea() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                dni, apellido, nombre, fechaNacimiento.format(FORMATO_FECHA),
                obraSocial, fechaIngreso.format(FORMATO_FECHA), textoFechaEgreso(), observacion);
    }
}