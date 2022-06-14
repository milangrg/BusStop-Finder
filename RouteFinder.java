package BusFinder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;

public class RouteFinder implements IRouteFinder {
	
	private Map<String, Map<String, String>> cityMap; // example: <Brier, <111, http://....>
	private Map<String, LinkedHashMap<String, String>> routeStopsMap;
	private ArrayList<String> cities;
	private String busUrl;
	
	// constructor
	public RouteFinder() {
		this.cityMap = new HashMap<>();
		this.routeStopsMap = new HashMap<>();
		this.cities = new ArrayList<>();
		this.busUrl = "";
	}
	
	// pre : requires a string of an URL
	// post: returns the html text in string format for the specified URL
	private String getUrlText(String urlString) {
		UrlText urlText = new UrlText(urlString); // creates new UrlText object
		return urlText.getUrlText();
	}
	
	// pre : requires an initial letter of a city name as parameter, ignores case sensitivity
	// post: returns a map of all the bus routes for city(s) beginning with the given initial
	public Map<String, Map<String, String>> getBusRoutesUrls(final char destInitial) {
		String i = Character.toString(destInitial).toUpperCase();
		char DestInitial = i.charAt(0);
		checkAlpha(DestInitial); // calls the private helper method
		return cityMap;
	}

	// pre : requires a valid URL string for a bus route schedule as parameter
	// post: returns a map of all the ordered stops for that bus route both to & from destination
	public Map<String, LinkedHashMap<String, String>> getRouteStops(final String url) {
		getStopsMap(url); // calls the private helper method
		return routeStopsMap;
	}
	
	// post: private helper method to validate the user input choices of destination and route id,
	//		 throws RuntiemException when route ID and destination is not found
	public void createBusUrl() {
		try {
			String destination = userCity();
			boolean foundCity = false;
			boolean foundBus = false;
			for (int i = 0; i < cities.size(); i++) {
				String city = cities.get(i);
				if (city.equalsIgnoreCase(destination)) { // ignores case sensitivity
					foundCity = true;
					destination = cities.get(i);
				}
			}	
			if (!foundCity) {
				System.out.println("Invalid Destination!");
				throw new RuntimeException();
			} else {
				String busNum = userBusNum();
				for (String bus : cityMap.get(destination).keySet()) {
					if (bus.equalsIgnoreCase(busNum)) { // ignores case sensitivity
						foundBus = true;
						busNum = bus;
						break;
					}
				}
				this.busUrl = cityMap.get(destination).get(busNum);
			}
			if (!foundBus) {
				System.out.println("Invalid Route ID!");
				throw new RuntimeException();
			}
		} catch (RuntimeException e) {
			createBusUrl();
		}
	}
	
	// post: private helper method to get city destination from user input
	private String userCity() {
		Scanner scan = new Scanner(System.in);
		System.out.print("Please enter your destination: ");
		String destination = scan.nextLine();
		return destination;
	}
	
	// post: private helper method to get route id from user input
	private String userBusNum() {
		Scanner scan = new Scanner(System.in);
		System.out.print("Please enter a route: ");
		String busNum = scan.nextLine();
		return busNum;
	}
	
	// getter method for the specific bus route URL based on user input choice
	public String getBusUrl() {
		return busUrl;
	}
	
	// post: private helper method for getBusRoutesUrls to extract data
	private void getCityMap(final char destInitial) {
		String text = getUrlText(TRANSIT_WEB_URL);
		String regex = "<h3>(" + destInitial + "(.*?))<\\/h3>[^$]*?<(hr|div) id.*>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		
		while (matcher.find()) {
			String city = matcher.group(1);
			cities.add(city);
			
			Pattern routes = Pattern.compile("<strong><a href=\"(.*)\".*>(.*)</a></strong>");
			Matcher routesMatcher = routes.matcher(matcher.group());
			
			Map<String, String> route = new HashMap<>();
			
			while (routesMatcher.find()) {
				String routeURL = TRANSIT_WEB_URL + routesMatcher.group(1).substring(11);
				route.put(routesMatcher.group(2), routeURL);	
			}
			cityMap.put(city, route);
		}
	}
	
	// post: private helper method for getRouteStops to extract data
	private void getStopsMap(final String url) {
		String text = getUrlText(url);
		String regex = "<h2>Weekday<small>(.*?)</small>[^$]*?</thead>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		
		while (matcher.find()) {
			String destination = matcher.group(1);
			
			Pattern stops = Pattern.compile("<strong class.*?>(.*?)</strong>[^$]*?<p>(.*?)</p>");
			Matcher stopsMatcher = stops.matcher(matcher.group());
			
			LinkedHashMap<String, String> stopPair = new LinkedHashMap<>();
			
			while (stopsMatcher.find()) {
				stopPair.put(stopsMatcher.group(1), stopsMatcher.group(2));	
			}
			routeStopsMap.put(destination, stopPair);
		}
	}
	
	// post: private helper method to check if the char entry is an alphabet
	private void checkAlpha(char c) throws RuntimeException {
		try {
			int ascii = (int) c;
			if (ascii < 65 | ascii > 90) {
				throw new RuntimeException();
			}
			getCityMap(c);
		} catch (RuntimeException e) {
			System.out.println("Invalid letter alphabet!");
			System.out.print("Please enter a letter that your destinations start with ");
			Scanner scan = new Scanner(System.in);
			char alphabet = scan.next().charAt(0);
			this.getBusRoutesUrls(alphabet);
			scan.close();
		}
	}
	
}
