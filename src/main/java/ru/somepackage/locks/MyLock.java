package ru.somepackage.locks;

public interface MyLock {

    void lock() throws InterruptedException;

    void unlock();
}
