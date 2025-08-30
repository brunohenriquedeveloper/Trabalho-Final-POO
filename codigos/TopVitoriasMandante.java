package codigos;

import java.util.*;
import java.util.stream.Collectors;

public class TopVitoriasMandante extends Estatistica {

    public TopVitoriasMandante(Campeonato campeonato) {
        super(campeonato);
    }

    @Override
    public List<Time> analisar(int ano, int top) {
        Map<String, Time> times = (ano == 0) ? gerarTimes() : gerarTimes(ano);

        // Ordena os times pelo número de vitórias como mandante
        return times.values().stream()
                .sorted(Comparator.comparingInt(Time::getVitoriasMandante).reversed())
                .limit(top)
                .collect(Collectors.toList());
    }
}
