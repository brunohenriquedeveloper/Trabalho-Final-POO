package codigos;

import java.util.*;
import java.util.stream.Collectors;

public class TopVitoriasVisitante extends Estatistica {

    public TopVitoriasVisitante(Campeonato campeonato) {
        super(campeonato);
    }

    @Override
    public List<Time> analisar(int ano, int top) {
        Map<String, Time> times = (ano == 0) ? gerarTimes() : gerarTimes(ano);

        // Ordena os times pelo número de vitórias como visitante
        return times.values().stream()
                .sorted(Comparator.comparingInt(Time::getVitoriasVisitante).reversed())
                .limit(top)
                .collect(Collectors.toList());
    }
}
