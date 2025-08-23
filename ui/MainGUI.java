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
        Graphics g = painelGrafico.getGraphics();
        g.clearRect(0, 0, painelGrafico.getWidth(), painelGrafico.getHeight());

        int largura = painelGrafico.getWidth();
        int altura = painelGrafico.getHeight();

        int max = 0;
        for (Time t : lista) {
            switch (tipo) {
                case "golsPro": if (t.getGolsPro() > max) max = t.getGolsPro(); break;
                case "pontos": if (t.getPontuacao() > max) max = t.getPontuacao(); break;
                case "saldoGols": if (t.getSaldoGols() > max) max = t.getSaldoGols(); break;
                case "vitoriasMandante": if (t.getVitoriasMandante() > max) max = t.getVitoriasMandante(); break;
                case "vitoriasVisitante": if (t.getVitoriasVisitante() > max) max = t.getVitoriasVisitante(); break;
                case "defesa": if (t.getGolsContra() > max) max = t.getGolsContra(); break;
                case "ataque": if (t.getGolsPro() > max) max = t.getGolsPro(); break;
            }
        }
        if (max == 0) max = 1;

        int margem = 50;
        int numBarras = Math.min(lista.size(), 20);
        int larguraBarra = (largura - 2 * margem) / numBarras;

        for (int i = 0; i < numBarras; i++) {
            Time t = lista.get(i);
            int valor = switch (tipo) {
                case "golsPro" -> t.getGolsPro();
                case "pontos" -> t.getPontuacao();
                case "saldoGols" -> t.getSaldoGols();
                case "vitoriasMandante" -> t.getVitoriasMandante();
                case "vitoriasVisitante" -> t.getVitoriasVisitante();
                case "defesa" -> t.getGolsContra();
                case "ataque" -> t.getGolsPro();
                default -> 0;
            };
            int alturaBarra = (int) ((double) valor / max * (altura - 2 * margem));
            int x = margem + i * larguraBarra;
            int y = altura - margem - alturaBarra;

            g.setColor(Color.BLUE);
            g.fillRect(x, y, larguraBarra - 5, alturaBarra);
            g.setColor(Color.BLACK);
            g.drawString(t.getNome(), x, altura - margem + 15);
            g.drawString(String.valueOf(valor), x, y - 5);
        }
    }
}
