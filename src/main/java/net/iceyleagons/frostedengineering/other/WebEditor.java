package net.iceyleagons.frostedengineering.other;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.fasterxml.jackson.databind.ObjectMapper;

public class WebEditor {
	
	/*
	public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
		String id = getIDFromWeb("https://api.iceyleagons.net/","asd","asdf1234").getContent();
		System.out.println(id);
		String file = getFile("https://api.iceyleagons.net/",id,"asdf1234").getContent();
		System.out.println(new String(Base64.getDecoder().decode(file)));
		
		
	}
	*/
	@SuppressWarnings("unused")
	private static Resp getIDFromWeb(String u, String data, String key) throws IOException, InterruptedException, URISyntaxException {

		Map<String,String> values = new HashMap<String,String>() {/**
			 * 
			 */
			private static final long serialVersionUID = -968389473882250090L;

		{
			put("function","editor");
			put("data",data);
			put("key",key);
		}};
		ObjectMapper m = new ObjectMapper();
		String request = m.writeValueAsString(values);
		
		URIBuilder b = new URIBuilder(u);
		b.setParameter("function", "editor").setParameter("data",data).setParameter("key", key);
		
		HttpClient cc = HttpClient.newHttpClient();
		HttpRequest req = HttpRequest.newBuilder()
				.uri(b.build())
				.POST(HttpRequest.BodyPublishers.ofString(request))
				.build();
		
		HttpResponse<String> resp = cc.send(req, HttpResponse.BodyHandlers.ofString());
		JSONObject obj = new JSONObject(new JSONTokener(resp.body()));


		String respcode = obj.getString("response_code");
		String response = obj.getString("response");
		String a = obj.getString("content");

		return new Resp(respcode, response, a);
	}
	
	@SuppressWarnings("unused")
	private static Resp getFile(String u,String id,String key) throws IOException, InterruptedException, URISyntaxException {
		
		HttpClient cc = HttpClient.newHttpClient();
		URIBuilder b = new URIBuilder(u);
		b.setParameter("function", "editor").setParameter("id",id).setParameter("key", key);
		
		HttpRequest req = HttpRequest.newBuilder()
				.uri(b.build())
				.GET()
				.build();
		
		HttpResponse<String> resp = cc.send(req, HttpResponse.BodyHandlers.ofString());
		JSONObject obj = new JSONObject(new JSONTokener(resp.body()));


		String respcode = obj.getString("response_code");
		String response = obj.getString("response");
		String a = obj.getString("content");

		return new Resp(respcode, response, a);
	}

}
class Resp {

	private String response_code;
	private String response;
	private String content;

	public Resp(String response_code, String response, String content) {
		this.response_code = response_code;
		this.response = response;
		this.content = content;
	}

	public String getResponse_code() {
		return response_code;
	}

	public String getResponse() {
		return response;
	}

	public String getContent() {
		return content;
	}

}


