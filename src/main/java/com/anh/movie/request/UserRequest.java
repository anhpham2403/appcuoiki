package com.anh.movie.request;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

@Path("/user")
public class UserRequest {
	@GET
	@Produces("application/json")
	public Response  getUser() throws JSONException{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("F Value", "3"); 
		return Response.status(200).entity(jsonObject.toString()).build();
	}
}
