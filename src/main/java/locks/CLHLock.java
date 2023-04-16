package locks;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class CLHLock implements MyLock{

    public class QNode {
        AtomicBoolean state = new AtomicBoolean(false);
    }

    AtomicInteger counter = new AtomicInteger(0);

    AtomicReference<QNode> tail = new AtomicReference<QNode>(new QNode());
    ThreadLocal<QNode> myNode = new ThreadLocal<QNode>();
    ThreadLocal<QNode> myPred = new ThreadLocal<QNode>();

    @Override
    public void lock() throws InterruptedException {
        myNode.get().state.getAndSet(true);
        QNode pred = tail.getAndSet(myNode.get());
        myPred.set(pred);
        while (pred.state.get()) {}
        counter.getAndIncrement();
    }

    @Override
    public void unlock() {
        myNode.get().state.set(false);
        myNode.set(myPred.get());
    }
}
