package ru.benchmark.locks;

public interface MyLock {

    void lock() throws InterruptedException;

    void unlock();
}
