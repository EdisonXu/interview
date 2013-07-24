package com.edi.interview.bank;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServiceWindows implements Runnable {

    
    public static Queue<Integer> waitingQueue = new ConcurrentLinkedQueue<Integer>(); 
    private Bank bank;
    
    
    public ServiceWindows(Bank bank) {
        super();
        this.bank = bank;
    }

    public void run() {
        Integer current = null;
        while(true)
        {
            do
            {
                current = waitingQueue.poll();
                if(current==null){
                    System.out.println(Thread.currentThread().getName() + " is waiting task");
                    bank.getThreadGroup().await(Thread.currentThread().getName());
                    if(bank.isClosed())
                    {
                        //Thread.currentThread().interrupt();
                        return;
                    }
                }
            }while(current == null);
            
            System.out.println(Thread.currentThread().getName() + " is handing request " + current);
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
