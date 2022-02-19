import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;

import com.coderscampus.assignment.Assignment8;

public class test {

	private Assignment8 listOfIntegersFromFile = new Assignment8();
	private List<Integer> x =  Collections.synchronizedList(new ArrayList<>());
	private static Integer totalIterationsForCallingMethod = 1000;

	@Test
	public void should_create_list_of_1000000_numbers () throws InterruptedException {

		ExecutorService cachedPoolOfThreads = Executors.newCachedThreadPool();
				
		
		List<CompletableFuture<Object>> listOfPromisesToAddPulledDataToListOfAllNumbers = new ArrayList<>();
		
		for(int i = 0; i < totalIterationsForCallingMethod; i++) {
		
			CompletableFuture<Object> promiseToAddPulledDataToListOfAllNumbers =  CompletableFuture.supplyAsync(() -> listOfIntegersFromFile.getNumbers() , cachedPoolOfThreads)
																									  .thenApplyAsync(completeList -> x.addAll(completeList));
		
			listOfPromisesToAddPulledDataToListOfAllNumbers.add(promiseToAddPulledDataToListOfAllNumbers);
		}
		
		while (listOfPromisesToAddPulledDataToListOfAllNumbers.stream().filter(promisedOperation ->  promisedOperation.isDone()).count() < totalIterationsForCallingMethod) {
			Thread.sleep(500);
		}
		
		assertEquals(x.size(), 1000000);
	}
}
