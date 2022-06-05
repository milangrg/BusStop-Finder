package BusFinder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

public class UrlText {
	private String urlAddress;
	private String urlText;

	// constructor
	public UrlText(String url) {
		this.urlAddress = url;
		try {
			processUrl(urlAddress); // private method call
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// pre : requires a string URL as parameter
	// post: creates a connection to the URL and returns a string of the HTML data
	private void processUrl(String url) throws Exception {
		URLConnection ct = new URL(url).openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(ct.getInputStream()));
		String inputLine = "";

		String text = "";
		while ((inputLine = in.readLine()) != null) {
		    text += inputLine + "\n";
		}
		//System.out.println(text);
		in.close();
		this.urlText = text;
	}
	
	// getter for urlText
	public String getUrlText() {
		return urlText;
	}
	
}