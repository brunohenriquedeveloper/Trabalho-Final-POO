from typing import List, Dict
from .Partida import Partida
from .Time import Time

class Campeonato:
    def __init__(self, partidas: List[Partida], times: Dict[str, Time]):
        self._partidas: List[Partida] = list(partidas)
        self._times: Dict[str, Time] = dict(times)

        for p in partidas:
            self._atualizarEstatisticasTimes(p)


    def _atualizarEstatisticasTimes(self, partida: Partida):
        mandante = self._times.get(partida.mandante)
        if mandante:
            mandante.atualizarEstatisticas(partida)

        visitante = self._times.get(partida.visitante)
        if visitante:
            visitante.atualizarEstatisticas(partida)


    def adicionarPartida(self, partida: Partida):
        self._partidas.append(partida)
        self._atualizarEstatisticasTimes(partida)


    @property
    def partidas(self) -> List[Partida]:
        return self._partidas.copy() 

    @property
    def times(self) -> Dict[str, Time]:
        return dict(self._times)  

    def getClassificacaoPorGols(self) -> List[Time]:
        return sorted(self._times.values(), key=lambda t: t.golsPro, reverse=True)

    def getClassificacaoPorPontos(self) -> List[Time]:
        return sorted(self._times.values(), key=lambda t: t.getPontuacao(), reverse=True)

    def getClassificacaoPorSaldoGols(self) -> List[Time]:
        return sorted(self._times.values(), key=lambda t: t.getSaldoGols(), reverse=True)

    def getClassificacaoPorVitoriasMandante(self) -> List[Time]:
        return sorted(self._times.values(), key=lambda t: t.vitoriasMandante, reverse=True)

    def getClassificacaoPorVitoriasVisitante(self) -> List[Time]:
        return sorted(self._times.values(), key=lambda t: t.vitoriasVisitante, reverse=True)


    def getPartidasPorAno(self, ano: int) -> List[Partida]:
        partidas_ano = []
        for p in self._partidas:
            try:
                anoPartida = int(p.data.split("/")[2])
                if anoPartida == ano:
                    partidas_ano.append(p)
            except (IndexError, ValueError):
                continue  
        return partidas_ano
