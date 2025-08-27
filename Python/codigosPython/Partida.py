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
        self.mandante = mandante
        self.visitante = visitante
        self.estadio = estadio
        self.data = data
        self.hora = hora
        self.vencedor = vencedor
        self.placarMandante = placarMandante
        self.placarVisitante = placarVisitante

    def getMandante(self) -> str:
        return self.mandante

    def setMandante(self, mandante: str):
        self.mandante = mandante

    def getVisitante(self) -> str:
        return self.visitante

    def setVisitante(self, visitante: str):
        self.visitante = visitante

    def getEstadio(self) -> str:
        return self.estadio

    def setEstadio(self, estadio: str):
        self.estadio = estadio

    def getData(self) -> str:
        return self.data

    def setData(self, data: str):
        self.data = data

    def getHora(self) -> str:
        return self.hora

    def setHora(self, hora: str):
        self.hora = hora

    def getVencedor(self) -> str:
        return self.vencedor

    def setVencedor(self, vencedor: str):
        self.vencedor = vencedor

    def getPlacarMandante(self) -> int:
        return self.placarMandante

    def setPlacarMandante(self, placarMandante: int):
        self.placarMandante = placarMandante

    def getPlacarVisitante(self) -> int:
        return self.placarVisitante

    def setPlacarVisitante(self, placarVisitante: int):
        self.placarVisitante = placarVisitante

    def __str__(self) -> str:
        return f"{self.mandante} {self.placarMandante} x {self.placarVisitante} {self.visitante}"
