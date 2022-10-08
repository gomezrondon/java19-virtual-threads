package com.example.demo19;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class Demo19Application implements CommandLineRunner {

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

		for (int i = 0; i < 1_000_000; i++) {

			Thread thread = Thread.ofVirtual().start(() -> {
				int randNum = random.nextInt(max - min + 1) + min;
				try {
					Thread.sleep(randNum * 1000);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}

				atomicInteger.incrementAndGet();
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
		System.out.println("total: "+atomicInteger.get());
		System.out.println("testing...");
	}
}
