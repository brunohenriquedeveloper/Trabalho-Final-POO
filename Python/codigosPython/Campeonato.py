from typing import List, Dict
from .Time import Time
from .Partida import Partida  

class Campeonato:
    def __init__(self, partidas: List[Partida], times: Dict[str, Time]):
        self.partidas: List[Partida] = list(partidas)
        self.times: Dict[str, Time] = dict(times)

        for p in partidas:
            mandante = self.times.get(p.mandante)
            if mandante:
                mandante.atualizarEstatisticas(p)

            visitante = self.times.get(p.visitante)
            if visitante:
                visitante.atualizarEstatisticas(p)

    def adicionarPartida(self, partida: Partida):
        self.partidas.append(partida)

        mandante = self.times.get(partida.mandante)
        if mandante:
            mandante.atualizarEstatisticas(partida)

        visitante = self.times.get(partida.visitante)
        if visitante:
            visitante.atualizarEstatisticas(partida)

    def getPartidas(self) -> List[Partida]:
        return self.partidas

    def getTimes(self) -> Dict[str, Time]:
        return self.times

    def getClassificacaoPorGols(self) -> List[Time]:
        return sorted(self.times.values(), key=lambda t: t.golsPro, reverse=True)

    def getClassificacaoPorPontos(self) -> List[Time]:
        return sorted(self.times.values(), key=lambda t: t.getPontuacao(), reverse=True)

    def getClassificacaoPorSaldoGols(self) -> List[Time]:
        return sorted(self.times.values(), key=lambda t: t.getSaldoGols(), reverse=True)

    def getPartidasPorAno(self, ano: int) -> List[Partida]:
        partidas_ano = []
        for p in self.partidas:
            partes = p.data.split("/")  
            anoPartida = int(partes[2])
            if anoPartida == ano:
                partidas_ano.append(p)
        return partidas_ano

    def getClassificacaoPorVitoriasMandante(self) -> List[Time]:
        return sorted(self.times.values(), key=lambda t: t.vitoriasMandante, reverse=True)

    def getClassificacaoPorVitoriasVisitante(self) -> List[Time]:
        return sorted(self.times.values(), key=lambda t: t.vitoriasVisitante, reverse=True)
