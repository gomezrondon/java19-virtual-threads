package com.example.demo19;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

@SpringBootApplication
public class Demo19Application implements CommandLineRunner {
	private static final ReentrantLock LOCK = new ReentrantLock();
	public static void main(String[] args) {
		SpringApplication.run(Demo19Application.class, args);
	}

	@Override
	public void run(String... args)  {


		Random random= new Random();
		var lista = new ArrayList<Thread>();
		int max = 5;
		int min = 1;
		AtomicInteger atomicInteger = new AtomicInteger();
		Instant start = Instant.now();
		for (int i = 0; i < 1_000_000; i++) {

			Thread thread = Thread.ofVirtual().start(() -> {
/*				int randNum = random.nextInt(max - min + 1) + min;
				try {
					Thread.sleep(randNum * 1000);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}*/

				int count = atomicInteger.incrementAndGet();
				//	synchronized (atomicInteger){ // this is pinning :(
				printResource(count);
				//	}

			});
			lista.add(thread);
		}

		lista.forEach(x -> {
			try {
				x.join();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		});
		Instant end = Instant.now();
		System.out.println("total: "+atomicInteger.get());
		System.out.println("testing...");
		System.out.println("Execution time ms : "+Duration.between(start, end).toMillis());
	}

	private static void printResource(int count) {
		//lock guarantees sequential access without pinning :)
		LOCK.lock();
		try{
			if (count % 1_00_000 == 0) {
				System.out.println("adding..  "+ count);
			}
		}finally {
			LOCK.unlock();
		}

	}


}//end of class
