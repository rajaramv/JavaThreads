package com.rajaram.threads;

public class Barrier {
	
	int arrived =0;
	int maxCount;
	int released = 0;
	
	public Barrier(int maxCount) {
		this.maxCount = maxCount;
	}
	
	public synchronized void await() throws InterruptedException {
		
		while(arrived == maxCount) 
			wait();
		if(arrived++ == maxCount) {
			notifyAll();
			released = maxCount;
		} else {
			wait();
		}
		released--;
		if(released ==0) {
			arrived = 0;
			notifyAll();
		}
		
	}

}
