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
    private ReentrantLock pauseLock = new ReentrantLock();
    private Condition unpaused = pauseLock.newCondition();
    private static AtomicInteger countLinks = new AtomicInteger(0);  // Counter of the number of links to be downloaded

    public PausableThreadPoolExecutor(int corePoolSize,
            int maximumPoolSize,
            long keepAliveTime,
            TimeUnit unit,
            BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        pauseLock.lock();
        try {
            while (isPaused) {
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
        System.out.println("Added new Task: " + cont);
        super.execute(command);
    }

    public void pause() {
        pauseLock.lock();
        try {
            isPaused = true;
        } finally {
            pauseLock.unlock();
        }
    }

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
            if (cont == 0) {
                System.out.println("######################## ##### ### TERMINOU  ### #######      #################################");
                jWget.returnResult("Success");
            }

        } finally {
            jWget.returnResult("ENVIADO ----------------- TESTE ---::::: ");
            super.afterExecute(r, t);
        }
    }
}
