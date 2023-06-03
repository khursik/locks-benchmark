package ru.jmh.locks;

import java.util.concurrent.atomic.AtomicReference;

public class MCSLock implements MyLock {

    private static class QNode {
        private volatile boolean state = false;
        private volatile QNode next = null;
    }

    private final AtomicReference<QNode> tail = new AtomicReference<>();
    private final ThreadLocal<QNode> node = ThreadLocal.withInitial(QNode::new);

    @Override
    public void lock() {
        QNode qNode = this.node.get();
        qNode.state = true;
        QNode pred = tail.getAndSet(qNode);

        if (pred != null) {
            pred.next = qNode;
            while (qNode.state) {}
        }
    }

    @Override
    public void unlock() {
        QNode node = this.node.get();
        if (node.next == null) {
            if (tail.compareAndSet(node, null)) {
                return;
            }
            while (node.next == null) {}
        }
        node.next.state = false;
        node.next = null;
    }
}
