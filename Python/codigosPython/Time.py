from typing import Union
from .Partida import Partida  

class Time:
    def __init__(self, nome: str, tecnico: str, estado: str):
        self.nome = nome
        self.tecnico = tecnico
        self.estado = estado

        self.vitorias = 0
        self.empates = 0
        self.derrotas = 0
        self.golsPro = 0
        self.golsContra = 0

        self.vitoriasMandante = 0
        self.vitoriasVisitante = 0
        self.partidasJogadas = 0

    def atualizarEstatisticas(self, p: Partida):
        if p.mandante == self.nome:
            self.golsPro += p.placarMandante
            self.golsContra += p.placarVisitante
            self.partidasJogadas += 1

            if p.vencedor == self.nome:
                self.vitorias += 1
                self.vitoriasMandante += 1
            elif p.vencedor == "Empate":
                self.empates += 1
            else:
                self.derrotas += 1

        elif p.visitante == self.nome:
            self.golsPro += p.placarVisitante
            self.golsContra += p.placarMandante
            self.partidasJogadas += 1

            if p.vencedor == self.nome:
                self.vitorias += 1
                self.vitoriasVisitante += 1
            elif p.vencedor == "Empate":
                self.empates += 1
            else:
                self.derrotas += 1

    def getPontuacao(self) -> int:
        return self.vitorias * 3 + self.empates

    def getSaldoGols(self) -> int:
        return self.golsPro - self.golsContra

    def getMediaGolsSofridos(self) -> float:
        return self.golsContra / self.partidasJogadas if self.partidasJogadas > 0 else 0.0

    def __str__(self):
        return (
            f"{self.nome} - Pontos: {self.getPontuacao()}, "
            f"Gols: {self.golsPro}:{self.golsContra}, "
            f"Saldo: {self.getSaldoGols()}, "
            f"Vitórias Casa: {self.vitoriasMandante}, "
            f"Vitórias Fora: {self.vitoriasVisitante}, "
            f"Média Gols Sofridos: {self.getMediaGolsSofridos():.2f}"
        )
