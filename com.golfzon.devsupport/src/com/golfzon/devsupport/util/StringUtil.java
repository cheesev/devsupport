package com.golfzon.devsupport.util;

public class StringUtil {
	
	public static boolean contains(String[] arr, String value) {
	    for (int i = 0; (arr != null) && (i < arr.length); i++) {
	      if (arr[i].equals(value)) {
	        return true;
	      }
	    }
	    return false;
	}
	
	
	public static int containIdx(String[] arr, String value) {
	    for (int i = 0; (arr != null) && (i < arr.length); i++) {
	      if (arr[i].equals(value)) {
	        return i;
	      }
	    }
	    return -1;
	}
	
	public static boolean similContains(String[] arr, String value) {
	    for (int i = 0; (arr != null) && (i < arr.length); i++) {
	      if (value.indexOf(arr[i]) > -1) {
	        return true;
	      }
	    }
	    return false;
	}
	
	public static boolean isEmpty (String value) {		
		return value == null || value.length() == 0 ? true : false;		
	}

}
