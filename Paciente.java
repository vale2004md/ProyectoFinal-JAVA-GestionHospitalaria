package app;

public class Paciente {
    private String apellido;
    private String nombre;
    private String fechaNacimiento;
    private String dni;
    private String obraSocial;
    private String fechaIngreso;
    private String fechaEgreso; // Puede ser "Pendiente" si sigue internado
    private String observacion;

    // Constructor
    public Paciente(String apellido, String nombre, String fechaNacimiento, String dni,
                    String obraSocial, String fechaIngreso, String observacion) {
        this.apellido = apellido;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.dni = dni;
        this.obraSocial = obraSocial;
        this.fechaIngreso = fechaIngreso;
        this.fechaEgreso = "Pendiente"; // Por defecto al ingresar no tiene alta
        this.observacion = observacion;
    }

    // Getters y Setters
    public String getApellido() { return apellido; }
    public String getNombre() { return nombre; }
    public String getFechaNacimiento() { return fechaNacimiento; }
    public String getDni() { return dni; }

    public String getObraSocial() { return obraSocial; }
    public void setObraSocial(String obraSocial) { this.obraSocial = obraSocial; }

    public String getFechaIngreso() { return fechaIngreso; }

    public String getFechaEgreso() { return fechaEgreso; }
    public void setFechaEgreso(String fechaEgreso) { this.fechaEgreso = fechaEgreso; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    // Método para mostrar los datos de forma limpia
    public void mostrarDatos() {
        System.out.println("\n=== ESTADO DEL PACIENTE ===");
        System.out.println("DNI: " + dni);
        System.out.println("Paciente: " + apellido + ", " + nombre);
        System.out.println("F. Nacimiento: " + fechaNacimiento);
        System.out.println("Obra Social: " + obraSocial);
        System.out.println("Fecha Ingreso: " + fechaIngreso);
        System.out.println("Fecha Egreso: " + fechaEgreso);
        System.out.println("Observación: " + observacion);
        System.out.println("===========================");
    }
}
