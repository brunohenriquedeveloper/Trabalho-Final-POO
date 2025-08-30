package codigos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        // Se rodar a partir da raiz do projeto, use "dados\\...".
        // Se rodar a partir da pasta codigos, troque para "../dados\\...".
        String caminho = "dados\\campeonato-brasileiro-full.csv";

        Map<String, Time> times = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha = br.readLine(); // pula cabeçalho

            while ((linha = br.readLine()) != null) {
                if (linha.isBlank()) continue;

                String[] campos = parseCsvLine(linha);
                // Deve ter 16 colunas conforme seu cabeçalho
                if (campos.length < 16) {
                    System.out.println("Linha inválida/curta: " + linha);
                    continue;
                }

                // Mapeamento pelos índices do seu cabeçalho:
                // 0:ID, 1:rodata, 2:data, 3:hora, 4:mandante, 5:visitante,
                // 6:form_mand, 7:form_vis, 8:tec_mand, 9:tec_vis,
                // 10:vencedor, 11:arena, 12:mandante_Placar, 13:visitante_Placar,
                // 14:mandante_Estado, 15:visitante_Estado

                String data = campos[2];
                String hora = campos[3];
                String mandante = campos[4];
                String visitante = campos[5];
                String tecnicoMand = vazioParaNull(campos[8]);
                String tecnicoVis = vazioParaNull(campos[9]);
                String vencedorRaw = campos[10];
                String arena = limpaNbsp(campos[11]);
                int placarMandante = parseIntSeguro(campos[12], 0);
                int placarVisitante = parseIntSeguro(campos[13], 0);
                String ufMand = vazioParaNull(campos[14]);
                String ufVis = vazioParaNull(campos[15]);

                // Normaliza empates: no CSV vem "-" e sua classe espera "Empate"
                String vencedor = "-".equals(vencedorRaw) ? "Empate" : vencedorRaw;

                // Garante criação/armazenamento dos times com técnico/UF quando disponível
                times.putIfAbsent(mandante, new Time(mandante, tecnicoMand, ufMand));
                times.putIfAbsent(visitante, new Time(visitante, tecnicoVis, ufVis));

                // Cria a partida usando SEU construtor
                Partida p = new Partida(
                        mandante, visitante, arena, data, hora,
                        vencedor, placarMandante, placarVisitante
                );

                // Atualiza estatísticas de cada time (sua função usa o objeto Partida)
                times.get(mandante).atualizarEstatisticas(p);
                times.get(visitante).atualizarEstatisticas(p);
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Falha ao ler: " + caminho);
        }

        // Imprime um resumo simples
        for (Time t : times.values()) {
            System.out.println(t);
        }
    }

    // --------- Helpers ---------

    // Parser simples de CSV que respeita aspas (não quebra em vírgulas dentro de aspas)
    private static String[] parseCsvLine(String line) {
        StringBuilder sb = new StringBuilder();
        java.util.List<String> campos = new java.util.ArrayList<>();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes; // alterna estado
            } else if (c == ',' && !inQuotes) {
                campos.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        campos.add(sb.toString().trim());

        // Remove aspas externas e NBSP de cada campo
        for (int i = 0; i < campos.size(); i++) {
            String v = campos.get(i);
            if (v.length() >= 2 && v.startsWith("\"") && v.endsWith("\"")) {
                v = v.substring(1, v.length() - 1);
            }
            v = limpaNbsp(v).trim();
            campos.set(i, v);
        }
        return campos.toArray(new String[0]);
    }

    private static String vazioParaNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }

    private static String limpaNbsp(String s) {
        return s == null ? null : s.replace('\u00A0', ' ');
    }

    private static int parseIntSeguro(String s, int padrao) {
        try { return Integer.parseInt(s.trim()); }
        catch (Exception e) { return padrao; }
    }
}
