package codigos;

import java.util.*;

import codigos.Campeonato;

public abstract class Estatistica {

    protected final Campeonato campeonato;

    public Estatistica(Campeonato campeonato) {
        this.campeonato = campeonato;
    }

    // Métodos utilitários comuns
    protected Map<String, Time> gerarTimes() {
        Map<String, Time> times = new LinkedHashMap<>();
        for (Partida p : campeonato.getPartidas()) {
            times.putIfAbsent(p.getMandante(), new Time(p.getMandante(), "", ""));
            times.putIfAbsent(p.getVisitante(), new Time(p.getVisitante(), "", ""));
            times.get(p.getMandante()).atualizarEstatisticas(p);
            times.get(p.getVisitante()).atualizarEstatisticas(p);
        }
        return times;
    }

    /**
     * Gera o mapa de times com base no ano.
     * Se ano == 0 => usa todas as partidas (todas as temporadas).
     * Se ano > 0  => usa apenas partidas daquele ano via getPartidasPorAno(ano).
     */
    protected Map<String, Time> gerarTimes(int ano) {
        Map<String, Time> times = new LinkedHashMap<>();
        List<Partida> partidas;
        if (ano <= 0) {
            // 0 ou valores negativos significam "todas as temporadas"
            partidas = campeonato.getPartidas();
        } else {
            partidas = campeonato.getPartidasPorAno(ano);
        }

        if (partidas == null) partidas = Collections.emptyList();

        for (Partida p : partidas) {
            times.putIfAbsent(p.getMandante(), new Time(p.getMandante(), "", ""));
            times.putIfAbsent(p.getVisitante(), new Time(p.getVisitante(), "", ""));
            times.get(p.getMandante()).atualizarEstatisticas(p);
            times.get(p.getVisitante()).atualizarEstatisticas(p);
        }
        return times;
    }

    // Cada estatística define como calcula seu resultado
    public abstract List<Time> analisar(int ano, int top);
}
