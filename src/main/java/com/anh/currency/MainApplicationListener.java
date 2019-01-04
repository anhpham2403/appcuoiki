package com.anh.currency;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

import com.anh.currency.model.Feed;
import com.anh.currency.model.FeedWithPriority;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainApplicationListener implements ApplicationEventListener {

	public static final String URL_RATE = ".fxexchangerate.com/rss.xml";
	public static final int TIME_RELOAD = 1800000;
	@Context
	private ServletContext ctx;
	private static DatabaseReference mDatabase;
	private static final String[] CURRENCIES_ID = { 
			"AUD", "ALL", "DZD", "ARS", "AWG", "GBP", "BSD", "BHD", "BDT","BBD",
			"BYR", "BZD", "BMD", "BTN", "BOB", "BWP", "BRL", "BND", "BGN", "BIF",
			"CAD", "CNY", "KHR", "CVE", "KYD", "XOF", "XAF", "CLP", "COP", "KMF", 
			"CRC", "HRK", "CUP", "CZK", "EUR", "DKK", "DJF", "DOP", "XCD", "EGP", 
			"SVC", "EEK", "ETB", "FKP", "FJD", "HKD", "IDR", "INR", "GMD", "GTQ",
			"GNF", "GYD", "HTG", "HNL", "HUF", "ISK", "IRR", "IQD", "ILS", "JPY", 
			"JMD", "JOD", "KZT", "KES", "KRW", "KWD", "LAK", "LVL", "LBP", "LSL",
			"LRD", "LYD", "LTL", "MOP", "MKD", "MWK", "MYR", "MVR", "MRO", "MUR",
			"MXN", "MDL", "MNT", "MAD", "MMK", "NAD", "NPR", "ANG", "NZD", "NIO", 
			"NGN", "KPW", "NOK", "OMR", "XPF", "PKR", "PAB", "PGK", "PYG", "PEN",
			"PHP", "PLN", "QAR", "RON", "RUB", "RWF", "CHF", "WST", "STD", "SAR",
			"SCR", "SLL", "SGD", "SKK", "SBD", "SOS", "ZAR", "LKR", "SHP", "SDG", 
			"SZL", "SEK", "SYP", "USD", "THB", "TRY", "TWD", "TZS", "TOP", "TTD",
			"TND", "AED", "UGX", "UAH", "UYU", "VUV", "VND", "YER", "ZMK", "ZWD",
			"VEF", "UZS", "KGS", "GHS"};

	@Override
	public void onEvent(ApplicationEvent event) {
		switch (event.getType()) {
		case INITIALIZATION_FINISHED:
			InputStream serviceAccount = null;
			serviceAccount = ctx.getResourceAsStream("/WEB-INF/test-4a025-firebase-adminsdk-cyauk-6714680bd0.json");
			FirebaseOptions options = null;
			try {
				options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
						.setDatabaseUrl("https://test-4a025.firebaseio.com/").build();
			} catch (IOException e) {
				e.printStackTrace();
			}
			FirebaseApp.initializeApp(options);
			new Thread(new Runnable() {

				@Override
				public void run() {
					while (true) {
						for (int i = 0; i < CURRENCIES_ID.length; i++) {
							try {
								parseData(CURRENCIES_ID[i], i+1);
							} catch (MalformedURLException e) {
								e.printStackTrace();
							}

						}
						try {
							Thread.sleep(TIME_RELOAD);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				}
			}).start();
		}
	}

	@Override
	public RequestEventListener onRequest(RequestEvent requestEvent) {
		return null;
	}

	public void syncData(Feed feed, String idCurrency) {
		mDatabase = FirebaseDatabase.getInstance().getReference("currency");
		mDatabase.child(idCurrency.toLowerCase()).child(feed.getTitle().toLowerCase()).child(mDatabase.push().getKey())
				.setValueAsync(feed);
	}

	public void syncData(FeedWithPriority feed, String idCurrency) {
		mDatabase = FirebaseDatabase.getInstance().getReference("currency");
		mDatabase.child(idCurrency.toLowerCase()).child(feed.getTitle().toLowerCase()).child(mDatabase.push().getKey())
				.setValueAsync(feed);
	}

	public void parseData(String idCurrency, int position) throws MalformedURLException {
		String urlHost = "https://" + idCurrency + URL_RATE;
		RSSFeedParser parser = new RSSFeedParser(urlHost);
		List<FeedWithPriority> listFeed = parser.readFeed();
		for(int i = position ; i < CURRENCIES_ID.length; i++) {
		//for (FeedWithPriority feed : listFeed) {
			if (listFeed.get(i).getPriority() == 0) {
				syncData(new Feed(listFeed.get(i)), idCurrency);
			} else {
				syncData(listFeed.get(i), idCurrency);
			}
		}
	}
}
