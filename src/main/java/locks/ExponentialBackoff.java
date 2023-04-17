package locks;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Math.random;
import static java.lang.Thread.sleep;

public class ExponentialBackoff implements MyLock {

    private final static long MIN_DELAY = 100;
    private final static long MAX_DELAY = 3000;
    AtomicBoolean state = new AtomicBoolean(false);
    AtomicInteger counter = new AtomicInteger(0);


    @Override
    public void lock() throws InterruptedException {
        long delay = MIN_DELAY;
        while (true) {
            while (state.get()) {}
            if(!state.getAndSet(true)) {
                counter.getAndIncrement();
                return;
            }
            sleep((long) (random() * delay));
            if (delay < MAX_DELAY) {
                delay *= 2;
            }
        }
    }

    @Override
    public void unlock() {
        state.set(false);
    }

    @Override
    public int getCounter() {
        return counter.get();
    }
}
