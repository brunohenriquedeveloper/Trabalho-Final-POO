import sys
import os
import math

sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from PyQt5 import QtWidgets, QtGui
import pyqtgraph as pg
from PyQt5.QtWidgets import (
    QLineEdit, QLabel, QPushButton,
    QHBoxLayout, QVBoxLayout, QWidget
)
from matplotlib.backends.backend_qt5agg import FigureCanvasQTAgg as FigureCanvas
from matplotlib.figure import Figure

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

        central = QWidget()
        self.setCentralWidget(central)
        mainLayout = QVBoxLayout(central)

        topo = QHBoxLayout()
        mainLayout.addLayout(topo)

        self.campoAno = QLineEdit()
        self.campoAno.setFixedWidth(60)
        topo.addWidget(QLabel("Ano:"))
        topo.addWidget(self.campoAno)

        self.btnGols = QPushButton("Ranking de Gols")
        self.btnPontos = QPushButton("Mais Pontos")
        self.btnSaldo = QPushButton("Saldo de Gols")
        self.btnMelhorDefesa = QPushButton("Melhor Defesa (ano)")
        self.btnMelhorAtaque = QPushButton("Melhor Ataque (ano)")
        self.btnVitMand = QPushButton("Top Vitórias Casa")
        self.btnVitVis = QPushButton("Top Vitórias Fora")

        for btn in (
            self.btnGols, self.btnPontos, self.btnSaldo,
            self.btnMelhorDefesa, self.btnMelhorAtaque,
            self.btnVitMand, self.btnVitVis
        ):
            topo.addWidget(btn)

        self.plotWidget = pg.PlotWidget()
        mainLayout.addWidget(self.plotWidget)

        self.pizzaContainer = QWidget()
        self.pizzaLayout = QVBoxLayout(self.pizzaContainer)
        mainLayout.addWidget(self.pizzaContainer)
        self.pizzaContainer.hide()

        self.btnGols.clicked.connect(lambda:
            self.desenharGrafico(
                self.campeonato.getClassificacaoPorGols(),
                "golsPro", "bar"
            )
        )
        self.btnPontos.clicked.connect(lambda:
            self.desenharGrafico(
                self.campeonato.getClassificacaoPorPontos(),
                "pontos", "barh"
            )
        )
        self.btnSaldo.clicked.connect(lambda:
            self.desenharGrafico(
                self.campeonato.getClassificacaoPorSaldoGols(),
                "saldoGols", "line"
            )
        )
        self.btnMelhorDefesa.clicked.connect(lambda:
            self.acaoAno("defesa")
        )
        self.btnMelhorAtaque.clicked.connect(lambda:
            self.acaoAno("ataque")
        )
        self.btnVitMand.clicked.connect(lambda:
            self.desenharGrafico(
                self.campeonato.getClassificacaoPorVitoriasMandante(),
                "vitoriasMandante", "bar"
            )
        )
        self.btnVitVis.clicked.connect(lambda:
            self.desenharGrafico(
                self.campeonato.getClassificacaoPorVitoriasVisitante(),
                "vitoriasVisitante", "barh"
            )
        )

        self.show()

    # -------------------- Métodos auxiliares --------------------
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
            else:
                lista = self.analise.melhorAtaquePorAno(ano, 5)
                if lista:
                    self.desenharGrafico(lista, "ataque", "pie")
        except ValueError:
            QtWidgets.QMessageBox.warning(self, "Erro", "Digite um ano válido!")

    # -------------------- Desenho de gráficos --------------------
    def desenharGrafico(self, lista, tipo, estilo="bar"):
        # caso pizza
        if estilo == "pie":
            self.plotWidget.hide()
            self.pizzaContainer.show()
            self.mostrarPizza(lista, tipo)
            return

        # barra ou linha
        self.pizzaContainer.hide()
        self.plotWidget.show()
        plotItem = self.plotWidget.getPlotItem()
        plotItem.clear()

        # pega referências aos eixos
        bottom_axis = plotItem.getAxis('bottom')
        left_axis   = plotItem.getAxis('left')

        # define quais eixos aparecem
        if estilo == "barh":
            plotItem.showAxis('left')
            plotItem.hideAxis('bottom')
        else:
            plotItem.showAxis('bottom')
            plotItem.showAxis('left')

        # limpa ticks do eixo que não será usado para categorias
        if estilo == "barh":
            bottom_axis.setTicks([])
        else:
            left_axis.setTicks([])

        # margens para caber rótulos
        if estilo == "bar":
            plotItem.layout.setContentsMargins(40, 10, 10, 120)
        elif estilo == "barh":
            plotItem.layout.setContentsMargins(200, 10, 10, 40)
        else:
            plotItem.layout.setContentsMargins(40, 10, 10, 120)

        self.plotWidget.setBackground("w")
        self.plotWidget.showGrid(x=True, y=True, alpha=0.3)

        nomes   = [t.nome for t in lista]
        valores = [self.pegarValor(t, tipo) for t in lista]
        brush   = pg.mkBrush(*self.pegarCor(tipo))

        # Top 10
        pares = sorted(zip(nomes, valores), key=lambda nv: nv[1], reverse=True)[:10]
        nomes, valores = zip(*pares)
        max_val = max(valores) * 1.1

        if estilo == "bar":
            bg = pg.BarGraphItem(
                x=range(len(valores)), height=valores,
                width=0.6, brush=brush
            )
            self.plotWidget.addItem(bg)

            ax = bottom_axis
            ax.setTicks([list(zip(range(len(nomes)), nomes))])
            ax.setStyle(
                tickFont=QtGui.QFont("Arial", 8),
                tickTextOffset=10
            )

            for i, v in enumerate(valores):
                txt = pg.TextItem(str(v), anchor=(0.5, -0.3), color=(0, 0, 0))
                txt.setFont(QtGui.QFont("Arial", 9))
                txt.setPos(i, v)
                self.plotWidget.addItem(txt)

            self.plotWidget.setXRange(-0.5, len(valores) - 0.5)
            self.plotWidget.setYRange(0, max_val)
            plotItem.enableAutoRange(False, False)

        elif estilo == "barh":
            bg = pg.BarGraphItem(
                y=range(len(valores)), x0=0, x1=valores,
                height=0.6, brush=brush
            )
            self.plotWidget.addItem(bg)

            ay = left_axis
            ay.setTicks([list(zip(range(len(nomes)), nomes))])
            ay.setStyle(
                tickFont=QtGui.QFont("Arial", 8),
                tickTextOffset=10
            )

            for i, v in enumerate(valores):
                txt = pg.TextItem(str(v), anchor=(0, 0.5), color=(0, 0, 0))
                txt.setFont(QtGui.QFont("Arial", 9))
                txt.setPos(v, i)
                self.plotWidget.addItem(txt)

            self.plotWidget.setYRange(-0.5, len(valores) - 0.5)
            self.plotWidget.setXRange(0, max_val)
            plotItem.enableAutoRange(False, False)

        else:  # line
            x = list(range(len(valores)))
            self.plotWidget.plot(
                x, valores,
                pen=pg.mkPen(*self.pegarCor(tipo), width=2),
                symbol="o", symbolBrush=brush
            )

            ax = bottom_axis
            ax.setTicks([list(zip(x, nomes))])
            ax.setStyle(
                tickFont=QtGui.QFont("Arial", 8),
                tickTextOffset=10
            )

            for i, v in enumerate(valores):
                txt = pg.TextItem(str(v), anchor=(0.5, -1.0), color=(0, 0, 0))
                txt.setFont(QtGui.QFont("Arial", 9))
                txt.setPos(i, v)
                self.plotWidget.addItem(txt)

            self.plotWidget.setXRange(-0.5, len(valores) - 0.5)
            self.plotWidget.setYRange(0, max_val)
            plotItem.enableAutoRange(False, False)

    # -------------------- Pizza --------------------
    def mostrarPizza(self, lista, tipo):
        for i in reversed(range(self.pizzaLayout.count())):
            w = self.pizzaLayout.itemAt(i).widget()
            if w:
                w.setParent(None)

        nomes = [t.nome for t in lista]
        valores = [self.pegarValor(t, tipo) for t in lista]
        cores = [
            tuple(pg.intColor(i, hues=len(lista)).getRgb()[:3])
            for i in range(len(lista))
        ]
        cores = [(r/255, g/255, b/255) for r, g, b in cores]

        fig = Figure(figsize=(6, 6), facecolor="w")
        ax = fig.add_subplot(111)
        ax.set_facecolor("w")
        ax.pie(valores, labels=nomes, autopct="%1.1f%%", colors=cores)
        fig.tight_layout()

        canvas = FigureCanvas(fig)
        self.pizzaLayout.addWidget(canvas)
        canvas.draw()


if __name__ == "__main__":
    caminhoCsv = R"C:\Users\Bruno\Documents\Trabalho-Final-POO\dados\campeonato-brasileiro-full.csv"
    times = {}
    partidas = LeitorCSV.carregarPartidas(caminhoCsv, times)
    camp = Campeonato(partidas, times)

    app = QtWidgets.QApplication(sys.argv)
    gui = MainGui(camp)
    sys.exit(app.exec_())
