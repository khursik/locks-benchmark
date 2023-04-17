import locks.*;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

public class Benchmark implements Callable<Integer> {

    private final MyLock lock;

    public Benchmark(LockType lockType) {
        this.lock = getLockImp(lockType);
    }

    @Override
    public Integer call() {
        try {
            lock.lock();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return lock.getCounter();
    }

    private MyLock getLockImp(LockType lockType) {
        switch (lockType) {
            case TAS:
                return new TASLock();
            case TTAS:
                return new TTASLock();
            case BACKOFF:
                return new ExponentialBackoff();
            case CLH:
                return new CLHLock();
            case MCS:
                return new MCSLock();
            default:
                throw new IllegalArgumentException("Expected valid lock type value.");
        }
    }
}
