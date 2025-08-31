from .Partida import Partida

class Time:
    def __init__(self, nome: str, tecnico: str, estado: str):
        self._nome = nome
        self._tecnico = tecnico
        self._estado = estado

        self._vitorias = 0
        self._empates = 0
        self._derrotas = 0
        self._golsPro = 0
        self._golsContra = 0

        self._vitoriasMandante = 0
        self._vitoriasVisitante = 0
        self._partidasJogadas = 0

    # Getters
    @property
    def nome(self) -> str:
        return self._nome

    @property
    def tecnico(self) -> str:
        return self._tecnico

    @property
    def estado(self) -> str:
        return self._estado

    @property
    def vitorias(self) -> int:
        return self._vitorias

    @property
    def empates(self) -> int:
        return self._empates

    @property
    def derrotas(self) -> int:
        return self._derrotas

    @property
    def golsPro(self) -> int:
        return self._golsPro

    @property
    def golsContra(self) -> int:
        return self._golsContra

    @property
    def vitoriasMandante(self) -> int:
        return self._vitoriasMandante

    @property
    def vitoriasVisitante(self) -> int:
        return self._vitoriasVisitante

    @property
    def partidasJogadas(self) -> int:
        return self._partidasJogadas

    def atualizarEstatisticas(self, p: Partida):
        if p.mandante == self._nome:
            self._golsPro += p.placarMandante
            self._golsContra += p.placarVisitante
            self._partidasJogadas += 1

            if p.vencedor == self._nome:
                self._vitorias += 1
                self._vitoriasMandante += 1
            elif p.vencedor == "Empate":
                self._empates += 1
            else:
                self._derrotas += 1

        elif p.visitante == self._nome:
            self._golsPro += p.placarVisitante
            self._golsContra += p.placarMandante
            self._partidasJogadas += 1

            if p.vencedor == self._nome:
                self._vitorias += 1
                self._vitoriasVisitante += 1
            elif p.vencedor == "Empate":
                self._empates += 1
            else:
                self._derrotas += 1

    def getPontuacao(self) -> int:
        return self._vitorias * 3 + self._empates

    def getSaldoGols(self) -> int:
        return self._golsPro - self._golsContra

    def getMediaGolsSofridos(self) -> float:
        return self._golsContra / self._partidasJogadas if self._partidasJogadas > 0 else 0.0

    def __str__(self):
        return (
            f"{self._nome} - Pontos: {self.getPontuacao()}, "
            f"Gols: {self._golsPro}:{self._golsContra}, "
            f"Saldo: {self.getSaldoGols()}, "
            f"Vitórias Casa: {self._vitoriasMandante}, "
            f"Vitórias Fora: {self._vitoriasVisitante}, "
            f"Média Gols Sofridos: {self.getMediaGolsSofridos():.2f}"
        )
