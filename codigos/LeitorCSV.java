package codigos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LeitorCsv {

    public static List<Partida> caminhoCsv;

    public static List<Partida> carregarPartidas(String caminho, Map<String, Time> times) {
        List<Partida> partidas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminho, StandardCharsets.UTF_8))) {
            String linha = br.readLine();

            while ((linha = br.readLine()) != null) {
                if (linha.isBlank()) continue;

                String[] dados = linha.split(",", -1); 
                if (dados.length < 16) continue;

                for (int i = 0; i < dados.length; i++) {
                    dados[i] = dados[i].replace("\"", "").trim();
                }

                int id = Integer.parseInt(dados[0]); 
                String mandante = dados[4];
                String visitante = dados[5];
                String estadio = dados[11];
                String data = dados[2];
                String hora = dados[3];
                String vencedor = "-".equals(dados[10]) ? "Empate" : dados[10];
                int placarMandante = Integer.parseInt(dados[12]);
                int placarVisitante = Integer.parseInt(dados[13]);
                String estadoMandante = dados[14];
                String estadoVisitante = dados[15];

                Partida p = new Partida(
                        id,
                        mandante,
                        visitante,
                        estadio,
                        data,
                        hora,
                        vencedor,
                        placarMandante,
                        placarVisitante
                );
                partidas.add(p);

                times.putIfAbsent(mandante, new Time(mandante, "", estadoMandante));
                times.putIfAbsent(visitante, new Time(visitante, "", estadoVisitante));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return partidas;
    }
}
