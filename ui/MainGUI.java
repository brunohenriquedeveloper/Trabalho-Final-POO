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

        // ----------------- topo -----------------
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

        // ----------------- painel gráfico -----------------
        painelGrafico = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        add(painelGrafico, BorderLayout.CENTER);

        // ----------------- listeners -----------------
        btnGols.addActionListener(e -> desenharGrafico(campeonato.getClassificacaoPorGols(), "golsPro"));
        btnPontos.addActionListener(e -> desenharGraficoHorizontal(campeonato.getClassificacaoPorPontos(), "pontos"));
        btnSaldo.addActionListener(e -> desenharGraficoLinhas(campeonato.getClassificacaoPorSaldoGols(), "saldoGols"));

        btnMelhorDefesa.addActionListener(e -> {
            try {
                int ano = Integer.parseInt(campoAno.getText().trim());
                List<Time> lista = analise.melhoresDefesasPorAno(ano, 5);
                if (!lista.isEmpty()) {
                    desenharGraficoLinhas(lista, "defesa");
                } else {
                    JOptionPane.showMessageDialog(this, "Nenhuma partida encontrada para o ano " + ano);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Digite um ano válido!");
            }
        });

        btnMelhorAtaque.addActionListener(e -> {
            try {
                int ano = Integer.parseInt(campoAno.getText().trim());
                List<Time> lista = analise.melhorAtaquePorAno(ano, 5);
                if (!lista.isEmpty()) {
                    desenharGraficoPizza(lista, "ataque");
                } else {
                    JOptionPane.showMessageDialog(this, "Nenhuma partida encontrada para o ano " + ano);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Digite um ano válido!");
            }
        });

        btnVitoriasMandante.addActionListener(e ->
                desenharGrafico(campeonato.getClassificacaoPorVitoriasMandante(), "vitoriasMandante"));

        btnVitoriasVisitante.addActionListener(e ->
                desenharGraficoHorizontal(campeonato.getClassificacaoPorVitoriasVisitante(), "vitoriasVisitante"));

        setVisible(true);
    }

    // ----------------- métodos auxiliares -----------------
    private int pegarValor(Time t, String tipo) {
        return switch (tipo) {
            case "golsPro", "ataque" -> t.getGolsPro();
            case "pontos" -> t.getPontuacao();
            case "vitoriasMandante" -> t.getVitoriasMandante();
            case "vitoriasVisitante" -> t.getVitoriasVisitante();
            case "defesa" -> t.getGolsContra();
            case "saldoGols" -> t.getSaldoGols(); // corrigido aqui
            default -> 0;
        };
    }

    private Color pegarCor(String tipo) {
        return switch (tipo) {
            case "golsPro", "ataque" -> new Color(52, 152, 219);
            case "pontos" -> new Color(46, 204, 113);
            case "vitoriasMandante" -> new Color(230, 126, 34);
            case "vitoriasVisitante" -> new Color(231, 76, 60);
            case "defesa" -> new Color(155, 89, 182);
            case "saldoGols" -> new Color(241, 196, 15);
            default -> Color.GRAY;
        };
    }

    private void desenharGrafico(List<Time> lista, String tipo) {
        Graphics2D g2 = (Graphics2D) painelGrafico.getGraphics();
        g2.clearRect(0, 0, painelGrafico.getWidth(), painelGrafico.getHeight());

        int largura = painelGrafico.getWidth();
        int altura = painelGrafico.getHeight();
        int margem = 50;
        int numBarras = Math.min(lista.size(), 20);
        int larguraBarra = (largura - 2 * margem) / numBarras;

        Color corBarra = pegarCor(tipo);
        int max = lista.stream().mapToInt(t -> pegarValor(t, tipo)).max().orElse(1);

        for (int i = 0; i < numBarras; i++) {
            Time t = lista.get(i);
            int valor = pegarValor(t, tipo);
            int alturaBarra = (int) ((double) valor / max * (altura - 2 * margem));
            int x = margem + i * larguraBarra;
            int y = altura - margem - alturaBarra;

            g2.setColor(corBarra);
            g2.fillRect(x, y, larguraBarra - 5, alturaBarra);
            g2.setColor(Color.BLACK);
            g2.drawRect(x, y, larguraBarra - 5, alturaBarra);
            g2.drawString(String.valueOf(valor), x, y - 5);

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
    }

    private void desenharGraficoHorizontal(List<Time> lista, String tipo) {
        Graphics2D g2 = (Graphics2D) painelGrafico.getGraphics();
        g2.clearRect(0, 0, painelGrafico.getWidth(), painelGrafico.getHeight());

        int largura = painelGrafico.getWidth();
        int altura = painelGrafico.getHeight();
        int margem = 50;
        int numBarras = Math.min(lista.size(), 20);
        int alturaBarra = (altura - 2 * margem) / numBarras;

        Color corBarra = pegarCor(tipo);
        int max = lista.stream().mapToInt(t -> pegarValor(t, tipo)).max().orElse(1);

        for (int i = 0; i < numBarras; i++) {
            Time t = lista.get(i);
            int valor = pegarValor(t, tipo);
            int larguraBarra = (int) ((double) valor / max * (largura - 2 * margem));
            int x = margem;
            int y = margem + i * alturaBarra;

            g2.setColor(corBarra);
            g2.fillRect(x, y, larguraBarra, alturaBarra - 5);
            g2.setColor(Color.BLACK);
            g2.drawRect(x, y, larguraBarra, alturaBarra - 5);
            g2.drawString(t.getNome() + " (" + valor + ")", x + larguraBarra + 5, y + alturaBarra - 10);
        }
    }

    private void desenharGraficoLinhas(List<Time> lista, String tipo) {
    Graphics2D g2 = (Graphics2D) painelGrafico.getGraphics();
    int w = painelGrafico.getWidth();
    int h = painelGrafico.getHeight();
    int margin = 60; // espaço para labels e grid
    int offsetX = 10; // deslocamento dos pontos à direita do eixo Y

    // Anti-aliasing
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    // Fundo com gradiente moderno
    GradientPaint gp = new GradientPaint(0, 0, new Color(250, 250, 250),
                                         0, h, new Color(220, 220, 230));
    g2.setPaint(gp);
    g2.fillRect(0, 0, w, h);

    // Calcular limites
    int n = Math.min(lista.size(), 20);
    int rawMax = lista.stream().mapToInt(t -> pegarValor(t, tipo)).max().orElse(1);
    int rawMin = lista.stream().mapToInt(t -> pegarValor(t, tipo)).min().orElse(0);
    if ("saldoGols".equals(tipo)) {
        int bound = Math.max(Math.abs(rawMin), Math.abs(rawMax));
        rawMax = bound;
        rawMin = -bound;
    }
    int range = rawMax - rawMin;

    // Grid tracejada leve
    g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{6,6},0));
    g2.setColor(new Color(200, 200, 200, 120));
    for (int i = 0; i <= 5; i++) {
        int y = h - margin - i * (h - 2*margin)/5;
        g2.drawLine(margin, y, w - margin, y);
    }

    // Eixo Y mais light
    g2.setColor(new Color(100, 100, 100, 120)); // cinza claro e semi-transparente
    g2.setStroke(new BasicStroke(1.2f));        // linha fina
    g2.drawLine(margin, margin, margin, h - margin);

    // Labels do eixo Y
    g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
    g2.setColor(Color.DARK_GRAY);
    for (int i = 0; i <= 5; i++) {
        int y = h - margin - i*(h - 2*margin)/5;
        int valor = rawMin + i*range/5;
        String txt = String.valueOf(valor);
        int tw = g2.getFontMetrics().stringWidth(txt);
        g2.drawString(txt, margin - tw - 8, y + 4);
    }

    // Traçar linha e pontos
    int stepX = (w - 2*margin)/(n - 1);
    g2.setColor(pegarCor(tipo));
    g2.setStroke(new BasicStroke(2f));

    int prevX = margin + offsetX;
    int prevY = h - margin - (int)((double)(pegarValor(lista.get(0), tipo) - rawMin)/(range)*(h - 2*margin));

    for (int i = 0; i < n; i++) {
        Time t = lista.get(i);
        int val = pegarValor(t, tipo);
        int x = margin + i * stepX + offsetX; // deslocamento aplicado
        int y = h - margin - (int)((double)(val - rawMin)/(range)*(h - 2*margin));

        // Linha de ligação
        if (i > 0) {
            g2.drawLine(prevX, prevY, x, y);
        }
        prevX = x;
        prevY = y;

        // Ponto com borda e preenchimento
        g2.setColor(pegarCor(tipo));
        g2.fillOval(x-5, y-5, 10, 10);
        g2.setColor(pegarCor(tipo).darker());
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawOval(x-5, y-5, 10, 10);

        // Valor acima do ponto
        String labVal = String.valueOf(val);
        int sw = g2.getFontMetrics().stringWidth(labVal);
        g2.setColor(Color.DARK_GRAY);
        g2.drawString(labVal, x - sw/2, y - 12);

        // Nome do time
        String nome = t.getNome();
        int namW = g2.getFontMetrics().stringWidth(nome);
        g2.drawString(nome, x - namW/2, h - margin + 20);
    }
}

    private void desenharGraficoPizza(List<Time> lista, String tipo) {
        Graphics2D g2 = (Graphics2D) painelGrafico.getGraphics();
        g2.clearRect(0, 0, painelGrafico.getWidth(), painelGrafico.getHeight());

        int largura = painelGrafico.getWidth();
        int altura = painelGrafico.getHeight();
        int raio = Math.min(largura, altura) / 3;

        int total = lista.stream().mapToInt(t -> pegarValor(t, tipo)).sum();
        int startAngle = 0;
        int centroX = largura / 3;
        int centroY = altura / 2;

        Color[] cores = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.CYAN, Color.PINK, Color.YELLOW};

        for (int i = 0; i < lista.size(); i++) {
            Time t = lista.get(i);
            int valor = pegarValor(t, tipo);
            int angle = (int) Math.round(360.0 * valor / total);
            g2.setColor(cores[i % cores.length]);
            g2.fillArc(centroX - raio, centroY - raio, 2 * raio, 2 * raio, startAngle, angle);
            g2.setColor(Color.BLACK);
            g2.drawArc(centroX - raio, centroY - raio, 2 * raio, 2 * raio, startAngle, angle);

            int legendaX = 2 * largura / 3;
            int legendaY = 50 + i * 30;
            g2.setColor(cores[i % cores.length]);
            g2.fillRect(legendaX, legendaY - 10, 20, 20);
            g2.setColor(Color.BLACK);
            double perc = (valor * 100.0) / total;
            g2.drawString(t.getNome() + " (" + valor + " gols | " + String.format("%.1f", perc) + "%)", legendaX + 25, legendaY + 5);

            startAngle += angle;
        }
    }

    
}
