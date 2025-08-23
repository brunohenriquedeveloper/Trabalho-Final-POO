package ui;

import codigos.Campeonato;
import codigos.Time;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainGUI extends JFrame {

    private final Campeonato campeonato;
    private final JPanel painelGrafico;

    public MainGUI(Campeonato campeonato) {
        super("Campeonato Brasileiro – Estatísticas");
        this.campeonato = campeonato;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Painel de botões no topo
        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnGols = new JButton("Ranking de Gols");
        JButton btnPontos = new JButton("Mais Pontos");
        JButton btnSaldo = new JButton("Saldo de Gols");
        topo.add(btnGols);
        topo.add(btnPontos);
        topo.add(btnSaldo);

        add(topo, BorderLayout.NORTH);

        // Painel central para desenhar gráficos
        painelGrafico = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(0,0,getWidth(),getHeight());
            }
        };
        add(painelGrafico, BorderLayout.CENTER);

        // Ações dos botões
        btnGols.addActionListener(e -> desenharGrafico(campeonato.getClassificacaoPorGols(), "golsPro"));
        btnPontos.addActionListener(e -> desenharGrafico(campeonato.getClassificacaoPorPontos(), "pontos"));
        btnSaldo.addActionListener(e -> desenharGrafico(campeonato.getClassificacaoPorSaldoGols(), "saldoGols"));


        setVisible(true);
    }

    private void desenharGrafico(List<Time> lista, String tipo) {
        Graphics g = painelGrafico.getGraphics();
        g.clearRect(0,0,painelGrafico.getWidth(), painelGrafico.getHeight());

        int largura = painelGrafico.getWidth();
        int altura = painelGrafico.getHeight();

        int max = 0;
        for (Time t : lista) {
            switch (tipo) {
                case "golsPro": if (t.getGolsPro() > max) max = t.getGolsPro(); break;
                case "pontos": if (t.getPontuacao() > max) max = t.getPontuacao(); break;
                case "saldoGols": if (t.getSaldoGols() > max) max = t.getSaldoGols(); break;
            }
        }

        int margem = 50;
        int numBarras = Math.min(lista.size(), 20); // mostra só top 20
        int larguraBarra = (largura - 2*margem) / numBarras;

        for (int i = 0; i < numBarras; i++) {
            Time t = lista.get(i);
            int valor = 0;
            switch (tipo) {
                case "golsPro": valor = t.getGolsPro(); break;
                case "pontos": valor = t.getPontuacao(); break;
                case "saldoGols": valor = t.getSaldoGols(); break;
            }
            int alturaBarra = (int)((double)valor / max * (altura - 2*margem));
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
