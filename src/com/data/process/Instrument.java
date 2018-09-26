package com.data.process;
import java.util.List;

public class Instrument {
 String instrument;
 List<PositionData> positionList;
 List<Transaction> transList;
public List<Transaction> getTransList() {
	return transList;
}
public void setTransList(List<Transaction> transList) {
	this.transList = transList;
}
public String getInstrument() {
	return instrument;
}
public void setInstrument(String instrument) {
	this.instrument = instrument;
}
public List<PositionData> getPositionList() {
	return positionList;
}
public void setPositionList(List<PositionData> positionList) {
	this.positionList = positionList;
}
}
