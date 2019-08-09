package com.rajaram.threads;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

 class CallBackHandler {
	
	PriorityQueue<CallBack> callBackQueue = new PriorityQueue<CallBack>(new Comparator<CallBack>() {

		@Override
		public int compare(CallBack o1, CallBack o2) {
			return (int) (o1.executeAt - o2.executeAt);
		}
	});
	
	ReentrantLock lock = new ReentrantLock();
	Condition wakeMeUp = lock.newCondition();
	
	public void executeCallBack() throws InterruptedException {
		while(true) {
			lock.lock();
			CallBack cback = callBackQueue.peek();
			long timeToExecute = 0;
			while(cback != null) {
				timeToExecute =   cback.executeAt - System.currentTimeMillis();
				
				if( timeToExecute <=0) {
					callBackQueue.poll();
					timeToExecute = 0;
					cback.execute();
					cback = callBackQueue.peek();
				} else {
					cback = null;
				}
				
			} 
			wakeMeUp.await(timeToExecute, TimeUnit.MILLISECONDS);
			
			lock.unlock();
			
			
		}
	}
	
	public void addToQueue(CallBack cback) {
		lock.lock();
		callBackQueue.offer(cback);
		wakeMeUp.signal();
		lock.unlock();
	}
	
	
	
	
	
	

}

 class CallBack {
	
	public long executeAt;
	
	String name;
	
	public CallBack(String name, long executeAt) {
		this.name = name;
		this.executeAt = executeAt;
	}
	public void execute() {
		System.out.println("Executed :" +name + " at : " + System.currentTimeMillis() + " original execution at " + executeAt + " ");
	}
}

  class CallBackDriver{
	public static void main(String[] args) throws InterruptedException {
		CallBackHandler handler = new CallBackHandler();
		Thread callBackExecutor = new Thread(new Runnable() {
			@Override
			public void run() {
				
				try {
					handler.executeCallBack();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		callBackExecutor.start();
		
		long earlyThreadTimer = System.currentTimeMillis();
		/*System.out.println("Sleep for 1 seconds");
		Thread.sleep(1000);*/
		Thread lateThread = new Thread(new Runnable() {
            public void run() {
                CallBack cb = new CallBack( "Hello this is late thread", System.currentTimeMillis() + 3000);
                handler.addToQueue(cb);
            }
        });
        lateThread.start();

        //Thread.sleep(1000);

        Thread earlyThread = new Thread(new Runnable() {
            public void run() {
                CallBack cb = new CallBack( "Hello this is early thread", earlyThreadTimer);
                handler.addToQueue(cb);
            }
        });
        earlyThread.start();
        
        lateThread.join();
        earlyThread.join();
	}
}
