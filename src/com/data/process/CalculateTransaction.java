package com.data.process;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CalculateTransaction {

	//Bulk Calculation
	public void bulkCalculate(ReadData read) throws IOException{
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
		FileWriter writer = new FileWriter("Expected_EndOfDay_Positions.txt"); 
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
	
	
	//Transaction Wise calculation
	public void transactionWiseCalculation(ReadData read) throws IOException{

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
		FileWriter writer = new FileWriter("Expected_EndOfDay_Positions.txt"); 
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
