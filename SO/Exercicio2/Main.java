package Exercicio2;

public class Main {
    public static void main(String[] args) {

        ThreadSafeArrayList<Integer> lista = new ThreadSafeArrayList<>();

        for (int i = 0; i < 3; i++) {
            int id = i;
            new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    lista.add(id * 10 + j);
                    try { Thread.sleep(500); } catch (InterruptedException e) {}
                }
            }).start();
        }

        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                while (true) {
                    if (lista.size() > 0) {
                        System.out.println("Lendo: " + lista.get(0));
                    }
                    try { Thread.sleep(300); } catch (InterruptedException e) {}
                }
            }).start();
        }

        try {
            Thread.sleep(10000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Encerrando...");
        System.exit(0);
    }
}