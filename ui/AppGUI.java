package ui;

import codigos.*;

import javax.swing.*;
import java.util.*;

public class AppGUI {
    public static void main(String[] args) {
        String caminhoCsv = "dados/campeonato-brasileiro-full.csv"; // ajuste se necessário

        // Pega o mapa de times e lista de partidas diretamente do leitor estático
        Map<String, Time> times = new LinkedHashMap<>();
        List<Partida> partidas = LeitorCsv.carregarPartidas(caminhoCsv, times);

        // Cria o campeonato
        Campeonato camp = new Campeonato(partidas, times);

        // Inicializa GUI
        SwingUtilities.invokeLater(() -> new MainGUI(camp));
    }
}
