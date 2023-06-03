package ru.jmh.locks;

public interface MyLock {

    void lock() throws InterruptedException;

    void unlock();
}
