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

    // --------- Construtor ---------
    public Partida(
            String mandante,
            String visitante,
            String estadio,
            String data,
            String hora,
            String vencedor,
            int placarMandante,
            int placarVisitante
    ) {
        this.mandante = mandante;
        this.visitante = visitante;
        this.estadio = estadio;
        this.data = data;
        this.hora = hora;
        this.vencedor = vencedor;
        this.placarMandante = placarMandante;
        this.placarVisitante = placarVisitante;
    }

    // --------- Getters ---------
    public String getMandante() {
        return mandante;
    }

    public String getVisitante() {
        return visitante;
    }

    public String getEstadio() {
        return estadio;
    }

    public String getData() {
        return data;
    }

    public String getHora() {
        return hora;
    }

    public String getVencedor() {
        return vencedor;
    }

    public int getPlacarMandante() {
        return placarMandante;
    }

    public int getPlacarVisitante() {
        return placarVisitante;
    }

    // --------- Representação textual ---------
    @Override
    public String toString() {
        return mandante + " " +
               placarMandante + " x " +
               placarVisitante + " " +
               visitante;
    }
}
