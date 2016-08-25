package com.clashofcoders.rest;
import java.io.FileInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
 
public class XmlReader {
	String cashflow;
	String routingNumber;
	String execution="";
	Transactions transactions = new Transactions();
	ExecutorService executorService = Executors.newCachedThreadPool();
  public Transactions getAmountfromXml(String filepath,String key) {
	try{
	Main main = new Main();
    String tagContent = null;
    Cashflows cashflows=new Cashflows();
    XMLInputFactory factory = XMLInputFactory.newInstance();
    XMLStreamReader reader =
        factory.createXMLStreamReader(
        new FileInputStream(filepath));
         
    while(reader.hasNext() && !execution.contentEquals("exit")){
      int event = reader.next();
      switch(event){     
      case XMLStreamConstants.START_ELEMENT:
 	     if ("TransactionSummaries".equals(reader.getLocalName())){
 	    	 cashflows = new Cashflows();
 	    	 cashflows.amount=0.0;
 	          }
 	     break;
           
        case XMLStreamConstants.CHARACTERS:
          tagContent = reader.getText().trim();
          break;
           
        case XMLStreamConstants.END_ELEMENT:
          switch(reader.getLocalName()){          
            case "RoutingNumberEntered":
          	transactions.routingNumber=tagContent; 
          	executorService.execute(()->
          	transactions.bankName=main.getHttpsconnection().testItonce("https://dev-ui1.adfdata.net/hacker/bank/"+transactions.routingNumber));
            break;
              
           case "Amount":
            cashflows.amount+=Double.parseDouble(tagContent);
            transactions.cashFlow=""+Math.round(cashflows.amount);
            break;
            case "TransactionSummaries":
         	   execution="exit";
         	   break;
          }
          break;           
      }
    }
    transactions.key=key;
	}catch(Exception e){
		e.printStackTrace();
	}
	executorService.shutdown();
	try{
	executorService.awaitTermination(4, TimeUnit.SECONDS);
	}catch(Exception e){}
    return transactions;
       
  }
  class Cashflows{
		double amount;
}
}