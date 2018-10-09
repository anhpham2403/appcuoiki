package com.anh.currency.request;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.anh.currency.model.Feed;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

@Path("/history")
public class HistoryRequest {
	@Context
	ServletContext servletContext;
	private DatabaseReference mDatabase;
	private Response response;

	@GET
	@Path("/{id_base}")
	@Produces("application/json")
	// Chu thich
	// 0: lay het
	// 1: ngay
	// 2: tuan
	// 3: thang
	// 4: nam
	public Response getUser(@PathParam("id_base") String idBase, @QueryParam("id") String idCurrency,
			@QueryParam("to") long dateTo, @QueryParam("from") long dateFrom, @QueryParam("priority") String priority) {
		mDatabase = FirebaseDatabase.getInstance().getReference("currency");
		List<Feed> feeds = new ArrayList<>();
		CountDownLatch latch = new CountDownLatch(1);
		Query query = mDatabase.child(idBase).child(idCurrency);
		if (!priority.isEmpty()) {
			query = query.orderByChild("priorities").equalTo(priority);
		}
		query.addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
					Feed feed = snapshot.getValue(Feed.class);
					SimpleDateFormat dt = new SimpleDateFormat("EEE MMM dd yyyyy hh:mm:ss z");
					Calendar time = Calendar.getInstance();
					try {
						time.setTime(dt.parse(feed.getPubDate()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					if (time.getTimeInMillis() >= dateFrom && time.getTimeInMillis() <= dateTo) {
						feeds.add(feed);
					}
				}
				Gson gson = new Gson();
				String json = gson.toJson(feeds);
				response = Response.status(200).entity(json).build();
				latch.countDown();
			}

			@Override
			public void onCancelled(DatabaseError error) {
				response = Response.status(500).entity("DatabaseError").build();
				latch.countDown();

			}
		});
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return response;
	}
}