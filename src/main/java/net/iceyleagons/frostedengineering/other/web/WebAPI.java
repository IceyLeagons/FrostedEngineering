package net.iceyleagons.frostedengineering.other.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

public class WebAPI {

	public static List<String> getAvailableLanguages() {
		try {
			Response s = getResponseFromWeb("https://api.iceyleagons.net/?p=fengineering&f=lang","function=lang&plugin=frostedengineering&data=*");
			s.getContent().forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Response getResponseFromWeb(String u) throws IOException {

		HttpsURLConnection c = (HttpsURLConnection) new URL(u).openConnection();

		c.setRequestMethod("GET");
		c.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

		c.setRequestProperty("Content-Type", "application/json");
		c.setDoInput(true);
		c.setDoOutput(true);


		int responseCode = c.getResponseCode();
		c.connect();

		BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));

		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}

		JSONObject obj = new JSONObject(sb.toString());

		String respcode = obj.getString("response_code");
		String resp = obj.getString("response");
		JSONArray a = obj.getJSONArray("content");

		return new Response(String.valueOf(respcode != null ? respcode : responseCode), resp, a);
	}

	public static Response getResponseFromWeb(String u, String param) throws IOException {

		HttpsURLConnection c = (HttpsURLConnection) new URL(u).openConnection();

		c.setRequestMethod("GET");
		c.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

		c.setRequestProperty("Content-Type", "application/json");
		c.setDoInput(true);
		c.setDoOutput(true);
		c.setFixedLengthStreamingMode(param.getBytes().length);
		PrintWriter out = new PrintWriter(c.getOutputStream());
		out.print(param);
		out.close();


		int responseCode = c.getResponseCode();
		c.connect();

		BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));

		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}

		JSONObject obj = new JSONObject(sb.toString());

		String respcode = obj.getString("response_code");
		String resp = obj.getString("response");
		JSONArray a = obj.getJSONArray("content");

		return new Response(String.valueOf(respcode != null ? respcode : responseCode), resp, a);
	}

}
