package locks;

import java.util.concurrent.atomic.AtomicReference;

public class MCSLock implements MyLock {

    public static class QNode {
        volatile boolean state = false;
        volatile QNode next = null;
    }

    AtomicReference<QNode> tail = new AtomicReference<>(new QNode());
    ThreadLocal<QNode> qNode = new ThreadLocal<>();

    @Override
    public void lock() {
        QNode qPred = tail.getAndSet(qNode.get());
        if (qPred != null) {
            qNode.get().state = true;
            qPred.next = qNode.get();
            while (qNode.get().state) {}
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
