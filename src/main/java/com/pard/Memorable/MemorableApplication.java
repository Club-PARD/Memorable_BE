package com.pard.Memorable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class MemorableApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemorableApplication.class, args);
//		List<String> arr = new ArrayList<>();
//		for(int i=0;i<5;i++)
//			arr.add(String.valueOf('A'+i));
//		for(String str : arr)
//			System.out.println(str);
	}

}
