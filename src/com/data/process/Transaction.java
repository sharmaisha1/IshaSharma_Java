package com.data.process;

public class Transaction {
	
	long transactionId;
	String instrument;
	long transactionQuantity;
	String transactionType;
	
	public long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}
	public String getInstrument() {
		return instrument;
	}
	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public long getTransactionQuantity() {
		return transactionQuantity;
	}
	public void setTransactionQuantity(long transactionQuantity) {
		this.transactionQuantity = transactionQuantity;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	
}
