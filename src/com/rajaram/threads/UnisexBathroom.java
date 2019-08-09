package com.rajaram.threads;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class UnisexBathroom {
	
	int numOfMen;
	int numOfWomen;
	int maxFree;
	
	
	public UnisexBathroom(int maxFreeSlots) {
		this.maxFree = maxFreeSlots;
	}
	
	ReentrantLock lock = new ReentrantLock();
	Condition waitToUse = lock.newCondition();
	
	public void useMale(String name) throws InterruptedException {
		lock.lock();
		while(numOfWomen > 0 ||  numOfMen == maxFree) {
			waitToUse.await();
		}
		numOfMen++;
		lock.unlock();
		System.out.println("Male in use by :" + name+ " Total men in room:" + numOfMen + " Number of women: "+ numOfWomen);
		Thread.sleep(1000);
		lock.lock();
		numOfMen--;
		waitToUse.signalAll();
		System.out.println("Men :" +name + " Left");
		lock.unlock();
		
	}
	
	public void useFemale(String name) throws InterruptedException {
		lock.lock();
		while(numOfMen > 0 ||  numOfWomen == maxFree) {
			waitToUse.await();
		}
		numOfWomen++;
		lock.unlock();
		System.out.println("Womens in use by :" + name +" Total Women in room:" + numOfWomen + " Men in room:" + numOfMen);
		Thread.sleep(1000);
		lock.lock();
		numOfWomen--;
		waitToUse.signalAll();
		System.out.println("Women :" +name + " Left");
		lock.unlock();
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		UnisexBathroom bath = new UnisexBathroom(3);
		Thread[] maleThread = new Thread[5];
		//int j =0;
		for(int i=0; i < 5; i++) {
			int j =i;
			 maleThread[i] = new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						//for(int i=0; i < 5; i++)
						bath.useMale("Male " + j);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
		Thread[] womenThread = new Thread[5];
		for(int i=0; i < 5; i++) {
			int j =i;
		 womenThread[i] = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					//for(int i=0; i < 5; i++)
					bath.useFemale("Women " + j);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		}
		for(int i=0; i< 5; i++)
			maleThread[i].start();
		for(int i=0; i< 5; i++)
			womenThread[i].start();
		for(int i=0; i< 5; i++)
			maleThread[i].join();
		for(int i=0; i< 5; i++)
			womenThread[i].join();
		
	}

}
