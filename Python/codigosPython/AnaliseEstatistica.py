from abc import ABC, abstractmethod
from typing import List, Dict, Optional
from .Campeonato import Campeonato
from .Time import Time


class AnaliseEstatistica(ABC):
    def __init__(self, campeonato: Campeonato):
        self.campeonato = campeonato

    @abstractmethod
    def gerarTimes(self) -> Dict[str, Time]:
        pass

    @abstractmethod
    def rankingPorPontos(self) -> List[Time]:
        pass

    @abstractmethod
    def timeComMaisGols(self) -> Optional[Time]:
        pass

    @abstractmethod
    def melhorDefesaGeral(self) -> Optional[Time]:
        pass

    @abstractmethod
    def rankingPorMediaGolsSofridos(self) -> List[Time]:
        pass

    @abstractmethod
    def maisVitoriasMandanteGeral(self) -> Optional[Time]:
        pass

    @abstractmethod
    def maisVitoriasVisitanteGeral(self) -> Optional[Time]:
        pass

    @abstractmethod
    def gerarTimesPorAno(self, ano: int) -> Dict[str, Time]:
        pass

    @abstractmethod
    def melhoresDefesasPorAno(self, ano: int, top: int) -> List[Time]:
        pass

    @abstractmethod
    def melhorDefesaPorAno(self, ano: int) -> List[Time]:
        pass

    @abstractmethod
    def melhorDefesaTop1PorAno(self, ano: int) -> Optional[Time]:
        pass

    @abstractmethod
    def maisVitoriasMandantePorAno(self, ano: int) -> List[Time]:
        pass

    @abstractmethod
    def maisVitoriasVisitantePorAno(self, ano: int) -> List[Time]:
        pass

    @abstractmethod
    def melhorAtaqueGeral(self, top: int) -> List[Time]:
        pass

    @abstractmethod
    def melhorAtaquePorAno(self, ano: int, top: int) -> List[Time]:
        pass

    @abstractmethod
    def topVitoriasMandante(self, top: int) -> List[Time]:
        pass

    @abstractmethod
    def topVitoriasVisitante(self, top: int) -> List[Time]:
        pass
