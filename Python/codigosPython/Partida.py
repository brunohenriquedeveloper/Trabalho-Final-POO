class Partida:
    def __init__(
        self,
        mandante: str,
        visitante: str,
        estadio: str,
        data: str,
        hora: str,
        vencedor: str,
        placarMandante: int,
        placarVisitante: int
    ):
        self._mandante = mandante
        self._visitante = visitante
        self._estadio = estadio
        self._data = data
        self._hora = hora
        self._vencedor = vencedor
        self._placarMandante = placarMandante
        self._placarVisitante = placarVisitante

    @property
    def mandante(self) -> str:
        return self._mandante

    @mandante.setter
    def mandante(self, valor: str):
        self._mandante = valor

    @property
    def visitante(self) -> str:
        return self._visitante

    @visitante.setter
    def visitante(self, valor: str):
        self._visitante = valor

    @property
    def estadio(self) -> str:
        return self._estadio

    @estadio.setter
    def estadio(self, valor: str):
        self._estadio = valor

    @property
    def data(self) -> str:
        return self._data

    @data.setter
    def data(self, valor: str):
        self._data = valor

    @property
    def hora(self) -> str:
        return self._hora

    @hora.setter
    def hora(self, valor: str):
        self._hora = valor

    @property
    def vencedor(self) -> str:
        return self._vencedor

    @vencedor.setter
    def vencedor(self, valor: str):
        self._vencedor = valor

    @property
    def placarMandante(self) -> int:
        return self._placarMandante

    @placarMandante.setter
    def placarMandante(self, valor: int):
        self._placarMandante = valor

    @property
    def placarVisitante(self) -> int:
        return self._placarVisitante

    @placarVisitante.setter
    def placarVisitante(self, valor: int):
        self._placarVisitante = valor

    def __str__(self) -> str:
        return f"{self._mandante} {self._placarMandante} x {self._placarVisitante} {self._visitante}"
