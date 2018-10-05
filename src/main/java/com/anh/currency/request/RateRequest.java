package com.anh.currency.request;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.anh.currency.RSSFeedParser;
import com.anh.currency.model.Feed;
import com.google.gson.Gson;

@Path("/rate")
public class RateRequest {
	public static final String URL_RATE = ".fxexchangerate.com/rss.xml";

	@GET
	@Path("/{id_base}")
	@Produces("application/json")
	public Response getRate(@PathParam("id_base") String idBase, @QueryParam("id") List<String> listId) {
		List<Feed> listRates = new ArrayList<>();
		List<Feed> list = new ArrayList<>();
		try {
			listRates = parseData(idBase);
			for(Feed feed: listRates) {
				if(listId.contains(feed.getTitle())) {
					list.add(feed);
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		JsonArray array = new JsonArray();
		DecimalFormat twoDecimals = new DecimalFormat("#.#####");
		for (Feed feed : list) {
			JsonObject jsonObject = new JsonObject();
			JsonObject currency = new JsonObject();
			currency.addProperty("id", idBase.toUpperCase());
			jsonObject.add("currency1", currency);
			Double double1 = 0.00000;
			try {
				double1 = Double.valueOf(feed.getDescription());
			} catch (NumberFormatException e) {
			}
			jsonObject.addProperty("rate2", twoDecimals.format(double1));
			currency = new JsonObject();
			currency.addProperty("id", feed.getTitle().toUpperCase());
			jsonObject.add("currency2", currency);
			jsonObject.addProperty("rate1", twoDecimals.format(double1 != 0.00000 ? (double) 1 / double1 : double1));
			array.add(jsonObject);
		}
		return Response.status(200).entity(new Gson().toJson(array)).build();
	}
	
	public List<Feed> parseData(String idCurrency) throws MalformedURLException {
		String urlHost = "https://" + idCurrency + URL_RATE;
		RSSFeedParser parser = new RSSFeedParser(urlHost);
		return parser.readFeed();
	}
}
