import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class BarbeiroDorminhoco {

    static final int MAX_CLIENTES = 10;

    static Semaphore clientes = new Semaphore(0);
    static Semaphore barbeiros = new Semaphore(2);
    static Semaphore mutex = new Semaphore(1);

    static Queue<Integer> fila = new LinkedList<>();
    static int idCliente = 1;

    static Random rand = new Random();

    public static void main(String[] args) {

        for (int i = 1; i <= 2; i++) {
            int id = i;
            new Thread(() -> barbeiro(id)).start();
        }

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(4000 + rand.nextInt(2000)); 
                    cliente();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    static void barbeiro(int id) {
        while (true) {
            try {
                clientes.acquire();

                mutex.acquire();
                int cliente = fila.poll();
                System.out.println("Barbeiro " + id + " atendendo cliente " + cliente);
                mutex.release();

                int tempo = 5000 + rand.nextInt(10000);
                Thread.sleep(tempo);

                System.out.println("Barbeiro " + id + " terminou cliente " + cliente);

                barbeiros.release();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static void cliente() {
        try {
            mutex.acquire();

            if (fila.size() < MAX_CLIENTES) {
                int clienteId = idCliente++;
                fila.add(clienteId);
                System.out.println("Cliente " + clienteId + " entrou na fila");

                clientes.release();
                barbeiros.acquire();

            } else {
                System.out.println("Fila cheia! Cliente foi embora");
            }

            mutex.release();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}