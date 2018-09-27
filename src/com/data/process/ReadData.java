package com.data.process;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class ReadData {
	
	private List<Instrument> instList;
	private List<PositionData> positionList;
	private List<Transaction> transList;
	

	public List<Instrument> getInstList() {
		return instList;
	}

	public void setInstList(List<Instrument> instList) {
		this.instList = instList;
	}

	public List<PositionData> getPositionList() {
		return positionList;
	}

	public void setPositionList(List<PositionData> positionList) {
		this.positionList = positionList;
	}

	public List<Transaction> getTransList() {
		return transList;
	}

	public void setTransList(List<Transaction> transList) {
		this.transList = transList;
	}

	//Read input files provided
	public void processInput(String sodFile,String transFile) throws FileNotFoundException, IOException, ParseException {
		BufferedReader br = new BufferedReader(new FileReader(sodFile));
	    String line = null;
	    br.readLine();
	    
	    Set<String> instrumentSet=new HashSet<String>();
	    List<String> ins=new ArrayList<String>();
	    List<PositionData> tempPosition=new ArrayList<PositionData>();
	    while ((line = br.readLine()) != null) {
	      String[] values = line.split(",");
	      instrumentSet.add(values[0]);
	  if (!ins.contains(values[0])){
		  ins.add(values[0]);
	  }
	      PositionData sodPos=new PositionData(values[0],values[1],values[2],Integer.valueOf(values[3]));
	      tempPosition.add(sodPos);
	    }
	    this.setPositionList(tempPosition);
	    br.close();
	    
	    
	    Object obj = new JSONParser().parse(new FileReader(transFile)); 
        // typecasting obj to JSONObject 
        JSONArray jo = (JSONArray) obj; 
        List<Transaction> tempTrans=new ArrayList<Transaction>();
        for (int i=0;i<jo.size();i++){
        	Transaction trans = new Transaction();
        	trans.setTransactionId((Long)((JSONObject)jo.get(i)).get("TransactionId"));
        	trans.setInstrument((String)((JSONObject)jo.get(i)).get("Instrument"));
        	trans.setTransactionQuantity((Long)((JSONObject)jo.get(i)).get("TransactionQuantity"));
        	trans.setTransactionType((String)((JSONObject)jo.get(i)).get("TransactionType"));
        	
        	tempTrans.add(trans);
        }
        this.setTransList(tempTrans);
        List<Instrument> tempInsList=new ArrayList<Instrument>();
        for (String i:ins){
        	Instrument inst=new Instrument();
        	inst.setInstrument(i);
        	List<PositionData> tempList = new ArrayList<PositionData>();
        	for (int j=0;j<this.getPositionList().size();j++){
        		if (i.equals(this.getPositionList().get(j).getInstrumentName())){
        			tempList.add(this.getPositionList().get(j));
        		}
        	}
        	inst.setPositionList(tempList);
        	List<Transaction> perTrans=new ArrayList<Transaction>();
        	for (int k=0;k<this.getTransList().size();k++){   		
        		if (i.equals(this.getTransList().get(k).getInstrument())){
        			perTrans.add(this.getTransList().get(k));
        		}
        	}
        	inst.setTransList(perTrans);
        	tempInsList.add(inst);
        	this.setInstList(tempInsList);
        }
       // bulkCalculate();
     //   transactionWiseCalculation();
	}

	}
 