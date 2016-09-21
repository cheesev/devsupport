package com.golfzon.devsupport.preference;

import org.eclipse.core.internal.runtime.Activator;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;

public class SPPreference {
	
	private String env;
	private String ip;
	private String port;
	private String userId;
	private String userPassword;
	
	public static SPPreference getPreference(){
		
		IEclipsePreferences eclipsePreferences =
				 InstanceScope.INSTANCE. getNode(Activator.PLUGIN_ID);
		
		String file = eclipsePreferences.get("settingFile", "");
		
		System.out.println("file path : " + file);
		
		SPPreference prefs = new SPPreference();
		
		prefs.setEnv(eclipsePreferences.get("DbSelection", "DEV"));
		
		return prefs;
	}

	public String getEnv() {
		return env;
	}


	public void setEnv(String env) {
		this.env = env;
	}


	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	
	

}
