package codigos;

import java.util.*;

public class RankingPorGols extends Estatistica {

    public RankingPorGols(Campeonato campeonato) {
        super(campeonato);
    }

    @Override
    public List<Time> analisar(int ano, int top) {
        List<Time> times = new ArrayList<>(gerarTimes(ano).values());
        times.sort(Comparator.comparingInt(Time::getGolsPro).reversed());
        return times.subList(0, Math.min(top, times.size()));
    }
}

