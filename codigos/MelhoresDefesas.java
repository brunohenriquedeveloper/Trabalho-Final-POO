package codigos;

import java.util.*;

public class MelhoresDefesas extends Estatistica {

    public MelhoresDefesas(Campeonato campeonato) {
        super(campeonato);
    }

    @Override
    public List<Time> analisar(int ano, int top) {
        return gerarTimes(ano).values().stream()
            .sorted(Comparator.comparingInt(Time::getGolsContra))
            .limit(top)
            .toList();
    }
}
