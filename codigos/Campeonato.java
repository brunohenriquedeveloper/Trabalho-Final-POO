package codigos;

import java.util.*;
import java.util.stream.Collectors;

public class Campeonato {

    private final List<Partida> partidas = new ArrayList<>();
    private final Map<String, Time> times = new HashMap<>();

    // --------- Construtor ---------
    public Campeonato(
            List<Partida> partidas,
            Map<String, Time> times
    ) {
        this.partidas.addAll(partidas);
        this.times.putAll(times);

        // Atualiza as estatísticas de cada time baseado nas partidas
        for (Partida p : partidas) {
            Time mandante = this.times.get(p.getMandante());
            if (mandante != null) mandante.atualizarEstatisticas(p);

            Time visitante = this.times.get(p.getVisitante());
            if (visitante != null) visitante.atualizarEstatisticas(p);
        }
    }

    // --------- Métodos ---------
    public void adicionarPartida(Partida partida) {
        partidas.add(partida);

        Time mandante = times.get(partida.getMandante());
        if (mandante != null) mandante.atualizarEstatisticas(partida);

        Time visitante = times.get(partida.getVisitante());
        if (visitante != null) visitante.atualizarEstatisticas(partida);
    }

    public List<Partida> getPartidas() {
        return partidas;
    }

    public Map<String, Time> getTimes() {
        return times;
    }

    // --------- Classificações ---------
    public List<Time> getClassificacaoPorGols() {
        return times.values().stream()
                .sorted((a, b) -> b.getGolsPro() - a.getGolsPro())
                .collect(Collectors.toList());
    }

    public List<Time> getClassificacaoPorPontos() {
        return times.values().stream()
                .sorted((a, b) -> b.getPontuacao() - a.getPontuacao())
                .collect(Collectors.toList());
    }

    public List<Time> getClassificacaoPorSaldoGols() {
        return times.values().stream()
                .sorted((a, b) -> b.getSaldoGols() - a.getSaldoGols())
                .collect(Collectors.toList());
    }

    public List<Time> getClassificacaoPorVitoriasMandante() {
        return times.values().stream()
                .sorted((a, b) -> b.getVitoriasMandante() - a.getVitoriasMandante())
                .collect(Collectors.toList());
    }

    public List<Time> getClassificacaoPorVitoriasVisitante() {
        return times.values().stream()
                .sorted((a, b) -> b.getVitoriasVisitante() - a.getVitoriasVisitante())
                .collect(Collectors.toList());
    }

    // --------- Filtros ---------
    public List<Partida> getPartidasPorAno(int ano) {
        return partidas.stream()
                .filter(p -> {
                    String[] partes = p.getData().split("/");
                    int anoPartida = Integer.parseInt(partes[2]);
                    return anoPartida == ano;
                })
                .collect(Collectors.toList());
    }
}
