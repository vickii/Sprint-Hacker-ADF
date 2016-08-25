package com.clashofcoders.rest;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.xml.stream.XMLStreamException;

import com.google.gson.Gson;

/**
 * Root resource (exposed at "myresource" path)
 */
//@Path("my_api")
public class MyResource {
	
	ExecutorService executorService = Executors.newCachedThreadPool();
	ExecutorService execService = Executors.newFixedThreadPool(10);


	static List<Transactions> transactionsList= null;
	XmlReader reader = new XmlReader();

	/**
	 * Method handling HTTP GET requests. The returned object will be sent to
	 * the client as "text/plain" media type.
	 *
	 * @return String that will be returned as a text/plain response.
	 * @throws XMLStreamException
	 * @throws FileNotFoundException
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
    public String doIt(List<String> keys) throws FileNotFoundException, XMLStreamException, InterruptedException, ExecutionException {
		transactionsList = new ArrayList<Transactions>();
		keys.forEach(currkey ->
        executorService.submit(() -> process(currkey)));
    	executorService.shutdown();
    	executorService.awaitTermination(5, TimeUnit.SECONDS);
    	//List<Transactions> entity = new ArrayList<>(transactionsList);
    	execService.shutdown();
    	execService.awaitTermination(5, TimeUnit.SECONDS);
    	return new Gson().toJson(transactionsList);
    }
	
	public Transactions process(String currkey){
		XmlReader reader = new XmlReader();
		Transactions transactions = reader.getAmountfromXml(ConnectionFactory.executeQuery("SELECT `dl_file_name` FROM `cash_flow` WHERE  `key` ='"+currkey+"'").trim(),currkey);
		execService.execute(() ->
		ConnectionFactory.UpdateQuery("UPDATE hacker.cash_flow SET cash_flow ="+transactions.getCashFlow()+", bank_name='"+transactions.getBankName()+"' where `key` ='"+currkey+"'"));
		transactionsList.add(transactions);
		return transactions;
		
	}
}
