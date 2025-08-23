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

    // Novos atributos para estatísticas adicionais
    private int vitoriasMandante = 0;
    private int vitoriasVisitante = 0;
    private int partidasJogadas = 0;

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
            partidasJogadas++;

            if (p.getVencedor().equals(nome)) {
                vitorias++;
                vitoriasMandante++;
            } else if (p.getVencedor().equals("Empate")) {
                empates++;
            } else {
                derrotas++;
            }

        } else if (p.getVisitante().equals(nome)) {
            golsPro += p.getPlacarVisitante();
            golsContra += p.getPlacarMandante();
            partidasJogadas++;

            if (p.getVencedor().equals(nome)) {
                vitorias++;
                vitoriasVisitante++;
            } else if (p.getVencedor().equals("Empate")) {
                empates++;
            } else {
                derrotas++;
            }
        }
    }

    public int getPontuacao() {
        return vitorias * 3 + empates;
    }

    public int getSaldoGols() {
        return golsPro - golsContra;
    }

    public double getMediaGolsSofridos() {
        return partidasJogadas > 0 ? (double) golsContra / partidasJogadas : 0.0;
    }

    public int getGolsPro() { return golsPro; }

    public int getGolsContra() { return golsContra; }

    public int getVitoriasMandante() { return vitoriasMandante; }

    public int getVitoriasVisitante() { return vitoriasVisitante; }

    public String getNome() { return nome; }

    @Override
    public String toString() {
        return nome + " - Pontos: " + getPontuacao() +
                ", Gols: " + golsPro + ":" + golsContra +
                ", Saldo: " + getSaldoGols() +
                ", Vitórias Casa: " + vitoriasMandante +
                ", Vitórias Fora: " + vitoriasVisitante +
                ", Média Gols Sofridos: " + String.format("%.2f", getMediaGolsSofridos());
    }
}
