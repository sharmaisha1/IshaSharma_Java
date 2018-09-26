package com.test;

import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.junit.BeforeClass;
import org.junit.Test;

import com.data.process.ReadData;

public class DataTest {
	
	static ReadData read;
	@BeforeClass
	  public static void testSetup() {
	    read = new ReadData();
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
			read.bulkCalculate(read);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//SODPosition
	}
	
	@Test
	public void transactionWiseCalculate(){
		try {
			read.(read);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//SODPosition
	}
}
