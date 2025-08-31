package codigos;

import java.util.*;
import java.util.stream.Collectors;

public class CalculoEstatistica extends AnaliseEstatistica {

    private final Map<String, Time> timesCache;

    public CalculoEstatistica(Campeonato campeonato) {
        super(campeonato);
        this.timesCache = inicializarTimes(campeonato.getPartidas());
    }

    // ----------- Métodos públicos -----------

    @Override
    public List<Time> rankingPorPontos() {
        return timesCache.values().stream()
                .sorted(Comparator.comparingInt(Time::getPontuacao).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Time> melhoresDefesasPorAno(int ano, int top) {
        return gerarTimesPorAno(ano).values().stream()
                .sorted(Comparator.comparingInt(Time::getGolsContra))
                .limit(top)
                .collect(Collectors.toList());
    }

    @Override
    public List<Time> melhorAtaquePorAno(int ano, int top) {
        return gerarTimesPorAno(ano).values().stream()
                .sorted(Comparator.comparingInt(Time::getGolsPro).reversed())
                .limit(top)
                .collect(Collectors.toList());
    }

    @Override
    public List<Time> topVitoriasMandante(int top) {
        return timesCache.values().stream()
                .sorted(Comparator.comparingInt(Time::getVitoriasMandante).reversed())
                .limit(top)
                .collect(Collectors.toList());
    }

    @Override
    public List<Time> topVitoriasVisitante(int top) {
        return timesCache.values().stream()
                .sorted(Comparator.comparingInt(Time::getVitoriasVisitante).reversed())
                .limit(top)
                .collect(Collectors.toList());
    }

    // ----------- Auxiliares -----------

    /** Cria todos os times e atualiza estatísticas uma única vez */
    private Map<String, Time> inicializarTimes(List<Partida> partidas) {
        Map<String, Time> times = new LinkedHashMap<>();
        Set<Integer> partidasProcessadas = new HashSet<>();

        // Primeiro, criar todos os times
        for (Partida p : partidas) {
            times.putIfAbsent(p.getMandante(), new Time(p.getMandante(), "", ""));
            times.putIfAbsent(p.getVisitante(), new Time(p.getVisitante(), "", ""));
        }

        // Depois, processar as partidas sem duplicatas
        for (Partida p : partidas) {
            if (partidasProcessadas.contains(p.getId())) {
                continue;
            }
            partidasProcessadas.add(p.getId());

            // Atualizar estatísticas manualmente para evitar duplicação
            Time mandante = times.get(p.getMandante());
            Time visitante = times.get(p.getVisitante());
            
            atualizarEstatisticasTime(mandante, p, true);
            atualizarEstatisticasTime(visitante, p, false);
        }
        
        return times;
    }

    /** Gera estatísticas filtradas por ano, criando novas instâncias temporárias */
    private Map<String, Time> gerarTimesPorAno(int ano) {
        Map<String, Time> timesAno = new LinkedHashMap<>();
        List<Partida> partidasAno = campeonato.getPartidasPorAno(ano);
        Set<Integer> partidasProcessadas = new HashSet<>();

        // Primeiro, criar todos os times
        for (Partida p : partidasAno) {
            timesAno.putIfAbsent(p.getMandante(), new Time(p.getMandante(), "", ""));
            timesAno.putIfAbsent(p.getVisitante(), new Time(p.getVisitante(), "", ""));
        }

        // Depois, processar as partidas sem duplicatas
        for (Partida p : partidasAno) {
            if (partidasProcessadas.contains(p.getId())) {
                continue;
            }
            partidasProcessadas.add(p.getId());

            // Atualizar estatísticas manualmente para evitar duplicação
            Time mandante = timesAno.get(p.getMandante());
            Time visitante = timesAno.get(p.getVisitante());
            
            atualizarEstatisticasTime(mandante, p, true);
            atualizarEstatisticasTime(visitante, p, false);
        }
        
        return timesAno;
    }

    /** Atualiza estatísticas do time manualmente */
    private void atualizarEstatisticasTime(Time time, Partida p, boolean ehMandante) {
        int golsPro = ehMandante ? p.getPlacarMandante() : p.getPlacarVisitante();
        int golsContra = ehMandante ? p.getPlacarVisitante() : p.getPlacarMandante();
        
        // Atualizar gols
        time.setGolsPro(time.getGolsPro() + golsPro);
        time.setGolsContra(time.getGolsContra() + golsContra);
        
        // Determinar resultado
        String nomeTime = time.getNome();
        boolean venceu = p.getVencedor().equals(nomeTime);
        boolean empate = p.getVencedor().equals("Empate");
        
        // Atualizar vitórias/empates/derrotas
        if (venceu) {
            time.setVitorias(time.getVitorias() + 1);
            if (ehMandante) {
                time.setVitoriasMandante(time.getVitoriasMandante() + 1);
            } else {
                time.setVitoriasVisitante(time.getVitoriasVisitante() + 1);
            }
        } else if (empate) {
            time.setEmpates(time.getEmpates() + 1);
        } else {
            time.setDerrotas(time.getDerrotas() + 1);
        }
        
        // Atualizar partidas jogadas
        time.setPartidasJogadas(time.getPartidasJogadas() + 1);
    }
}