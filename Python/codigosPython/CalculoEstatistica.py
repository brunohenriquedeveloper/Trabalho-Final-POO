from typing import List, Dict, Optional
from .AnaliseEstatistica import AnaliseEstatistica
from .Time import Time

class CalculoEstatistica(AnaliseEstatistica):
    def __init__(self, campeonato):
        super().__init__(campeonato)

        self._times: Dict[str, Time] = {}
        for p in self.campeonato.partidas: 
            if p.mandante not in self._times:
                self._times[p.mandante] = Time(p.mandante, "", "")
            if p.visitante not in self._times:
                self._times[p.visitante] = Time(p.visitante, "", "")
            self._times[p.mandante].atualizarEstatisticas(p)
            self._times[p.visitante].atualizarEstatisticas(p)

    def gerarTimes(self) -> Dict[str, Time]:
        return self._times

    def rankingPorPontos(self) -> List[Time]:
        return sorted(self._times.values(), key=lambda t: t.getPontuacao(), reverse=True)

    def timeComMaisGols(self) -> Optional[Time]:
        return max(self._times.values(), key=lambda t: t.golsPro, default=None)

    def melhorDefesaGeral(self) -> Optional[Time]:
        return min(self._times.values(), key=lambda t: t.golsContra, default=None)

    def rankingPorMediaGolsSofridos(self) -> List[Time]:
        return sorted(self._times.values(), key=lambda t: t.getMediaGolsSofridos())

    def maisVitoriasMandanteGeral(self) -> Optional[Time]:
        return max(self._times.values(), key=lambda t: t.vitoriasMandante, default=None)

    def maisVitoriasVisitanteGeral(self) -> Optional[Time]:
        return max(self._times.values(), key=lambda t: t.vitoriasVisitante, default=None)

    def melhorAtaqueGeral(self, top: int) -> List[Time]:
        return sorted(self._times.values(), key=lambda t: t.golsPro, reverse=True)[:top]

    def topVitoriasMandante(self, top: int) -> List[Time]:
        return sorted(self._times.values(), key=lambda t: t.vitoriasMandante, reverse=True)[:top]

    def topVitoriasVisitante(self, top: int) -> List[Time]:
        return sorted(self._times.values(), key=lambda t: t.vitoriasVisitante, reverse=True)[:top]

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
        return sorted(self.gerarTimesPorAno(ano).values(), key=lambda t: t.golsContra)[:top]

    def melhorDefesaPorAno(self, ano: int) -> List[Time]:
        return sorted(self.gerarTimesPorAno(ano).values(), key=lambda t: t.golsContra)

    def melhorDefesaTop1PorAno(self, ano: int) -> Optional[Time]:
        lista = self.melhorDefesaPorAno(ano)
        return lista[0] if lista else None

    def maisVitoriasMandantePorAno(self, ano: int) -> List[Time]:
        return sorted(self.gerarTimesPorAno(ano).values(), key=lambda t: t.vitoriasMandante, reverse=True)

    def maisVitoriasVisitantePorAno(self, ano: int) -> List[Time]:
        return sorted(self.gerarTimesPorAno(ano).values(), key=lambda t: t.vitoriasVisitante, reverse=True)

    def melhorAtaquePorAno(self, ano: int, top: int) -> List[Time]:
        return sorted(self.gerarTimesPorAno(ano).values(), key=lambda t: t.golsPro, reverse=True)[:top]
