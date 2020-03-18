package net.iceyleagons.frostedengineering.other.web;

import org.json.JSONArray;

public class Response {

	private String response_code;
	private String response;
	private JSONArray content;

	public Response(String response_code, String response, JSONArray content) {
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

	public JSONArray getContent() {
		return content;
	}

}