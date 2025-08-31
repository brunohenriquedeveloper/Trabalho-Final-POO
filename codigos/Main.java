package codigos;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        String caminhoCsv = "dados\\campeonato-brasileiro-full.csv"; // ajuste se necessário
        Map<String, Time> times = new LinkedHashMap<>();

        // Aqui você usa o método estático do seu leitor que não vai mudar
        List<Partida> partidas = LeitorCsv.carregarPartidas(caminhoCsv, times);

        // Cria o campeonato
        Campeonato camp = new Campeonato(partidas, times);

        // Teste simples: imprime resumo de cada time
        for (Time t : times.values()) {
            System.out.println(t);
        }
    }
}
