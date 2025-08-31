package codigos;

import java.util.*;
import java.util.stream.Collectors;

public class CalculoEstatistica extends AnaliseEstatistica {

    public CalculoEstatistica(Campeonato campeonato) {
        super(campeonato);
    }

    @Override
    public List<Time> rankingPorPontos() {
        List<Time> ranking = new ArrayList<>(gerarTimes().values());
        ranking.sort(Comparator.comparingInt(Time::getPontuacao).reversed());
        return ranking;
    }

    @Override
    public List<Time> melhoresDefesasPorAno(int ano, int top) {
        Map<String, Time> times = gerarTimes(ano);
        return times.values().stream()
                .sorted(Comparator.comparingInt(Time::getGolsContra))
                .limit(top)
                .collect(Collectors.toList());
    }

    @Override
    public List<Time> melhorAtaquePorAno(int ano, int top) {
        Map<String, Time> times = gerarTimes(ano);
        return times.values().stream()
                .sorted(Comparator.comparingInt(Time::getGolsPro).reversed())
                .limit(top)
                .collect(Collectors.toList());
    }

    @Override
    public List<Time> topVitoriasMandante(int top) {
        return gerarTimes().values().stream()
                .sorted(Comparator.comparingInt(Time::getVitoriasMandante).reversed())
                .limit(top)
                .collect(Collectors.toList());
    }

    @Override
    public List<Time> topVitoriasVisitante(int top) {
        return gerarTimes().values().stream()
                .sorted(Comparator.comparingInt(Time::getVitoriasVisitante).reversed())
                .limit(top)
                .collect(Collectors.toList());
    }

    // métodos auxiliares privados
    private Map<String, Time> gerarTimes() {
        Map<String, Time> times = new LinkedHashMap<>();
        for (Partida p : campeonato.getPartidas()) {
            times.putIfAbsent(p.getMandante(), new Time(p.getMandante(), "", ""));
            times.putIfAbsent(p.getVisitante(), new Time(p.getVisitante(), "", ""));
            times.get(p.getMandante()).atualizarEstatisticas(p);
            times.get(p.getVisitante()).atualizarEstatisticas(p);
        }
        return times;
    }

    private Map<String, Time> gerarTimes(int ano) {
        Map<String, Time> times = new LinkedHashMap<>();
        List<Partida> partidasDoAno = campeonato.getPartidasPorAno(ano);
        for (Partida p : partidasDoAno) {
            times.putIfAbsent(p.getMandante(), new Time(p.getMandante(), "", ""));
            times.putIfAbsent(p.getVisitante(), new Time(p.getVisitante(), "", ""));
            times.get(p.getMandante()).atualizarEstatisticas(p);
            times.get(p.getVisitante()).atualizarEstatisticas(p);
        }
        return times;
    }
}
