import locks.*;

import java.util.concurrent.atomic.AtomicInteger;

public class Benchmark implements Runnable {

    private final MyLock lock;
    private final AtomicInteger counter = new AtomicInteger(0);

    public Benchmark(LockType lockType) {
        this.lock = getLockImp(lockType);

    }

    @Override
    public void run() {
        try {
            lock.lock();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private MyLock getLockImp(LockType lockType) {
        switch (lockType) {
            case TAS:
                return new TASLock();
            case TTAS:
                return new TTASLock();
            case BACKOFF:
                return new ExponentialBackoff();
            default:
                throw new IllegalArgumentException("Expected valid lock type value.");
        }
    }
}
