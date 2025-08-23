package ui;

import codigos.Campeonato;
import codigos.Time;
import codigos.AnaliseEstatistica;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainGUI extends JFrame {

    private final Campeonato campeonato;
    private final AnaliseEstatistica analise;
    private final JPanel painelGrafico;
    private final JTextField campoAno;

    public MainGUI(Campeonato campeonato) {
        super("Campeonato Brasileiro – Estatísticas");
        this.campeonato = campeonato;
        this.analise = new AnaliseEstatistica(campeonato);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Painel de botões no topo
        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnGols = new JButton("Ranking de Gols");
        JButton btnPontos = new JButton("Mais Pontos");
        JButton btnSaldo = new JButton("Saldo de Gols");
        JButton btnMelhorDefesa = new JButton("Melhor Defesa (ano)");
        JButton btnMelhorAtaque = new JButton("Melhor Ataque (ano)");
        JButton btnVitoriasMandante = new JButton("Top Vitórias Casa");
        JButton btnVitoriasVisitante = new JButton("Top Vitórias Fora");

        campoAno = new JTextField(6);
        topo.add(new JLabel("Ano:"));
        topo.add(campoAno);

        topo.add(btnGols);
        topo.add(btnPontos);
        topo.add(btnSaldo);
        topo.add(btnMelhorDefesa);
        topo.add(btnMelhorAtaque);
        topo.add(btnVitoriasMandante);
        topo.add(btnVitoriasVisitante);

        add(topo, BorderLayout.NORTH);

        // Painel central para gráficos
        painelGrafico = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        add(painelGrafico, BorderLayout.CENTER);

        // Botões gerais
        btnGols.addActionListener(e -> desenharGrafico(campeonato.getClassificacaoPorGols(), "golsPro"));
        btnPontos.addActionListener(e -> desenharGrafico(campeonato.getClassificacaoPorPontos(), "pontos"));
        btnSaldo.addActionListener(e -> desenharGrafico(campeonato.getClassificacaoPorSaldoGols(), "saldoGols"));

        // Melhor Defesa - TOP 5 por ano
        btnMelhorDefesa.addActionListener(e -> {
            try {
                int ano = Integer.parseInt(campoAno.getText().trim());
                List<Time> melhores = analise.melhoresDefesasPorAno(ano, 5);
                if (!melhores.isEmpty()) {
                    desenharGrafico(melhores, "defesa");
                } else {
                    JOptionPane.showMessageDialog(this, "Nenhuma partida encontrada para o ano " + ano);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Digite um ano válido!");
            }
        });

        // Melhor Ataque - TOP 5 por ano
        btnMelhorAtaque.addActionListener(e -> {
            try {
                int ano = Integer.parseInt(campoAno.getText().trim());
                List<Time> topAtaque = analise.melhorAtaquePorAno(ano, 5);
                if (!topAtaque.isEmpty()) {
                    desenharGrafico(topAtaque, "ataque");
                } else {
                    JOptionPane.showMessageDialog(this, "Nenhuma partida encontrada para o ano " + ano);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Digite um ano válido!");
            }
        });

        // Top 5 vitórias em casa - geral
        btnVitoriasMandante.addActionListener(e ->
                desenharGrafico(analise.topVitoriasMandante(5), "vitoriasMandante"));

        // Top 5 vitórias fora - geral
        btnVitoriasVisitante.addActionListener(e ->
                desenharGrafico(analise.topVitoriasVisitante(5), "vitoriasVisitante"));

        setVisible(true);
    }

   private void desenharGrafico(List<Time> lista, String tipo) {
    Graphics2D g2 = (Graphics2D) painelGrafico.getGraphics();
    g2.clearRect(0, 0, painelGrafico.getWidth(), painelGrafico.getHeight());

    int largura = painelGrafico.getWidth();
    int altura = painelGrafico.getHeight();
    int margem = 50;
    int numBarras = Math.min(lista.size(), 20);
    int larguraBarra = (largura - 2 * margem) / numBarras;

    if (tipo.equals("saldoGols")) {
        int max = lista.stream().mapToInt(Time::getSaldoGols).max().orElse(1);
        int min = lista.stream().mapToInt(Time::getSaldoGols).min().orElse(0);
        if (max == min) max++; // evita divisão por zero
        int zeroY = altura - margem - (int)((0 - min) / (double)(max - min) * (altura - 2 * margem));

        for (int i = 0; i < numBarras; i++) {
            Time t = lista.get(i);
            int valor = t.getSaldoGols();
            int barraAltura = (int)(Math.abs(valor) / (double)(max - min) * (altura - 2 * margem));
            int x = margem + i * larguraBarra;
            int y = valor >= 0 ? zeroY - barraAltura : zeroY;

            // Cor da barra
            g2.setColor(valor >= 0 ? new Color(241, 196, 15) : Color.RED);
            g2.fillRect(x, y, larguraBarra - 5, barraAltura);
            g2.setColor(Color.BLACK);
            g2.drawRect(x, y, larguraBarra - 5, barraAltura);

            // Valor da barra
            g2.drawString(String.valueOf(valor), x, valor >= 0 ? y - 5 : y + barraAltura + 15);

            // Nome do time - sempre logo abaixo da barra, com pequena margem
            String nome = t.getNome();
            int yNome = valor >= 0 ? zeroY + 20 : zeroY + barraAltura + 32; // 5px de margem
            int larguraTexto = g2.getFontMetrics().stringWidth(nome);
            if (larguraBarra < larguraTexto) {
                g2.rotate(-Math.PI / 4, x + larguraBarra / 2, yNome);
                g2.drawString(nome, x + larguraBarra / 4, yNome);
                g2.rotate(Math.PI / 4, x + larguraBarra / 2, yNome);
            } else {
                g2.drawString(nome, x, yNome);
            }
        }

        // Linha zero
        g2.setColor(Color.DARK_GRAY);
        g2.drawLine(margem, zeroY, largura - margem, zeroY);

    } else {
        // Mantém o estilo original para os outros gráficos
        Color corBarra = switch (tipo) {
            case "golsPro", "ataque" -> new Color(52, 152, 219); // Azul
            case "pontos" -> new Color(46, 204, 113); // Verde
            case "vitoriasMandante" -> new Color(230, 126, 34); // Laranja
            case "vitoriasVisitante" -> new Color(231, 76, 60); // Vermelho
            case "defesa" -> new Color(155, 89, 182); // Roxo
            default -> Color.GRAY;
        };

        int max = lista.stream().mapToInt(t -> switch (tipo) {
            case "golsPro", "ataque" -> t.getGolsPro();
            case "pontos" -> t.getPontuacao();
            case "vitoriasMandante" -> t.getVitoriasMandante();
            case "vitoriasVisitante" -> t.getVitoriasVisitante();
            case "defesa" -> t.getGolsContra();
            default -> 0;
        }).max().orElse(1);

        for (int i = 0; i < numBarras; i++) {
            Time t = lista.get(i);
            int valor = switch (tipo) {
                case "golsPro", "ataque" -> t.getGolsPro();
                case "pontos" -> t.getPontuacao();
                case "vitoriasMandante" -> t.getVitoriasMandante();
                case "vitoriasVisitante" -> t.getVitoriasVisitante();
                case "defesa" -> t.getGolsContra();
                default -> 0;
            };

            int alturaBarra = (int) ((double) valor / max * (altura - 2 * margem));
            int x = margem + i * larguraBarra;
            int y = altura - margem - alturaBarra;

            g2.setColor(corBarra);
            g2.fillRect(x, y, larguraBarra - 5, alturaBarra);
            g2.setColor(Color.BLACK);
            g2.drawRect(x, y, larguraBarra - 5, alturaBarra);
            g2.drawString(String.valueOf(valor), x, y - 5);

            // Nome do time
            String nome = t.getNome();
            int larguraTexto = g2.getFontMetrics().stringWidth(nome);
            int yNome = altura - margem + 15;
            if (larguraBarra < larguraTexto) {
                g2.rotate(-Math.PI / 4, x + larguraBarra / 2, yNome);
                g2.drawString(nome, x + larguraBarra / 4, yNome);
                g2.rotate(Math.PI / 4, x + larguraBarra / 2, yNome);
            } else {
                g2.drawString(nome, x, yNome);
            }
        }

        // Linhas de referência
        g2.setColor(Color.DARK_GRAY);
        for (int i = 1; i <= 3; i++) {
            int yLinha = altura - margem - (int) ((altura - 2 * margem) * i / 4.0);
            g2.drawLine(margem, yLinha, largura - margem, yLinha);
            g2.drawString(String.valueOf(max * i / 4), 5, yLinha + 5);
        }
    }
}

}
