package io.pusteblume.nexthink;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Q3SafeLock {
    private Lock lock = new ReentrantLock();

    public void lock() throws InterruptedException {
        LockingManager.registerLock(this);
        long timeout = 1000;
        while (!lock.tryLock(timeout, TimeUnit.MILLISECONDS)) {
            if (LockingManager.checkForCircularDependency()) {
                throw new ThreadLockException();
            }
        }
    }

    public void unlock() {
        LockingManager.unregisterLock(this);
        lock.unlock();
    }

    public static class LockingManager {
        final private static Map<Thread, Map<Q3SafeLock, Integer>> threadsToLocks = new HashMap();

        public static void registerLock(Q3SafeLock lock) {
            synchronized (threadsToLocks) {
                if (!threadsToLocks.containsKey(Thread.currentThread())) {
                    threadsToLocks.put(Thread.currentThread(), new HashMap() {{
                        put(lock, 0);
                    }});
                }
                if (!threadsToLocks.get(Thread.currentThread()).containsKey(lock))
                    threadsToLocks.get(Thread.currentThread()).put(lock, 0);

                final Integer count = threadsToLocks.get(Thread.currentThread()).get(lock);
                threadsToLocks.get(Thread.currentThread()).put(lock, count + 1);
            }

        }

        public static void unregisterLock(Q3SafeLock lock) {
            synchronized (threadsToLocks) {
                if (threadsToLocks.containsKey(Thread.currentThread())
                        && threadsToLocks.get(Thread.currentThread()).containsKey(lock)) {
                    final Integer count = threadsToLocks.get(Thread.currentThread()).get(lock);
                    if (count == 1)
                        threadsToLocks.get(Thread.currentThread()).remove(lock, count + 1);
                    else
                        threadsToLocks.get(Thread.currentThread()).put(lock, count - 1);
                }
            }
        }

        public static boolean checkForCircularDependency() {
            synchronized (threadsToLocks) {
                final Map<Q3SafeLock, Set<Thread>> locksToThreads = new HashMap<>();
                for (Thread thread : threadsToLocks.keySet()) {
                    for (Q3SafeLock lock : threadsToLocks.get(thread).keySet()) {
                        if (!locksToThreads.containsKey(lock))
                            locksToThreads.put(lock, new HashSet<>());
                        locksToThreads.get(lock).add(thread);
                    }
                }

                final Map<Thread, Set<Thread>> graph = new HashMap<>();
                for (Thread thread : threadsToLocks.keySet()) {
                    if (!graph.containsKey(thread))
                        graph.put(thread, new HashSet<>());
                    for (Q3SafeLock lock : threadsToLocks.get(thread).keySet()) {
                        graph.get(thread).addAll(locksToThreads.get(lock).stream().filter(t -> t != thread).collect(Collectors.toSet()));
                    }
                }

                final Queue<Thread> queue = new LinkedList<>();
                queue.addAll(graph.get(Thread.currentThread()));
                final Set<Thread> visited = new HashSet<>();
                while (!queue.isEmpty()) {
                    Thread t = queue.remove();
                    if (!visited.contains(t)) {
                        if (t == Thread.currentThread()) {
                            return true;
                        } else {
                            queue.addAll(graph.get(t));
                        }
                    }
                }
                return false;
            }
        }
    }

    public static class ThreadLockException extends RuntimeException {
    }

    public static abstract class LockAcquirer implements Runnable {
        Q3SafeLock l1;
        Q3SafeLock l2;

        abstract public void lockAction() throws InterruptedException;

        LockAcquirer(Q3SafeLock l1, Q3SafeLock l2) {
            this.l1 = l1;
            this.l2 = l2;
        }

        private boolean tryToLock() {
            try {
                lockAction();
                System.out.println("Thread " + Thread.currentThread().getName() + "locking success");
                return true;
            } catch (Throwable e) {
                System.out.println("Thread " + Thread.currentThread().getName() + "failed to lock: ");
                e.printStackTrace();
                return false;
            } finally {
                try {
                    l1.unlock();
                } catch (Throwable e) {
                }
                try {
                    l2.unlock();
                } catch (Throwable e) {
                }
            }
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                if (tryToLock())
                    break;
            }

        }
    }

    public static void main(String... args) throws InterruptedException {
        Q3SafeLock l1 = new Q3SafeLock();
        Q3SafeLock l2 = new Q3SafeLock();
        Thread t1 = new Thread(new LockAcquirer(l1, l2) {
            public void lockAction() throws InterruptedException {
                l1.lock();
                Thread.sleep(50);
                l2.lock();
            }
        }, "t1");

        Thread t2 = new Thread(new LockAcquirer(l1, l2) {
            public void lockAction() throws InterruptedException {
                l2.lock();
                Thread.sleep(100);
                l1.lock();
            }
        }, "t2");


        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

}