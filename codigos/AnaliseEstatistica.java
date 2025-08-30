package codigos;

import java.util.*;

public class AnaliseEstatistica {

    private final Campeonato campeonato;

    public AnaliseEstatistica(Campeonato campeonato) {
        this.campeonato = campeonato;
    }

    public Map<String, Time> gerarTimes() {
        Map<String, Time> times = new LinkedHashMap<>();
        for (Partida p : campeonato.getPartidas()) {
            times.putIfAbsent(p.getMandante(), new Time(p.getMandante(), "", ""));
            times.putIfAbsent(p.getVisitante(), new Time(p.getVisitante(), "", ""));
            times.get(p.getMandante()).atualizarEstatisticas(p);
            times.get(p.getVisitante()).atualizarEstatisticas(p);
        }
        return times;
    }

    public List<Time> rankingPorPontos() {
        List<Time> ranking = new ArrayList<>(gerarTimes().values());
        ranking.sort(Comparator.comparingInt(Time::getPontuacao).reversed());
        return ranking;
    }


    public Map<String, Time> gerarTimes(int ano) {
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

    public List<Time> melhoresDefesasPorAno(int ano, int top) {
    Map<String, Time> times = gerarTimes(ano);
    return times.values().stream()
            .sorted(Comparator.comparingInt(Time::getGolsContra)) 
            .limit(top)
            .toList();
}


    public List<Time> melhorAtaquePorAno(int ano, int top) {
        Map<String, Time> times = gerarTimes(ano);
        return times.values().stream()
            .sorted(Comparator.comparingInt(Time::getGolsPro).reversed()) 
            .limit(top)
            .toList();
    }


    public List<Time> topVitoriasMandante(int top) {
     return gerarTimes().values().stream()
            .sorted(Comparator.comparingInt(Time::getVitoriasMandante).reversed())
            .limit(top)
            .toList();
    }


    public List<Time> topVitoriasVisitante(int top) {
     return gerarTimes().values().stream()
            .sorted(Comparator.comparingInt(Time::getVitoriasVisitante).reversed())
            .limit(top)
            .toList();
    }

}
