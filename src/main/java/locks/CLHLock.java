package locks;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class CLHLock implements MyLock{

    public static class QNode {
        AtomicBoolean state = new AtomicBoolean(false);
    }


    AtomicReference<QNode> tail = new AtomicReference<>(new QNode());
    ThreadLocal<QNode> myNode = new ThreadLocal<>();
    ThreadLocal<QNode> myPred = new ThreadLocal<>();

    @Override
    public void lock(){
        myNode.get().state.getAndSet(true);
        QNode pred = tail.getAndSet(myNode.get());
        myPred.set(pred);
        while (pred.state.get()) {}
    }

    @Override
    public void unlock() {
        myNode.get().state.set(false);
        myNode.set(myPred.get());
    }
}
