from .LeitorCSV import LeitorCSV
from .Campeonato import Campeonato
from .Time import Time

def main():
    caminho_csv = "../dados/campeonato-brasileiro-full.csv"
    times = {}

    partidas = LeitorCSV.carregarPartidas(caminho_csv, times)

    camp = Campeonato(partidas, times)

    for t in times.values():
        print(t)

if __name__ == "__main__":
    main()
