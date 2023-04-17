package locks;

public interface MyLock {

    void lock() throws InterruptedException;

    void unlock();

    int getCounter();
}
