package com.coderscampus.assignment;

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
	
	//Here a *Thread Safe* ArrayList is being assigned for the synListOfAllNumbers so that as the Multiple threads
	//try to access the *add()* method, they are forced to access them synchronously
	private static List<Integer> synListOfAllNumbers =  Collections.synchronizedList(new ArrayList<>());
	private static Integer totalIterationsForCallingGetNumbers = 1000;
	
	
	public static void main(String[] args) throws InterruptedException {
		
		ExecutorService cachedPoolOfThreads = Executors.newCachedThreadPool();
				
		//This list is going to be checked for completeness before running the final mapping operation, so we can ensure 
		//ALL promises have been complete, and All data is available to the main thread
		List<CompletableFuture<Object>> listOfPromisesToAddPulledDataToListOfAllNumbers = new ArrayList<>();
		
		for(int i = 0; i < totalIterationsForCallingGetNumbers; i++) {
			
			//for each iteration, the promise to call .getNumbers() from assignment 8, and then to add those numbers to the 
			//ListOfAllNumbers is made, using an available thread from the cachedPoolOfThreads
			CompletableFuture<Object> promiseToAddPulledDataToListOfAllNumbers =  CompletableFuture.supplyAsync(() -> listOfIntegersFromFile.getNumbers() , cachedPoolOfThreads)
																									  .thenApplyAsync(completeList -> synListOfAllNumbers.addAll(completeList));
			
			//This adds the task to be completed in the future (pulling the ArrayList of numbers from Assignment8.getNumbers())
			// to the list of tasks able to complete in the future
			listOfPromisesToAddPulledDataToListOfAllNumbers.add(promiseToAddPulledDataToListOfAllNumbers);
		}
		
		//Checking that all promised operations have been completed before allowing the main thread to continue 
		//to the last operation
		while (listOfPromisesToAddPulledDataToListOfAllNumbers.stream().filter(promisedOperation ->  promisedOperation.isDone()).count() < totalIterationsForCallingGetNumbers) {
			
			
			Thread.sleep(500);
		}
		

		 Map<Integer, Integer> recordOfEachIntegerOccurrence;	

		recordOfEachIntegerOccurrence = synListOfAllNumbers.stream()
				  .collect(Collectors.toMap(i -> i, i -> 1, (existing, replacement) -> existing + 1));
		System.out.println(recordOfEachIntegerOccurrence);
	}

}
