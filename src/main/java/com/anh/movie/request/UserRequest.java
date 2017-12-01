package com.anh.movie.request;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;


import com.google.gson.JsonObject;

@Path("/user")
public class UserRequest {
	@GET
	@Produces("application/json")
	public Response  getUser(){
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("F Value", "3"); 
		return Response.status(200).entity(jsonObject.toString()).build();
	}
}
