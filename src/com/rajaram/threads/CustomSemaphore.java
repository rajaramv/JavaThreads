package com.rajaram.threads;

public class CustomSemaphore {
	
	public int maxPermit;
	public int usedPermits;
	
	public CustomSemaphore(int maxPermits) {
		this.maxPermit = maxPermits;
	}
	
	
	public synchronized void acquire() throws InterruptedException{
		while(usedPermits == maxPermit) {
				wait();
		}
		
		usedPermits ++;
		
	}
	
	public synchronized void release() {
		usedPermits--;
		notify();
	}

}
