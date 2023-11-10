package sv.edu.universidad.cuponfinder2.domain;

public class CategoryDomain {
    private String titulo;
    private String draw;

    public CategoryDomain(String titulo, String draw){
        this.titulo =titulo;
        this.draw=draw;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDraw() {
        return draw;
    }

    public void setDraw(String draw) {
        this.draw = draw;
    }
}
