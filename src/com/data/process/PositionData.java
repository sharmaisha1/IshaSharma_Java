package com.data.process;

public class PositionData {
	String instrumentName;
	String account;
	String accountType;
	long quantity;
	long delta;
	public PositionData(String instrumentName, String account, String accountType, int quantity) {
		super();
		this.instrumentName = instrumentName;
		this.account = account;
		this.accountType = accountType;
		this.quantity = quantity;
	}
	
	public String getInstrumentName() {
		return instrumentName;
	}
	public void setInstrumentName(String instrumentName) {
		this.instrumentName = instrumentName;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public long getDelta() {
		return delta;
	}

	public void setDelta(long delta) {
		this.delta = delta;
	}
	
}
