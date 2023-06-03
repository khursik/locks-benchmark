package ru.jmh.locks;

import java.util.concurrent.atomic.AtomicReference;

public class CLHLock implements MyLock{

    public static class QNode {
        private volatile boolean locked = false;
    }

    private final ThreadLocal<QNode> myPred = ThreadLocal.withInitial(() -> null);
    private final ThreadLocal<QNode> myNode = ThreadLocal.withInitial(QNode::new);
    static final AtomicReference<QNode> tail = new AtomicReference<>(new QNode());

    @Override
    public void lock() {
        final QNode node = this.myNode.get();
        node.locked = true;

        QNode pre = tail.getAndSet(node);
        this.myPred.set(pre);
        while (this.myPred.get().locked) {}
    }

    @Override
    public void unlock() {
        final QNode node = this.myNode.get();
        node.locked = false;
        this.myNode.set(this.myPred.get());
    }
}
