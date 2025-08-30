from typing import List, Dict, Optional
from .Campeonato import Campeonato
from .Time import Time
from .Partida import Partida

class AnaliseEstatistica:
    def __init__(self, campeonato: Campeonato):
        self.campeonato = campeonato

    def gerarTimes(self) -> Dict[str, Time]:
        times: Dict[str, Time] = {}
        for p in self.campeonato.getPartidas():
            if p.mandante not in times:
                times[p.mandante] = Time(p.mandante, "", "")
            if p.visitante not in times:
                times[p.visitante] = Time(p.visitante, "", "")
            times[p.mandante].atualizarEstatisticas(p)
            times[p.visitante].atualizarEstatisticas(p)
        return times

    def rankingPorPontos(self) -> List[Time]:
        ranking = list(self.gerarTimes().values())
        ranking.sort(key=lambda t: t.getPontuacao(), reverse=True)
        return ranking

    def timeComMaisGols(self) -> Optional[Time]:
        times = self.gerarTimes().values()
        return max(times, key=lambda t: t.golsPro, default=None)

    def melhorDefesaGeral(self) -> Optional[Time]:
        times = self.gerarTimes().values()
        return min(times, key=lambda t: t.golsContra, default=None)

    def rankingPorMediaGolsSofridos(self) -> List[Time]:
        ranking = list(self.gerarTimes().values())
        ranking.sort(key=lambda t: t.getMediaGolsSofridos())
        return ranking

    def maisVitoriasMandanteGeral(self) -> Optional[Time]:
        times = self.gerarTimes().values()
        return max(times, key=lambda t: t.vitoriasMandante, default=None)

    def maisVitoriasVisitanteGeral(self) -> Optional[Time]:
        times = self.gerarTimes().values()
        return max(times, key=lambda t: t.vitoriasVisitante, default=None)

    def gerarTimesPorAno(self, ano: int) -> Dict[str, Time]:
        times: Dict[str, Time] = {}
        partidasDoAno = self.campeonato.getPartidasPorAno(ano)
        for p in partidasDoAno:
            if p.mandante not in times:
                times[p.mandante] = Time(p.mandante, "", "")
            if p.visitante not in times:
                times[p.visitante] = Time(p.visitante, "", "")
            times[p.mandante].atualizarEstatisticas(p)
            times[p.visitante].atualizarEstatisticas(p)
        return times

    def melhoresDefesasPorAno(self, ano: int, top: int) -> List[Time]:
        times = list(self.gerarTimesPorAno(ano).values())
        times.sort(key=lambda t: t.golsContra)  
        return times[:top]

    def melhorDefesaPorAno(self, ano: int) -> List[Time]:
        times = list(self.gerarTimesPorAno(ano).values())
        times.sort(key=lambda t: t.golsContra)
        return times

    def melhorDefesaTop1PorAno(self, ano: int) -> Optional[Time]:
        lista = self.melhorDefesaPorAno(ano)
        return lista[0] if lista else None

    def maisVitoriasMandantePorAno(self, ano: int) -> List[Time]:
        times = list(self.gerarTimesPorAno(ano).values())
        times.sort(key=lambda t: t.vitoriasMandante, reverse=True)
        return times

    def maisVitoriasVisitantePorAno(self, ano: int) -> List[Time]:
        times = list(self.gerarTimesPorAno(ano).values())
        times.sort(key=lambda t: t.vitoriasVisitante, reverse=True)
        return times

    def melhorAtaqueGeral(self, top: int) -> List[Time]:
        times = list(self.gerarTimes().values())
        times.sort(key=lambda t: t.golsPro, reverse=True)
        return times[:top]

    def melhorAtaquePorAno(self, ano: int, top: int) -> List[Time]:
        times = list(self.gerarTimesPorAno(ano).values())
        times.sort(key=lambda t: t.golsPro, reverse=True)
        return times[:top]

    def topVitoriasMandante(self, top: int) -> List[Time]:
        times = list(self.gerarTimes().values())
        times.sort(key=lambda t: t.vitoriasMandante, reverse=True)
        return times[:top]

    def topVitoriasVisitante(self, top: int) -> List[Time]:
        times = list(self.gerarTimes().values())
        times.sort(key=lambda t: t.vitoriasVisitante, reverse=True)
        return times[:top]
