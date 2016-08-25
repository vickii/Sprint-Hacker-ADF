package com.clashofcoders.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;

import io.undertow.Undertow;
import io.undertow.util.Headers;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://172.31.77.142:5566/";
	HttpsClient httpsclient = new HttpsClient();


//    /**
//     * Starts Grizzly HTTP server.
//     * @return Grizzly HTTP server.
//     */
//    public static HttpServer startServer() {
//        // create a resource config that scans for JAX-RS resources and providers
//        // in com.example.rest package
//        final ResourceConfig rc = new ResourceConfig().packages("com.clashofcoders.rest");
//
//        // create and start a new instance of grizzly http server
//        // exposing the Jersey application at BASE_URI
//        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
//    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
    	ConnectionFactory.getconnection(ConnectionFactory.getDataSource());
        Main main = new Main();
        main.intitalizeHttpsConnection();
        Undertow server = Undertow.builder()
                .addHttpListener(9999, /*"172.31.77.142"*/"172.31.77.142")
                .setHandler((exchange) -> {
                    final Deque<String> keys = exchange.getQueryParameters().get("key");
                    exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
                    String res = new MyResource().doIt(Arrays.asList(keys.getFirst().split(",")));
                    exchange.getResponseSender().send(res);

                }).build();
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Main.shutdown(server);
        }));
    }
    
    
    public static void shutdown(Undertow server){
     ConnectionFactory.closeDB();
     server.stop();
    }

    public void intitalizeHttpsConnection(){
   	    httpsclient.testItbeforeStartup("https://dev-ui1.adfdata.net/hacker/bank/061000052");

    }

    public HttpsClient getHttpsconnection(){
    	return httpsclient;
    }
}

