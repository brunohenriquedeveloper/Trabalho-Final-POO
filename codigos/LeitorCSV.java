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
            String linha = br.readLine(); // pula cabeçalho

            while ((linha = br.readLine()) != null) {
                if (linha.isBlank()) continue;

                // Divide por vírgula (CSV real)
                String[] dados = linha.split(",", -1);

                // Garante que a linha tem todas as colunas necessárias
                if (dados.length < 16) continue;

                // Remove aspas e espaços de todas as colunas
                for (int i = 0; i < dados.length; i++) {
                    dados[i] = dados[i].replace("\"", "").trim();
                }

                // Extrai informações da linha
                String mandante = dados[4];
                String visitante = dados[5];
                String estadio = dados[11];
                String data = dados[2];
                String hora = dados[3];
                String vencedor = dados[10];
                int placarMandante = Integer.parseInt(dados[12]);
                int placarVisitante = Integer.parseInt(dados[13]);
                String estadoMandante = dados[14];
                String estadoVisitante = dados[15];

                // Cria objeto Partida
                Partida p = new Partida(
                        mandante, visitante, estadio, data, hora,
                        vencedor, placarMandante, placarVisitante
                );
                partidas.add(p);

                // Atualiza mapa de times
                times.putIfAbsent(mandante, new Time(mandante, "", estadoMandante));
                times.putIfAbsent(visitante, new Time(visitante, "", estadoVisitante));

                // Atualiza estatísticas de cada time
                times.get(mandante).atualizarEstatisticas(p);
                times.get(visitante).atualizarEstatisticas(p);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return partidas;
    }
}
