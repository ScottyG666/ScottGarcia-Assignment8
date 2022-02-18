package AsynchAssignment.src.com.coderscampus.assignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Assignment8Appli {


	private static Assignment8 listOfIntegersFromFile = new Assignment8();
	private static List<Integer> listOfAllNumbers =  Collections.synchronizedList(new ArrayList<>());
	
	
	public static void main(String[] args) throws InterruptedException {

		ExecutorService pool = Executors.newCachedThreadPool();
				
		List<CompletableFuture<Object>> tasks = new ArrayList<>();
		
		for(int i = 0; i < 1000; i++) {
			
			CompletableFuture<Object> task =  CompletableFuture.supplyAsync(() -> listOfIntegersFromFile.getNumbers() , pool)
																									  .thenApplyAsync(completeList -> listOfAllNumbers.addAll(completeList));
			tasks.add(task);
		}
		
		while (tasks.stream().filter(t ->  t.isDone()).count() < 1000) {
			Thread.sleep(1000);
		}
		

		 Map<Integer, Integer> output;	

		output = listOfAllNumbers.stream()
				  .collect(Collectors.toMap(i -> i, i -> 1, (oldValue, newValue) -> oldValue + 1));
		System.out.println(output);
	}

}
