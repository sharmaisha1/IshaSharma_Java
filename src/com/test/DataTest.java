package com.test;

import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.junit.BeforeClass;
import org.junit.Test;

import com.data.process.CalculateTransaction;
import com.data.process.ReadData;

public class DataTest {
	
	static ReadData read;
	static CalculateTransaction calculate;
	@BeforeClass
	  public static void testSetup() {
	    read = new ReadData();
	    calculate=new CalculateTransaction();
	  }

	@Test
	public void testRead(){
		try {
			read.processInput("input_test.txt","transaction_test.txt");
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testBulkCalculate(){
		try {
			calculate.bulkCalculate(read);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//SODPosition
	}
	
	@Test
	public void transactionWiseCalculate(){
		try {
			calculate.transactionWiseCalculation(read);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//SODPosition
	}
}
