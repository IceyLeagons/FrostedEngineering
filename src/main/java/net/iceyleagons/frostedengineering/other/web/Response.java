/*******************************************************************************
 * Copyright (C) IceyLeagons(https://iceyleagons.net/) 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
