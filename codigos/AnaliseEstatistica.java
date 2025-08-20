package codigos;

import java.util.*;

public class AnaliseEstatistica {

    private Campeonato campeonato;

    public AnaliseEstatistica(Campeonato campeonato) {
        this.campeonato = campeonato;
    }

    // Cria um mapa com todos os times e suas estatísticas
    public Map<String, Time> gerarTimes() {
        Map<String, Time> times = new HashMap<>();

        for (Partida p : campeonato.getPartidas()) {
            // Cria time mandante se não existir
            times.putIfAbsent(p.getMandante(), new Time(p.getMandante(), "", ""));
            // Cria time visitante se não existir
            times.putIfAbsent(p.getVisitante(), new Time(p.getVisitante(), "", ""));

            // Atualiza estatísticas
            times.get(p.getMandante()).atualizarEstatisticas(p);
            times.get(p.getVisitante()).atualizarEstatisticas(p);
        }

        return times;
    }

    // Retorna ranking por pontos
    public List<Time> rankingPorPontos() {
        List<Time> ranking = new ArrayList<>(gerarTimes().values());
        ranking.sort((t1, t2) -> Integer.compare(t2.getPontuacao(), t1.getPontuacao())); 
        return ranking;
    }

    // Time com mais gols
    public Time timeComMaisGols() {
        return gerarTimes().values().stream()
                .max(Comparator.comparingInt(t -> t.getPontuacao()))
                .orElse(null);
    }
}
