package codigos;

import java.util.*;
import java.util.stream.Collectors;

public class AnaliseEstatistica {

    private final Campeonato campeonato;

    public AnaliseEstatistica(Campeonato campeonato) {
        this.campeonato = campeonato;
    }

    // --------- GERAL (todas as edições) ---------
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

    public Time timeComMaisGols() {
        return gerarTimes().values().stream()
                .max(Comparator.comparingInt(Time::getGolsPro))
                .orElse(null);
    }

    public Time melhorDefesaGeral() {
        return gerarTimes().values().stream()
                .min(Comparator.comparingInt(Time::getGolsContra))
                .orElse(null);
    }

    public List<Time> rankingPorMediaGolsSofridos() {
        List<Time> ranking = new ArrayList<>(gerarTimes().values());
        ranking.sort(Comparator.comparingDouble(Time::getMediaGolsSofridos));
        return ranking;
    }

    public Time maisVitoriasMandanteGeral() {
        return gerarTimes().values().stream()
                .max(Comparator.comparingInt(Time::getVitoriasMandante))
                .orElse(null);
    }

    public Time maisVitoriasVisitanteGeral() {
        return gerarTimes().values().stream()
                .max(Comparator.comparingInt(Time::getVitoriasVisitante))
                .orElse(null);
    }

    // --------- POR ANO (somente a temporada escolhida) ---------

    // Mapa de times considerando apenas as partidas daquele ano
    public Map<String, Time> gerarTimes(int ano) {
        Map<String, Time> times = new LinkedHashMap<>();

        // Se seu Campeonato já tem este método, use-o:
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
            .sorted(Comparator.comparingInt(Time::getGolsContra)) // menor gols sofridos primeiro
            .limit(top)
            .toList();
}


    // Ranking de melhor defesa no ano (menor gols contra primeiro)
    public List<Time> melhorDefesaPorAno(int ano) {
        Map<String, Time> times = gerarTimes(ano);
        List<Time> lista = new ArrayList<>(times.values());
        lista.sort(Comparator.comparingInt(Time::getGolsContra)); // crescente
        return lista;
    }

    // Só o TOP 1 de melhor defesa no ano
    public Time melhorDefesaTop1PorAno(int ano) {
        return melhorDefesaPorAno(ano).stream().findFirst().orElse(null);
    }

    // Exemplo extra: vitórias em casa/fora por ano (se quiser usar na GUI)
    public List<Time> maisVitoriasMandantePorAno(int ano) {
        Map<String, Time> times = gerarTimes(ano);
        return times.values().stream()
                .sorted(Comparator.comparingInt(Time::getVitoriasMandante).reversed())
                .collect(Collectors.toList());
    }

    public List<Time> maisVitoriasVisitantePorAno(int ano) {
        Map<String, Time> times = gerarTimes(ano);
        return times.values().stream()
                .sorted(Comparator.comparingInt(Time::getVitoriasVisitante).reversed())
                .collect(Collectors.toList());
    }

    // --------- MELHOR ATAQUE GERAL ---------
    public List<Time> melhorAtaqueGeral(int top) {
        return gerarTimes().values().stream()
            .sorted(Comparator.comparingInt(Time::getGolsPro).reversed()) // maior gols marcados primeiro
            .limit(top)
            .toList();
    }

// --------- MELHOR ATAQUE POR ANO ---------
    public List<Time> melhorAtaquePorAno(int ano, int top) {
        Map<String, Time> times = gerarTimes(ano);
        return times.values().stream()
            .sorted(Comparator.comparingInt(Time::getGolsPro).reversed()) // maior gols marcados primeiro
            .limit(top)
            .toList();
    }

    // TOP N vitórias em casa (geral)
    public List<Time> topVitoriasMandante(int top) {
     return gerarTimes().values().stream()
            .sorted(Comparator.comparingInt(Time::getVitoriasMandante).reversed())
            .limit(top)
            .toList();
    }

    // TOP N vitórias fora (geral)
    public List<Time> topVitoriasVisitante(int top) {
     return gerarTimes().values().stream()
            .sorted(Comparator.comparingInt(Time::getVitoriasVisitante).reversed())
            .limit(top)
            .toList();
    }

}
