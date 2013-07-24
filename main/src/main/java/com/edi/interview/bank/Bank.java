package com.edi.interview.bank;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Bank {

    private boolean closed = false;
    private int maxTicketNum = 100;
    private int waitThreshold = 2;
    private int maxServiceWindows = 3;
    private int currentServiceWindows = 0;
    private int currentTicketNum;
    
    private ServiceWindows sw = null;
    private MyThreadGroup threadGroup = null;
    
    public void getTicketAndWait()
    {
        currentTicketNum++;
        if(!closed && (currentTicketNum>maxTicketNum))
        {
            System.out.println("Reach maximum workload, will close");
            this.close();
            return;
        }
        ServiceWindows.waitingQueue.offer(new Integer(currentTicketNum));
        if(currentServiceWindows<maxServiceWindows && currentTicketNum>waitThreshold*currentServiceWindows)
        {
            currentServiceWindows++;
            new Thread(threadGroup, sw, "Windows"+currentServiceWindows).start();
        }
        this.threadGroup.notifyAllThread();
    }
    
    public Bank(int maxTicketNum) {
        super();
        this.maxTicketNum = maxTicketNum;
    }
    
    public Bank() {
        super();
    }
    
    public void open()
    {
        currentServiceWindows++;
        threadGroup = new MyThreadGroup("Bank Windows Group");
        this.sw = new ServiceWindows(this);
        new Thread(threadGroup, sw, "Windows"+currentServiceWindows).start();
        
    }
    
    public MyThreadGroup getThreadGroup() {
        return threadGroup;
    }
    
    public void close()
    {
        closed = true;
        while(true)
        {
            if(ServiceWindows.waitingQueue.size()==0 && this.threadGroup.isAwaiting())
            {
                threadGroup.notifyAllThread();
                //System.out.println("Alive threads: " + threadGroup.activeCount());
                if(threadGroup.activeCount()==0)
                {
                    System.out.println("Bank is closed.");
                    return;
                }
            }
        }
    }
    
    public boolean isClosed() {
        return closed;
    }

    class MyThreadGroup extends ThreadGroup
    {
        Map<String, Object> lockMap = new ConcurrentHashMap<String, Object>();

        public MyThreadGroup(String name) {
            super(name);
        }
        
        public void await(String name)
        {
            Object lock = new Object();
            lockMap.put(name, lock);
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        
        public void notifyAllThread()
        {
            for(Object obj:lockMap.values())
            {
                synchronized (obj) {
                    obj.notify();
                }
            }
        }
        
        public boolean isAwaiting()
        {
            //System.out.println("Map size: " + lockMap.size());
            if(lockMap.size()==currentServiceWindows)
            {
                return true;
            }
            return false;
        }
    }

    public static void main(String[] args) {
        Bank b = new Bank();
        b.open();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i=0;i<30;i++)
        {
            b.getTicketAndWait();
        }
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i=0;i<99;i++)
        {
            b.getTicketAndWait();
        }
    }
    
    
    
}
