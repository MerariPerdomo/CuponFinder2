package sv.edu.universidad.cuponfinder2.Model;

public class Usuarios {
    String email, negocio, nombre, password;

    public Usuarios(){

    }
    public Usuarios(String email, String negocio, String nombre, String password){
        this.email = email;
        this.negocio = negocio;
        this.nombre = nombre;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNegocio() {
        return negocio;
    }

    public void setNegocio(String negocio) {
        this.negocio = negocio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre() {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
