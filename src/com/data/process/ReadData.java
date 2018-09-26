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

	private List<Instrument> instList;
	private List<PositionData> positionList;
	private List<Transaction> transList;
	
	public static void main(String[] args) throws IOException, ParseException {
		ReadData read=new ReadData();
		String sodFile="Input_StartOfDay_Positions.txt";
		String trans="Input_Transactions.txt";
	    read.processInput(sodFile,trans);
	    read.bulkCalculate(read);
	    read.transactionWiseCalculation(read);
	}

	private void processInput(String sodFile,String transFile) throws FileNotFoundException, IOException, ParseException {
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

	private void bulkCalculate(ReadData read) throws IOException{
		List<PositionData> eodPositions=new ArrayList<PositionData>();
		for (Instrument ins:read.getInstList()){
			long transQuantityB=0;
			long transQuantityS=0;
			long quantFinalE=0;
			long quantFinalI=0;
			long deltaI=0;
			long deltaE=0;
			
			for (int i=0;i<ins.getTransList().size();i++){
				switch(ins.getTransList().get(i).getTransactionType().toString()){
				case "B" :transQuantityB=transQuantityB+ins.getTransList().get(i).getTransactionQuantity();break;
				case "S" :transQuantityS=transQuantityS+ins.getTransList().get(i).getTransactionQuantity();break;
				}
			}
			
			for (int k=0;k<ins.getPositionList().size();k++){
				switch(ins.getPositionList().get(k).getAccountType().toString()){
				case "I":quantFinalI=ins.getPositionList().get(k).getQuantity()-transQuantityB;
				quantFinalI=quantFinalI+transQuantityS;
				PositionData tempObj=new PositionData(ins.getInstrument(),"","",0);
				deltaI=quantFinalI-ins.getPositionList().get(k).getQuantity();
				tempObj.setQuantity(quantFinalI);
				tempObj.setDelta(deltaI);
				tempObj.setAccount(ins.getPositionList().get(k).getAccount());
				tempObj.setAccountType("I");
				eodPositions.add(tempObj);
				break;
				case "E":quantFinalE=ins.getPositionList().get(k).getQuantity()+transQuantityB;
				quantFinalE=quantFinalE-transQuantityS;
				deltaE=quantFinalE-ins.getPositionList().get(k).getQuantity();
				PositionData tempObj1=new PositionData(ins.getInstrument(),"","",0);
				tempObj1.setQuantity(quantFinalE);
				tempObj1.setDelta(deltaE);
				tempObj1.setAccountType("E");
				tempObj1.setAccount(ins.getPositionList().get(k).getAccount());
				eodPositions.add(tempObj1);
				break;
				}
			}
		}
		FileWriter writer = new FileWriter("output.txt"); 
		String header="Instrument,Account,AccountType,Quantity,Delta\n";
		writer.write(header);
		
		for (PositionData pos:eodPositions){
			String temp="";
			temp=temp+pos.getInstrumentName()+","+pos.getAccount()+","+pos.getAccountType()+","
			+pos.getQuantity()+","+pos.getDelta()+"\n";
			writer.write(temp);
		}
		writer.close();
	}
	
	private void transactionWiseCalculation(ReadData read) throws IOException{

		List<PositionData> eodPositions=new ArrayList<PositionData>();
		for (Instrument ins:read.getInstList()){
			long transQuantityB=0;
			long transQuantityS=0;
			long quantFinalE=0;
			long quantFinalI=0;
			long deltaI=0;
			long deltaE=0;
			
	
			for (int k=0;k<ins.getPositionList().size();k++){
				quantFinalI=0;
				quantFinalE=0;
				deltaI=0;
				deltaE=0;
				PositionData tempObj=new PositionData(ins.getInstrument(),"","",0);
				tempObj.setQuantity(ins.getPositionList().get(k).getQuantity());
				tempObj.setAccountType("E");
				tempObj.setAccount(ins.getPositionList().get(k).getAccount());
			for (int i=0;i<ins.getTransList().size();i++){
				transQuantityB=0;
				transQuantityS=0;
				
				switch(ins.getTransList().get(i).getTransactionType().toString()){
				case "B" :transQuantityB=ins.getTransList().get(i).getTransactionQuantity();break;
				case "S" :transQuantityS=ins.getTransList().get(i).getTransactionQuantity();break;
				}
				
				switch(ins.getPositionList().get(k).getAccountType().toString()+ins.getTransList().get(i).getTransactionType().toString()){
				case "IB":quantFinalI=tempObj.getQuantity()-transQuantityB;
				tempObj.setQuantity(quantFinalI);
				break;
				case "EB":quantFinalE=tempObj.getQuantity()+transQuantityB;
				tempObj.setQuantity(quantFinalE);		
				break;
				case "IS":quantFinalI=tempObj.getQuantity()+transQuantityS;
				tempObj.setQuantity(quantFinalI);	
				break;
				case "ES":quantFinalE=tempObj.getQuantity()-transQuantityS;
				tempObj.setQuantity(quantFinalE);
				break;
				}
			}
			if (quantFinalI != 0){
			deltaI=quantFinalI-ins.getPositionList().get(k).getQuantity();
			tempObj.setDelta(deltaI);
			}
			if (quantFinalE != 0){
			deltaE=quantFinalE-ins.getPositionList().get(k).getQuantity();
			tempObj.setDelta(deltaE);
			}
			eodPositions.add(tempObj);
			}
			
		}
		FileWriter writer = new FileWriter("output_seq.txt"); 
		String header="Instrument,Account,AccountType,Quantity,Delta\n";
		writer.write(header);
		
		for (PositionData pos:eodPositions){
			String temp="";
			temp=temp+pos.getInstrumentName()+","+pos.getAccount()+","+pos.getAccountType()+","
			+pos.getQuantity()+","+pos.getDelta()+"\n";
			writer.write(temp);
		}
		writer.close();
	
	}
}
 