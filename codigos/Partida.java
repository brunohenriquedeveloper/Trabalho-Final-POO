package codigos;

public class Partida {
    private int id;
    private String mandante;
    private String visitante;
    private String estadio;
    private String data;
    private String hora;
    private String vencedor;
    private int placarMandante;
    private int placarVisitante;

    // Construtor deve estar dentro da classe
    public Partida(String mandante, String visitante, String estadio, String data, String hora,
                   String vencedor, int placarMandante, int placarVisitante) {
        this.mandante = mandante;
        this.visitante = visitante;
        this.estadio = estadio;
        this.data = data;
        this.hora = hora;
        this.vencedor = vencedor;
        this.placarMandante = placarMandante;
        this.placarVisitante = placarVisitante;
    }

    // Getters e setters
    public String getMandante() { return mandante; }
    public void setMandante(String mandante) { this.mandante = mandante; }
    public String getVisitante() { return visitante; }
    public void setVisitante(String visitante) { this.visitante = visitante; }
    public String getEstadio() { return estadio; }
    public void setEstadio(String estadio) { this.estadio = estadio; }
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
    public String getVencedor() { return vencedor; }
    public void setVencedor(String vencedor) { this.vencedor = vencedor; }
    public int getPlacarMandante() { return placarMandante; }
    public void setPlacarMandante(int placarMandante) { this.placarMandante = placarMandante; }
    public int getPlacarVisitante() { return placarVisitante; }
    public void setPlacarVisitante(int placarVisitante) { this.placarVisitante = placarVisitante; }

    @Override
    public String toString() {
        return mandante + " " + placarMandante + " x " + placarVisitante + " " + visitante;
    }
}
