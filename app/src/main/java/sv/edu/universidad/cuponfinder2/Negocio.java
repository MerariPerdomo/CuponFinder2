package sv.edu.universidad.cuponfinder2;

public class Negocio {
    public String nombre;
    public String categoria;
    public int perfil_negocio;
    public int imagen;

    public Negocio(String nombre, String categoria, int imagen, int perfil_negocio) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.imagen = imagen;
        this.perfil_negocio= perfil_negocio;
    }

    public int getPerfil_negocio() {
        return perfil_negocio;
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