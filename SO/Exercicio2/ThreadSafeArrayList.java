package Exercicio2;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class ThreadSafeArrayList<T> {

    private final ArrayList<T> lista = new ArrayList<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public T get(int index) {
        lock.readLock().lock();
        try {
            return lista.get(index);
        } finally {
            lock.readLock().unlock();
        }
    }

    public int size() {
        lock.readLock().lock();
        try {
            return lista.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    // Escrita (exclusiva)
    public void add(T elemento) {
        lock.writeLock().lock();
        try {
            lista.add(elemento);
            System.out.println("Adicionado: " + elemento);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public T remove(int index) {
        lock.writeLock().lock();
        try {
            T removido = lista.remove(index);
            System.out.println("Removido: " + removido);
            return removido;
        } finally {
            lock.writeLock().unlock();
        }
    }
}