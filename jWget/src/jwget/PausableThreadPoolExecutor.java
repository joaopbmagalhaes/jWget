/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Isaac
 */
public class PausableThreadPoolExecutor extends ThreadPoolExecutor {

    private boolean isPaused;
    private final ReentrantLock pauseLock = new ReentrantLock();
    private final Condition unpaused = pauseLock.newCondition();
    private static AtomicInteger countLinks;  // Counter of the number of links to be downloaded

    public PausableThreadPoolExecutor(int corePoolSize,
            int maximumPoolSize,
            long keepAliveTime,
            TimeUnit unit,
            BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        PausableThreadPoolExecutor.countLinks = new AtomicInteger(0);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        // Try to obtain the lock - The lock is obtained 
        // sucessfully if no other Thread as ownership over 
        // the lock. The method lock() will return immediately
        // if the current Thread already owns the lock
        pauseLock.lock();
        try {
            while (isPaused) {
                // Wait until isPaused is set to False and the 
                // unpaused Condition is sign to proceed
                unpaused.await();
            }
        } catch (InterruptedException ie) {
            t.interrupt();
        } finally {
            pauseLock.unlock();
        }
    }

    private static int incrementCountLinks() {
        return countLinks.incrementAndGet();
    }

    private static int decrementCountLinks() {
        return countLinks.decrementAndGet();
    }

    public static int getCountLinks() {
        return countLinks.get();
    }

    @Override
    public void execute(Runnable command) {
        int cont = incrementCountLinks();
        Downloader d = (Downloader) command;
        System.out.println("Added new Task: " + cont);
        System.out.println("\t\t\t Resource: " + d.getWebfile().getUrl());
        super.execute(command);
    }

    /**
     * This method is responsible for pausing the download process. This method
     * first adquires the pauseLock lock and then sets the isPaused to true so
     * that all the Threads await until the process is resumed
     */
    public void pause() {
        pauseLock.lock();
        try {
            isPaused = true;
        } finally {
            pauseLock.unlock();
        }
    }

    /**
     * This method is responsible for resuming the download process. This method
     * first adquires the pauseLock lock and then calls signalAll to unlock all
     * Threads awaiting
     */
    public void resume() {
        pauseLock.lock();
        try {
            isPaused = false;
            unpaused.signalAll();
        } finally {
            pauseLock.unlock();
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        try {
            int cont = decrementCountLinks();
            System.out.println("Task Finalyzed: " + cont);
            Downloader d = (Downloader) r;
            System.out.println("\t\t\t Resource: " + d.getWebfile().getUrl());
            if (cont == 0) {
                System.out.println("######################## ##### ### TERMINOU  ### #######      #################################");
                jWget.returnResult("Success");
            }

        } finally {

            super.afterExecute(r, t);
        }
    }
}
