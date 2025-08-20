package codigos;
import java.util.*;
import java.util.stream.Collectors;

public class Campeonato {
    private final List<Partida> partidas = new ArrayList<>();

    public void adicionarPartida(Partida partida) {
        partidas.add(partida);
    }

    public List<Partida> getPartidas() {
        return partidas;
    }

    public Map<String, Long> contarVitoriasPorTime() {
        return partidas.stream()
            .map(Partida::getVencedor) 
            .filter(v -> !v.equals("Empate"))
            .collect(Collectors.groupingBy(v -> v, Collectors.counting()));
    }

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