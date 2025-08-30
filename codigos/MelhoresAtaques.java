package codigos;

import java.util.*;

public class MelhoresAtaques extends Estatistica {

    public MelhoresAtaques(Campeonato campeonato) {
        super(campeonato);
    }

    @Override
    public List<Time> analisar(int ano, int top) {
        return gerarTimes(ano).values().stream()
            .sorted(Comparator.comparingInt(Time::getGolsPro).reversed())
            .limit(top)
            .toList();
    }
}
