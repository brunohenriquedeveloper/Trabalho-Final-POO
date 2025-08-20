package codigos;

public class Time {
    private String nome;
    private String tecnico;
    private String estado;

    private int vitorias = 0;
    private int empates = 0;
    private int derrotas = 0;
    private int golsPro = 0;
    private int golsContra = 0;

    public Time(String nome, String tecnico, String estado) {
        this.nome = nome;
        this.tecnico = tecnico;
        this.estado = estado;
    }

    // Atualiza estatísticas com base em uma partida
    public void atualizarEstatisticas(Partida p) {
        if (p.getMandante().equals(nome)) {
            golsPro += p.getPlacarMandante();
            golsContra += p.getPlacarVisitante();
            if (p.getVencedor().equals(nome)) vitorias++;
            else if (p.getVencedor().equals("Empate")) empates++;
            else derrotas++;
        } else if (p.getVisitante().equals(nome)) {
            golsPro += p.getPlacarVisitante();
            golsContra += p.getPlacarMandante();
            if (p.getVencedor().equals(nome)) vitorias++;
            else if (p.getVencedor().equals("Empate")) empates++;
            else derrotas++;
        }
    }

    public int getPontuacao() {
        return vitorias * 3 + empates;
    }

    public int getSaldoGols() {
        return golsPro - golsContra;
    }

    public String getNome() { return nome; }

    @Override
    public String toString() {
        return nome + " - Pontos: " + getPontuacao() + ", Gols: " + golsPro + ":" + golsContra + ", Saldo: " + getSaldoGols();
    }
}