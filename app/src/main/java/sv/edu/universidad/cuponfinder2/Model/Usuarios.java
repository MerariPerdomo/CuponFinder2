package sv.edu.universidad.cuponfinder2.Model;

public class Usuarios {
    String email, negocio, nombre, password, idUser;

    public Usuarios(){

    }
    public Usuarios(String email, String negocio, String nombre, String password, String idUser){
        this.email = email;
        this.negocio = negocio;
        this.nombre = nombre;
        this.password = password;
        this.idUser = idUser;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
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
