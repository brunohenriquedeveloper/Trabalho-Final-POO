package codigos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LeitorCSV {

    public static List<Partida> carregarPartidas(String caminho, Map<String, Time> times) {
        List<Partida> partidas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminho, StandardCharsets.UTF_8))) {
            String linha = br.readLine(); 
            while ((linha = br.readLine()) != null) {
                if (linha.isBlank()) continue;

                String[] dados = linha.split(";", -1);

                String mandante = dados[1].trim();
                String visitante = dados[2].trim();
                String estadio = dados[3].trim();
                String data = dados[4].trim();
                String hora = dados[5].trim();
                String vencedor = dados[6].trim();
                int placarMandante = Integer.parseInt(dados[7].trim());
                int placarVisitante = Integer.parseInt(dados[8].trim());

                Partida p = new Partida(
                        mandante, visitante, estadio, data, hora,
                        vencedor, placarMandante, placarVisitante
                );
                partidas.add(p);

                times.putIfAbsent(mandante, new Time(mandante, null, null));
                times.putIfAbsent(visitante, new Time(visitante, null, null));

                times.get(mandante).atualizarEstatisticas(p);
                times.get(visitante).atualizarEstatisticas(p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return partidas;
    }
}
