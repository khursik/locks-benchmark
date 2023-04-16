package locks;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class MCSLock implements MyLock {

    public class QNode {
        volatile boolean state = false;
        volatile QNode next = null;
    }

    AtomicInteger counter = new AtomicInteger(0);

    AtomicReference<QNode> tail = new AtomicReference<QNode>(new QNode());
    ThreadLocal<QNode> qNode = new ThreadLocal<>();

    @Override
    public void lock() throws InterruptedException {
        QNode qPred = tail.getAndSet(qNode.get());
        if (qPred != null) {
            qNode.get().state = true;
            qPred.next = qNode.get();
            while (qNode.get().state) {}
            counter.getAndIncrement();
        }
    }

    @Override
    public void unlock() {
        if (qNode.get().next == null) {
            if (tail.compareAndSet(qNode.get(), null)) {
                return;
            }
            while (qNode.get().next == null) {}
        }
        qNode.get().next.state = false;
    }
}
