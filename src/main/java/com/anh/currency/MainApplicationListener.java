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
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainApplicationListener implements ApplicationEventListener {

	public static final String URL_RATE = ".fxexchangerate.com/rss.xml";
	public static final int TIME_RELOAD = 1200000;
	@Context
	private ServletContext ctx;
	private static DatabaseReference mDatabase;
	private static final String[] CURRENCIES_ID = { "AED", "AFN", "ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG",
			"AZN", "BAM", "BBD", "BDT", "BGN", "BHD", "BIF", "BND", "BOB", "BRL", "BSD", "BTN", "BWP", "BYN", "BYR",
			"BZD", "CAD", "CDF", "CHF", "CLP", "CNY", "COP", "CRC", "CUP", "CVE", "CZK", "DJF", "DKK", "DOP", "DZD",
			"EGP", "ERN", "ETB", "EUR", "FJD", "FKP", "GBP", "GEL", "GHS", "GIP", "GMD", "GNF", "GTQ", "GYD", "HKD",
			"HNL", "HRK", "HTG", "HUF", "IDR", "ILS", "IQD", "IRR", "ISK", "JMD", "JOD", "JPY", "KES", "KGS", "KHR",
			"KMF", "KPW", "KRW", "KWD", "KYD", "KZT", "LAK", "LBP", "LKR", "LRD", "LSL", "LVL", "LYD", "MAD", "MDL",
			"MGA", "MKD", "MMK", "MNT", "MOP", "MRO", "MUR", "MVR", "MWK", "MXN", "MYR", "MZN", "NAD", "NGN", "NIO",
			"NOK", "NPR", "NZD", "OMR", "PAB", "PEN", "PGK", "PHP", "PKR", "PLN", "PYG", "QAR", "RON", "RSD", "RUB",
			"RWF", "SAR", "SBD", "SCR", "SDG", "SEK", "SGD", "SHP", "SLL", "SOS", "SRD", "STD", "SYP", "SZL", "THB",
			"TJS", "TMT", "TND", "TOP", "TRY", "TTD", "TWD", "TZS", "UAH", "UGX", "USD", "UYU", "UZS", "VEF", "VND",
			"VUV", "WST", "XAF", "XCD", "XDR", "XOF", "XPF", "YER", "ZAR", "ZMW" };

	@Override
	public void onEvent(ApplicationEvent event) {
		switch (event.getType()) {
		case INITIALIZATION_FINISHED:
			InputStream serviceAccount = null;
			serviceAccount = ctx.getResourceAsStream("/WEB-INF/currencyserver240395-bd7933f24eae.json");
			FirebaseOptions options = null;
			try {
				options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
						.setDatabaseUrl("https://currencyserver240395.firebaseio.com/").build();
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
								parseData(CURRENCIES_ID[i]);
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
		mDatabase.child(idCurrency.toLowerCase()).child(feed.getTitle().toLowerCase()).child(mDatabase.push().getKey()).setValueAsync(feed);
	}

	public void parseData(String idCurrency) throws MalformedURLException {
		String urlHost = "https://" + idCurrency + URL_RATE;
		RSSFeedParser parser = new RSSFeedParser(urlHost);
		List<Feed> listFeed = parser.readFeed();
		for (Feed feed : listFeed) {
			syncData(feed, idCurrency);
		}
	}
}
