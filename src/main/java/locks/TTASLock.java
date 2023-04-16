package locks;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TTASLock implements MyLock {

    AtomicBoolean state = new AtomicBoolean(false);
    AtomicInteger counter = new AtomicInteger(0);


    @Override
    public void lock() {
        while (true) {
            while (state.get()) {}
            if (!state.getAndSet(true)) {
                counter.getAndIncrement();
                return;
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
