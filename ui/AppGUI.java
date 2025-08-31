package ui;

import codigos.*;

import javax.swing.*;
import java.util.*;

public class AppGUI {
    public static void main(String[] args) {
        String caminhoCsv = "dados/campeonato-brasileiro-full.csv";

        Map<String, Time> times = new LinkedHashMap<>();
        List<Partida> partidas = LeitorCSV.carregarPartidas(caminhoCsv, times);

        Campeonato camp = new Campeonato(partidas, times);

        SwingUtilities.invokeLater(() -> new MainGUI(camp));
    }
}
