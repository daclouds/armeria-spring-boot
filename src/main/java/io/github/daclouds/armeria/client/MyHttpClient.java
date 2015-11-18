package io.github.daclouds.armeria.client;

import io.netty.util.concurrent.Future;

import com.linecorp.armeria.client.Clients;
import com.linecorp.armeria.client.http.SimpleHttpClient;
import com.linecorp.armeria.client.http.SimpleHttpRequest;
import com.linecorp.armeria.client.http.SimpleHttpRequestBuilder;
import com.linecorp.armeria.client.http.SimpleHttpResponse;

public class MyHttpClient {

	public static void main(String[] args) throws InterruptedException {
		SimpleHttpClient httpClient = Clients.newClient(
		        "none+http://127.0.0.1:8080/", SimpleHttpClient.class);

		SimpleHttpRequest req =
		        SimpleHttpRequestBuilder.forGet("/hi")
		                                .header("Accept", "application/json")
		                                .build();

		Future<SimpleHttpResponse> f = httpClient.execute(req);
		SimpleHttpResponse res = f.sync().getNow();
		System.out.println(new String(res.content()));
	}
}
