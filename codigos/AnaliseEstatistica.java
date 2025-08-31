package codigos;

import java.util.List;

public abstract class AnaliseEstatistica {

    protected final Campeonato campeonato;

    public AnaliseEstatistica(Campeonato campeonato) {
        this.campeonato = campeonato;
    }

    public abstract List<Time> rankingPorPontos();
    public abstract List<Time> melhoresDefesasPorAno(int ano, int top);
    public abstract List<Time> melhorAtaquePorAno(int ano, int top);
    public abstract List<Time> topVitoriasMandante(int top);
    public abstract List<Time> topVitoriasVisitante(int top);
}
