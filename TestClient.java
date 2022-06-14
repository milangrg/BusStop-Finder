package BusFinder;

import java.util.Map;
import java.util.Scanner;
import java.util.LinkedHashMap;

public class TestClient {

	public static void main(String[] args) {
		
		Scanner input = new Scanner(System.in);
		boolean stop = false;
		
		while(!stop) {
			Scanner scan = new Scanner(System.in);

			System.out.print("Please enter a letter that your destinations start with ");
			char alphabet = scan.next().charAt(0);
			
			RouteFinder rf = new RouteFinder();
			Map<String, Map<String, String>> result = rf.getBusRoutesUrls(alphabet);
			printCityMap(result);
	
			rf.createBusUrl();
		
			Map<String, LinkedHashMap<String, String>> result2 = rf.getRouteStops(rf.getBusUrl());
			printStopsMap(result2);
			
			System.out.println("Do you want to check different destination? "
					+ "Please type y to continue or press any other key to exit");
		
			char ch = input.next().charAt(0);
			if (ch == 'y') {
				stop = true;
			}
		}
	}
	
	public static void printCityMap(Map<String, Map<String, String>> cityMap) {
		for (String key : cityMap.keySet()) {
			System.out.println("Destination: " + key);
			for (String key2 : cityMap.get(key).keySet()) {
				System.out.println("Bus Number: " + key2);
			}
			System.out.println("+++++++++++++++++++++++++++++++++++");
		}
	}
	
	public static void printStopsMap(Map<String, LinkedHashMap<String, String>> stopsMap) {
		System.out.println();
		for (String key : stopsMap.keySet()) {
			System.out.println("Destination: " + key);
			for (String key2 : stopsMap.get(key).keySet()) {
				System.out.println("Stop number: " + key2 + " is " + stopsMap.get(key).get(key2));
			}
		}
		System.out.println();
	}

}