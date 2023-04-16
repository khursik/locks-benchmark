package locks;

import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Math.random;
import static java.lang.Thread.sleep;

public class Backoff implements MyLock {
    
    private final long DELAY = 100;
    AtomicBoolean state = new AtomicBoolean(false);
    
    @Override
    public void lock() throws InterruptedException {
        while (true) {
            while (state.get()) {}
            if(!state.getAndSet(true)) {
                return;
            }
            sleep((long) (random() * DELAY));
        }
    }

    @Override
    public void unlock() {
        state.set(false);
    }
}
