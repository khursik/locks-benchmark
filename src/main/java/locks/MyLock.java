package locks;

import java.util.concurrent.atomic.AtomicInteger;

public interface MyLock {

    void lock() throws InterruptedException;

    void unlock();

    int getCounter();
}
