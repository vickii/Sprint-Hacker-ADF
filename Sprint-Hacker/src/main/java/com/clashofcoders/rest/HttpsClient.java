package com.clashofcoders.rest;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.io.*;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class HttpsClient{
	
  private TrustManager[ ] get_trust_mgr() {
     TrustManager[ ] certs = new TrustManager[ ] {
        new X509TrustManager() {
           public X509Certificate[ ] getAcceptedIssuers() { return null; }
           public void checkClientTrusted(X509Certificate[ ] certs, String t) { }
           public void checkServerTrusted(X509Certificate[ ] certs, String t) { }
         }
      };
      return certs;
  }
  
  public void testItbeforeStartup(String https_url){
	     try {
				
		    // Create a context that doesn't check certificates.
	            SSLContext ssl_ctx = SSLContext.getInstance("TLS");
	            TrustManager[ ] trust_mgr = get_trust_mgr();
	            ssl_ctx.init(null,                // key manager
	                         trust_mgr,           // trust manager
	                         new SecureRandom()); // random number generator
	            HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());

		  //  HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
		    InputStream is = new URL(https_url).openStream();
		    try {
		      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		      String jsonText = readAll(rd);
		      JSONObject json = (JSONObject) JSONValue.parse(jsonText);
		      System.out.println(json.get("bankName"));

		    } finally {
		      is.close();
		    }


				
		 } catch (MalformedURLException e) {
			e.printStackTrace();
		 } catch (IOException e) {
			e.printStackTrace();
		 }catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		 }catch (KeyManagementException e) {
			e.printStackTrace();
	      }	
	   }

  public String testItonce(String https_url){
	  JSONObject json = null;
     try {
	  //  HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
	    InputStream is = new URL(https_url).openStream();
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      String jsonText = readAll(rd);
	      json = (JSONObject) JSONValue.parse(jsonText);
	      
	    } finally {
	      is.close();
	    }	
	 } catch (MalformedURLException e) {
		e.printStackTrace();
	 } catch (IOException e) {
		e.printStackTrace();
	 }
     return json.get("bankName").toString();
   }
    
    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
          sb.append((char) cp);
        }
        return sb.toString();
      }
  }

