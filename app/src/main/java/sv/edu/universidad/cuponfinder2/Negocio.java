package sv.edu.universidad.cuponfinder2;

public class Negocio {
    public String nombre;
    public String categoria;
    public int imagen;

    public Negocio(String nombre, String categoria, int imagen) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.imagen = imagen;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getImagen() {
        return imagen;
    }

    public String getNombre() {
        return nombre;
    }
}