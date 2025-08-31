package codigos;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        String caminhoCsv = "dados\\campeonato-brasileiro-full.csv"; 
        Map<String, Time> times = new LinkedHashMap<>();


        List<Partida> partidas = LeitorCSV.carregarPartidas(caminhoCsv, times);


        Campeonato camp = new Campeonato(partidas, times);


        for (Time t : times.values()) {
            System.out.println(t);
        }
    }
}
