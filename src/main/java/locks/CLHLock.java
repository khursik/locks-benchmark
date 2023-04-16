package locks;

import javax.xml.namespace.QName;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class CLHLock implements MyLock{

    public class QNode {
        AtomicBoolean state = new AtomicBoolean(false);
    }

    AtomicReference<QNode> tail = new AtomicReference<QNode>(new QNode());
    ThreadLocal<QNode> myNode = new ThreadLocal<QNode>();
    ThreadLocal<QNode> myPred = new ThreadLocal<QNode>();

    @Override
    public void lock() throws InterruptedException {
        myNode.get().state.getAndSet(true);
        QNode pred = tail.getAndSet(myNode.get());
        myPred.set(pred);
        while (!pred.state.get()) {}
    }

    @Override
    public void unlock() {
        myNode.get().state.set(false);
        myNode.set(myPred.get());
    }
}
