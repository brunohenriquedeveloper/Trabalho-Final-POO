import csv
from typing import List, Dict, TYPE_CHECKING

if TYPE_CHECKING:
    from .Time import Time
    from .Partida import Partida


class LeitorCSV:
    @staticmethod
    def carregarPartidas(caminho: str, times: Dict[str, "Time"]) -> List["Partida"]:
        from .Partida import Partida
        from .Time import Time

        partidas: List[Partida] = []

        with open(caminho, encoding="utf-8") as f:
            leitor = csv.reader(f)
            next(leitor, None)  

            for linha in leitor:
                if not linha or len(linha) < 16:
                    continue

                dados = [col.replace('"', '').strip() for col in linha]

                mandante = dados[4]
                visitante = dados[5]
                estadio = dados[11]
                data = dados[2]
                hora = dados[3]
                vencedor = dados[10]

                try:
                    placarMandante = int(dados[12])
                    placarVisitante = int(dados[13])
                except ValueError:
                    continue

                estadoMandante = dados[14]
                estadoVisitante = dados[15]

                p = Partida(
                    mandante=mandante,
                    visitante=visitante,
                    estadio=estadio,
                    data=data,
                    hora=hora,
                    vencedor=vencedor,
                    placarMandante=placarMandante,
                    placarVisitante=placarVisitante,
                )
                partidas.append(p)

                if mandante not in times:
                    times[mandante] = Time(nome=mandante, tecnico="", estado=estadoMandante)
                if visitante not in times:
                    times[visitante] = Time(nome=visitante, tecnico="", estado=estadoVisitante)

                times[mandante].atualizarEstatisticas(p)
                times[visitante].atualizarEstatisticas(p)

        return partidas
