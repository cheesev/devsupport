package com.golfzon.devsupport.link;

import org.eclipse.jface.text.IRegion;

public class SPStringRegion implements IRegion {
	
	private int offset;
	private int length;
	private String spString;	// "glm.dbo.glp_select_MAbbsNotice" like this.
	
	private String catalNm;		// glm
	private String spNm;		// dbo.glp_select_MAbbsNotice
	private String dbNm;
	
	public SPStringRegion(int offset, int length, String spFullStr, String dbNm) {
	    this.offset = offset;
	    this.length = length;
	    this.spString = spFullStr;
	    this.dbNm = dbNm;
	    
	    int spNmIdx = spString.indexOf("."); 
	    
	    String[] splitedSpStr = spString.split("\\.");
	    
	    if(splitedSpStr.length == 3) {
	    	this.catalNm = splitedSpStr[0];
	    	this.spNm = splitedSpStr[2];
	    } else { 
	    	return;
	    }
	    		    
	}
	
	public boolean isOn(int i) {
	    return (this.offset <= i) && (i <= this.offset + this.length);
	}

	@Override
	public int getLength() {
		// TODO Auto-generated method stub
		return length;
	}

	@Override
	public int getOffset() {
		return offset;
	}

	public String getSpString() {
		return spString;
	}

	public String getCatalNm() {
		return catalNm;
	}

	public String getSpNm() {
		return spNm;
	}

	public String getDbNm() {
		return dbNm;
	}
	
	
	
}
