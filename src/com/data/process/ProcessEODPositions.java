package com.data.process;

import java.io.IOException;

import org.json.simple.parser.ParseException;

public class ProcessEODPositions {

	public static void main(String[] args) {
		ReadData read=new ReadData();
		CalculateTransaction calculate=new CalculateTransaction();
		String sodFile="Input_StartOfDay_Positions.txt";
		String trans="Input_Transactions.txt";
	    try {
	    	//Read Input SODPosition and transaction
			read.processInput(sodFile,trans);
			
			//Bulk Calculation
			calculate.bulkCalculate(read);
			
			//Transaction wise calculation
			calculate.transactionWiseCalculation(read);
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
	   
	}

}
