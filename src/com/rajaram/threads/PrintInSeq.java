package com.rajaram.threads;

public class PrintInSeq {

	int lastPrinted = 0;

	public synchronized void printInSeq(int seq) throws InterruptedException {
		while (seq-1 != lastPrinted) {
			wait();
		}
		System.out.println(seq);
		lastPrinted = seq;
		notifyAll();
	}
	
	

	public static void main(String[] args) throws InterruptedException {
		PrintInSeq seq = new PrintInSeq();
		Thread r1 = new Thread(new ThreadRunner(seq, 1, 3));
		Thread r2 = new Thread(new ThreadRunner(seq, 2, 3));
		Thread r3 = new Thread(new ThreadRunner(seq, 3, 3));
		
		r1.start();
		r2.start();
		r3.start();
		r1.join();
		r2.join();
		r3.join();

	}
}

class ThreadRunner implements Runnable {
	
	private PrintInSeq printer = null;
	int startSeq = 0;
	int threads = 0;
	
	public ThreadRunner(PrintInSeq printSeq, int startSeq, int threads) {
		this.printer = printSeq;
		this.startSeq = startSeq;
		this.threads = threads;
		
	}
	@Override
	public void run() {
		while(startSeq < 100) {
			try {
				printer.printInSeq(startSeq);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			startSeq+= threads;
		}
		
		
	}
}
