import sys
import os
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from PyQt5 import QtWidgets, QtGui
import pyqtgraph as pg
from PyQt5.QtWidgets import QLineEdit, QLabel, QPushButton, QHBoxLayout, QVBoxLayout, QWidget
from codigosPython.Campeonato import Campeonato
from codigosPython.AnaliseEstatistica import AnaliseEstatistica
from codigosPython.Time import Time
from codigosPython.LeitorCSV import LeitorCSV

class MainGui(QtWidgets.QMainWindow):
    def __init__(self, campeonato: Campeonato):
        super().__init__()
        self.campeonato = campeonato
        self.analise = AnaliseEstatistica(campeonato)

        self.setWindowTitle("Campeonato Brasileiro – Estatísticas")
        self.setGeometry(100, 100, 1200, 650)

        # ---------------- Topo ----------------
        centralWidget = QWidget()
        self.setCentralWidget(centralWidget)
        layout = QVBoxLayout()
        centralWidget.setLayout(layout)

        topoLayout = QHBoxLayout()
        layout.addLayout(topoLayout)

        self.campoAno = QLineEdit()
        self.campoAno.setFixedWidth(60)
        topoLayout.addWidget(QLabel("Ano:"))
        topoLayout.addWidget(self.campoAno)

        self.btnGols = QPushButton("Ranking de Gols")
        self.btnPontos = QPushButton("Mais Pontos")
        self.btnSaldo = QPushButton("Saldo de Gols")
        self.btnMelhorDefesa = QPushButton("Melhor Defesa (ano)")
        self.btnMelhorAtaque = QPushButton("Melhor Ataque (ano)")
        self.btnVitoriasMandante = QPushButton("Top Vitórias Casa")
        self.btnVitoriasVisitante = QPushButton("Top Vitórias Fora")

        for btn in [self.btnGols, self.btnPontos, self.btnSaldo, self.btnMelhorDefesa,
                    self.btnMelhorAtaque, self.btnVitoriasMandante, self.btnVitoriasVisitante]:
            topoLayout.addWidget(btn)

        # ---------------- Painel de gráfico ----------------
        self.plotWidget = pg.PlotWidget()
        layout.addWidget(self.plotWidget)

        # ---------------- Conectar eventos ----------------
        self.btnGols.clicked.connect(lambda: self.desenharGrafico(self.campeonato.getClassificacaoPorGols(), "golsPro", "bar"))
        self.btnPontos.clicked.connect(lambda: self.desenharGrafico(self.campeonato.getClassificacaoPorPontos(), "pontos", "barh"))
        self.btnSaldo.clicked.connect(lambda: self.desenharGrafico(self.campeonato.getClassificacaoPorSaldoGols(), "saldoGols", "line"))
        self.btnMelhorDefesa.clicked.connect(lambda: self.acaoAno("defesa"))
        self.btnMelhorAtaque.clicked.connect(lambda: self.acaoAno("ataque"))
        self.btnVitoriasMandante.clicked.connect(lambda: self.desenharGrafico(self.campeonato.getClassificacaoPorVitoriasMandante(), "vitoriasMandante", "bar"))
        self.btnVitoriasVisitante.clicked.connect(lambda: self.desenharGrafico(self.campeonato.getClassificacaoPorVitoriasVisitante(), "vitoriasVisitante", "barh"))

        self.show()

    # ---------------- Métodos ----------------
    def pegarValor(self, time: Time, tipo: str) -> int:
        return {
            "golsPro": time.golsPro,
            "ataque": time.golsPro,
            "pontos": time.getPontuacao(),
            "vitoriasMandante": time.vitoriasMandante,
            "vitoriasVisitante": time.vitoriasVisitante,
            "defesa": time.golsContra,
            "saldoGols": time.getSaldoGols()
        }.get(tipo, 0)

    def pegarCor(self, tipo: str):
        cores = {
            "golsPro": (52, 152, 219),
            "ataque": (52, 152, 219),
            "pontos": (46, 204, 113),
            "vitoriasMandante": (230, 126, 34),
            "vitoriasVisitante": (231, 76, 60),
            "defesa": (155, 89, 182),
            "saldoGols": (241, 196, 15)
        }
        return cores.get(tipo, (150, 150, 150))

    def acaoAno(self, tipo: str):
        try:
            ano = int(self.campoAno.text().strip())
            if tipo == "defesa":
                lista = self.analise.melhoresDefesasPorAno(ano, 5)
                if lista:
                    self.desenharGrafico(lista, "defesa", "line")
            elif tipo == "ataque":
                lista = self.analise.melhorAtaquePorAno(ano, 5)
                if lista:
                    self.desenharGrafico(lista, "ataque", "pie")
        except ValueError:
            QtWidgets.QMessageBox.warning(self, "Erro", "Digite um ano válido!")

    def desenharGrafico(self, lista, tipo, estilo="bar"):
        self.plotWidget.clear()
        self.plotWidget.show()
        self.plotWidget.setBackground('w')  # fundo branco

        nomes = [t.nome for t in lista]
        valores = [self.pegarValor(t, tipo) for t in lista]
        cor = self.pegarCor(tipo)
        brush = pg.mkBrush(*cor)

        if estilo == "bar":
            bg = pg.BarGraphItem(x=range(len(valores)), height=valores, width=0.6, brush=brush)
            self.plotWidget.addItem(bg)

        # Rótulos rotacionados abaixo das barras
            for i, nome in enumerate(nomes):
                text = pg.TextItem(text=nome, anchor=(0.5, 1.0), angle=-45, color=(0, 0, 0))
                text.setFont(QtGui.QFont("Arial", 9))
                text.setPos(i, 0)
                self.plotWidget.addItem(text)

        # Eixo X com nomes
            ax = self.plotWidget.getAxis('bottom')
            ax.setTicks([list(zip(range(len(nomes)), nomes))])
            ax.setStyle(showValues=True)

        elif estilo == "barh":
            bg = pg.BarGraphItem(y=range(len(valores)), x0=0, x1=valores, height=0.6, brush=brush)
            self.plotWidget.addItem(bg)

            ay = self.plotWidget.getAxis('left')
            ay.setTicks([list(zip(range(len(nomes)), nomes))])
            ay.setStyle(tickTextAngle=0)

        elif estilo == "line":
            self.plotWidget.plot(range(len(valores)), valores, pen=pg.mkPen(*cor, width=2), symbol='o')

        # Rótulos nos pontos
            for i, valor in enumerate(valores):
                text = pg.TextItem(text=str(valor), anchor=(0.5, -1.0), color=(0, 0, 0))
                text.setFont(QtGui.QFont("Arial", 9))
                text.setPos(i, valor)
                self.plotWidget.addItem(text)

            ax = self.plotWidget.getAxis('bottom')
            ax.setTicks([list(zip(range(len(nomes)), nomes))])
            ax.setStyle(showValues=True)

        elif estilo == "pie":
            self.plotWidget.hide()
            self.mostrarPizza(lista, tipo)

    def mostrarPizza(self, lista, tipo):
        import matplotlib.pyplot as plt
        nomes = [t.nome for t in lista]
        valores = [self.pegarValor(t, tipo) for t in lista]

        cores = [tuple(pg.intColor(i, hues=len(lista)).getRgb()[:3]) for i in range(len(lista))]
        cores = [(r/255, g/255, b/255) for r,g,b in cores]

        plt.figure(figsize=(6,6))
        plt.pie(valores, labels=nomes, autopct="%1.1f%%", colors=cores)
        plt.show()


if __name__ == "__main__":
    caminhoCsv = R"C:\Users\Bruno\Documents\Trabalho-Final-POO\dados\campeonato-brasileiro-full.csv"
    times = {}
    partidas = LeitorCSV.carregarPartidas(caminhoCsv, times)
    camp = Campeonato(partidas, times)

    app = QtWidgets.QApplication(sys.argv)
    gui = MainGui(camp)
    sys.exit(app.exec_())
