package ui;

import codigos.*;

import javax.swing.*;
import java.awt.*;
import java.util.List; 

public class MainGUI extends JFrame {

    private final Campeonato campeonato;
    private final JPanel painelGrafico;
    private final JTextField campoAno;

    public MainGUI(Campeonato campeonato) {
        super("Campeonato Brasileiro – Estatísticas");
        this.campeonato = campeonato;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

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

        painelGrafico = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        add(painelGrafico, BorderLayout.CENTER);

        btnGols.addActionListener(e -> {
            try {
                List<Time> lista = new RankingPorGols(campeonato).analisar(0, 20);
                desenharGrafico(lista, "golsPro");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao gerar ranking de gols: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        btnPontos.addActionListener(e -> {
            try {
                List<Time> lista = new RankingPorPontos(campeonato).analisar(0, 20);
                desenharGraficoHorizontal(lista, "pontos");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao gerar ranking de pontos: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        btnSaldo.addActionListener(e -> {
            try {
                List<Time> lista = new RankingPorSaldoGols(campeonato).analisar(0, 20);
                desenharGraficoLinhas(lista, "saldoGols");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao gerar ranking de saldo de gols: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        btnMelhorDefesa.addActionListener(e -> {
            String texto = campoAno.getText().trim();
            if (texto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Digite um ano válido!");
                return;
            }
            try {
                int ano = Integer.parseInt(texto);
                List<Time> lista = new MelhoresDefesas(campeonato).analisar(ano, 5);
                if (!lista.isEmpty()) {
                    desenharGraficoLinhas(lista, "defesa");
                } else {
                    JOptionPane.showMessageDialog(this, "Nenhuma partida encontrada para o ano " + ano);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao gerar melhores defesas: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        btnMelhorAtaque.addActionListener(e -> {
            String texto = campoAno.getText().trim();
            if (texto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Digite um ano válido!");
                return;
            }
            try {
                int ano = Integer.parseInt(texto);
                List<Time> lista = new MelhoresAtaques(campeonato).analisar(ano, 5);
                if (!lista.isEmpty()) {
                    desenharGraficoPizza(lista, "ataque");
                } else {
                    JOptionPane.showMessageDialog(this, "Nenhuma partida encontrada para o ano " + ano);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao gerar melhores ataques: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        btnVitoriasMandante.addActionListener(e -> {
            try {
                List<Time> lista = new TopVitoriasMandante(campeonato).analisar(0, 10);
                desenharGrafico(lista, "vitoriasMandante");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao gerar top vitórias em casa: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        btnVitoriasVisitante.addActionListener(e -> {
            try {
                List<Time> lista = new TopVitoriasVisitante(campeonato).analisar(0, 10);
                desenharGraficoHorizontal(lista, "vitoriasVisitante");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao gerar top vitórias fora: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        setVisible(true);
    }


    private int pegarValor(Time t, String tipo) {
        return switch (tipo) {
            case "golsPro", "ataque" -> t.getGolsPro();
            case "pontos" -> t.getPontuacao();
            case "vitoriasMandante" -> t.getVitoriasMandante();
            case "vitoriasVisitante" -> t.getVitoriasVisitante();
            case "defesa" -> t.getGolsContra();
            case "saldoGols" -> t.getSaldoGols();
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

        if (lista == null || lista.isEmpty()) {
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("SansSerif", Font.BOLD, 16));
            g2.drawString("Sem dados para exibir.", 50, painelGrafico.getHeight()/2);
            return;
        }

        int largura = painelGrafico.getWidth();
        int altura = painelGrafico.getHeight();
        int margem = 50;
        int numBarras = Math.min(lista.size(), 20);
        if (numBarras == 0) return;

        int larguraBarra = Math.max(1, (largura - 2 * margem) / numBarras);
        Color corBarra = pegarCor(tipo);

        int max = lista.stream().mapToInt(t -> pegarValor(t, tipo)).max().orElse(0);
        if (max <= 0) {
            g2.setColor(Color.BLACK);
            g2.drawString("Todos os valores são zero.", 50, altura/2);
            return;
        }

        g2.setFont(new Font("SansSerif", Font.BOLD, 12));

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

            String valorStr = String.valueOf(valor);
            g2.setColor(Color.DARK_GRAY);
            g2.drawString(valorStr, x + (larguraBarra-5 - g2.getFontMetrics().stringWidth(valorStr))/2, y - 5);

            String nome = t.getNome();
            int xNome = x + (larguraBarra - 5) / 2;
            int yNome = y + alturaBarra/2;
            g2.rotate(-Math.PI/2, xNome, yNome);
            g2.setColor(Color.WHITE);
            g2.drawString(nome, xNome - g2.getFontMetrics().stringWidth(nome)/2, yNome);
            g2.rotate(Math.PI/2, xNome, yNome);
        }
    }

    private void desenharGraficoHorizontal(List<Time> lista, String tipo) {
        Graphics2D g2 = (Graphics2D) painelGrafico.getGraphics();
        g2.clearRect(0, 0, painelGrafico.getWidth(), painelGrafico.getHeight());

        if (lista == null || lista.isEmpty()) {
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("SansSerif", Font.BOLD, 16));
            g2.drawString("Sem dados para exibir.", 50, painelGrafico.getHeight()/2);
            return;
        }

        int largura = painelGrafico.getWidth();
        int altura = painelGrafico.getHeight();
        int margem = 50;
        int numBarras = Math.min(lista.size(), 20);
        if (numBarras == 0) return;

        int alturaBarra = Math.max(1, (altura - 2 * margem) / numBarras);
        Color corBarra = pegarCor(tipo);

        int max = lista.stream().mapToInt(t -> pegarValor(t, tipo)).max().orElse(0);
        if (max <= 0) {
            g2.setColor(Color.BLACK);
            g2.drawString("Todos os valores são zero.", 50, altura/2);
            return;
        }

        g2.setFont(new Font("SansSerif", Font.BOLD, 12));

        for (int i = 0; i < numBarras; i++) {
            Time t = lista.get(i);
            int valor = pegarValor(t, tipo);
            int larguraBarra = (int)((double) valor / max * (largura - 150));
            int y = margem + i * alturaBarra;

            g2.setColor(corBarra);
            g2.fillRect(margem, y, larguraBarra, alturaBarra-5);
            g2.setColor(Color.BLACK);
            g2.drawRect(margem, y, larguraBarra, alturaBarra-5);

            g2.setColor(Color.DARK_GRAY);
            g2.drawString(t.getNome() + " ("+valor+")", margem + larguraBarra + 5, y + alturaBarra - 10);
        }
    }

    private void desenharGraficoLinhas(List<Time> lista, String tipo) {
        Graphics2D g2 = (Graphics2D) painelGrafico.getGraphics();
        int w = painelGrafico.getWidth();
        int h = painelGrafico.getHeight();
        int margin = 60;

        g2.clearRect(0, 0, w, h);

        if (lista == null || lista.isEmpty()) {
            g2.setColor(Color.BLACK);
            g2.drawString("Sem dados para exibir.", 50, h/2);
            return;
        }

        int n = Math.min(lista.size(), 20);
        if (n < 2) {
            g2.setColor(Color.BLACK);
            g2.drawString("Dados insuficientes para gráfico de linhas.", 50, h/2);
            return;
        }

        int rawMax = lista.stream().mapToInt(t -> pegarValor(t, tipo)).max().orElse(0);
        int rawMin = lista.stream().mapToInt(t -> pegarValor(t, tipo)).min().orElse(0);
        if ("saldoGols".equals(tipo)) {
            int bound = Math.max(Math.abs(rawMin), Math.abs(rawMax));
            rawMax = bound; rawMin = -bound;
        }
        int range = rawMax - rawMin;
        if (range == 0) range = 1;

        int stepX = (w - 2*margin)/(n-1);
        g2.setColor(pegarCor(tipo));
        g2.setStroke(new BasicStroke(2f));

        int prevX = margin;
        int prevY = h - margin - (int)((double)(pegarValor(lista.get(0),tipo)-rawMin)/range*(h-2*margin));

        for (int i = 0; i < n; i++) {
            Time t = lista.get(i);
            int val = pegarValor(t, tipo);
            int x = margin + i * stepX;
            int y = h - margin - (int)((double)(val-rawMin)/range*(h-2*margin));

            if (i > 0) g2.drawLine(prevX, prevY, x, y);

            prevX = x; prevY = y;

            g2.fillOval(x-4,y-4,8,8);
            g2.drawString(String.valueOf(val), x-5,y-10);

            String nome = t.getNome();
            g2.setColor(Color.BLACK); 

            g2.rotate(-Math.PI / 2, x, h - margin + 20);
            g2.drawString(nome, x, h - margin + 20);
            g2.rotate(Math.PI / 2, x, h - margin + 20);

        }
    }

    private void desenharGraficoPizza(List<Time> lista, String tipo) {
        Graphics2D g2 = (Graphics2D) painelGrafico.getGraphics();
        g2.clearRect(0, 0, painelGrafico.getWidth(), painelGrafico.getHeight());

        if (lista == null || lista.isEmpty()) {
            g2.setColor(Color.BLACK);
            g2.drawString("Sem dados para exibir.", 50, painelGrafico.getHeight()/2);
            return;
        }

        int largura = painelGrafico.getWidth();
        int altura = painelGrafico.getHeight();
        int raio = Math.min(largura, altura)/3;
        int total = lista.stream().mapToInt(t -> pegarValor(t, tipo)).sum();
        if (total == 0) {
            g2.setColor(Color.BLACK);
            g2.drawString("Todos os valores são zero.", 50, altura/2);
            return;
        }

        int start = 0;
        Color[] cores = {Color.BLUE, Color.RED, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.CYAN};
        for (int i = 0; i < lista.size(); i++) {
            Time t = lista.get(i);
            int v = pegarValor(t, tipo);
            int ang = (int)Math.round(360.0*v/total);

            g2.setColor(cores[i%cores.length]);
            g2.fillArc(largura/4, altura/4, 2*raio, 2*raio, start, ang);
            g2.setColor(Color.BLACK);
            g2.drawString(t.getNome()+" ("+v+")", largura*2/3, 50+i*20);

            start += ang;
        }
    }
}