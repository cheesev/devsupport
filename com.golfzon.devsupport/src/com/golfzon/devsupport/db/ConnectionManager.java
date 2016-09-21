package com.golfzon.devsupport.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import com.golfzon.devsupport.constant.CommonConst;
import com.golfzon.devsupport.preference.SPPreference;
import com.golfzon.devsupport.util.ConsoleCommand;
import com.golfzon.devsupport.util.StringUtil;

public class ConnectionManager {
	
	private static HashMap<String,SPPreference> prefs = null;
	private static HashMap<String, Connection> connMap = new HashMap<String,Connection>();
	
	public static final int LOGIN_TIMEOUT = 4;
	public static final int QUERY_TIME_OUT = 4;

	public static final String[] WEBDB = { "default", "web" };
	public static final String[] READDB = { "read" };
	public static final String[] GLDB = { "gs", "gl" };
	
	public static void setConnection(String dbNm) {
		if (StringUtil.isEmpty(dbNm)) dbNm = "web";
		
		String devKey = CommonConst.DEV_ENV + "," + dbNm;
		String qaKey = CommonConst.QA_ENV + "," + dbNm;
		
		try {	
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			DriverManager.setLoginTimeout(LOGIN_TIMEOUT);
			
			if (!connMap.containsKey(devKey)) {
				if(StringUtil.similContains(WEBDB, dbNm.toLowerCase())) {					
					Connection devConn = DriverManager.getConnection(CommonConst.DEV_WEBDB_URL, CommonConst.DEV_WEBDB_ID, CommonConst.DEV_WEBDB_PW);
					connMap.put(devKey, devConn);					
				} else if(StringUtil.similContains(READDB, dbNm.toLowerCase())) {
					Connection devConn = DriverManager.getConnection(CommonConst.DEV_READDB_URL, CommonConst.DEV_READDB_ID, CommonConst.DEV_READDB_PW);
					connMap.put(devKey, devConn);
				}
				
			} 
			if (!connMap.containsKey(qaKey)) {
				if(StringUtil.similContains(WEBDB, dbNm.toLowerCase())) {
					Connection qaConn = DriverManager.getConnection(CommonConst.QA_WEBDB_URL, CommonConst.QA_WEBDB_ID, CommonConst.QA_WEBDB_PW);
					connMap.put(qaKey, qaConn);					
				} else if(StringUtil.similContains(READDB, dbNm.toLowerCase())) {
					Connection qaConn = DriverManager.getConnection(CommonConst.DEV_READDB_URL, CommonConst.DEV_READDB_ID, CommonConst.DEV_READDB_PW);
					connMap.put(qaKey, qaConn);
				}
			}
		} catch (Exception sqle) {
			ConsoleCommand.writeToConsole("DB접속 중 에러가 발생했습니다. dbsafer 접속을 확인해보세요.");
			ConsoleCommand.writeToConsole(sqle.getMessage());
			try {
				for(String key : connMap.keySet()) {
					Connection curConn = connMap.get(key);
					curConn.close();
				}				
			} catch (SQLException e){
				ConsoleCommand.writeToConsole(e.getMessage());
			}
			
		}
	}
	
	public static Connection getConnnection(String dbEnv, String dbNm) {
		Connection conn = null;
		try {
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			DriverManager.setLoginTimeout(LOGIN_TIMEOUT);
			if(CommonConst.DEV_ENV.equals(dbEnv)) {
				if(StringUtil.similContains(WEBDB, dbNm.toLowerCase())) {
					conn = DriverManager.getConnection(CommonConst.DEV_WEBDB_URL, CommonConst.DEV_WEBDB_ID, CommonConst.DEV_WEBDB_PW);
				} else if(StringUtil.similContains(READDB, dbNm.toLowerCase())) {
					conn = DriverManager.getConnection(CommonConst.DEV_READDB_URL, CommonConst.DEV_READDB_ID, CommonConst.DEV_READDB_PW);
				}			
			} else if (CommonConst.QA_ENV.equals(dbEnv)) {
				if(StringUtil.similContains(WEBDB, dbNm.toLowerCase())) {
					conn = DriverManager.getConnection(CommonConst.QA_WEBDB_URL, CommonConst.QA_WEBDB_ID, CommonConst.QA_WEBDB_PW);
				} else if(StringUtil.similContains(READDB, dbNm.toLowerCase())) {
					conn = DriverManager.getConnection(CommonConst.QA_READDB_URL, CommonConst.QA_READDB_ID, CommonConst.QA_READDB_PW);
				}			
			}	
		} catch (Exception e) {
			ConsoleCommand.writeToConsole("Fail to connect database. check DB Safer connection.");
			ConsoleCommand.writeToConsole(e.getMessage());
		}
		return conn;
			
	}
	
	public static boolean removeConnnection(String dbEnv, String dbNm) {
		Connection conn = connMap.get(dbEnv + "," + dbNm);
		try {
			connMap.remove(dbEnv + "," + dbNm);
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			ConsoleCommand.writeToConsole(e.getMessage());
		}
		return true;
	}
	
//	private static void setPreference() {
//		
//		if (prefs == null ) {
//		
//			IEclipsePreferences eclipsePreferences =
//					 InstanceScope.INSTANCE. getNode(Activator.PLUGIN_ID);
//			
//			String file = eclipsePreferences.get("settingFile", "");
//			
//			System.out.println("file path : " + file);
//			
//			SPPreference prefs = new SPPreference();
//			System.out.println(eclipsePreferences.get("DbSelection", "DEV"));
//			prefs.setEnv(eclipsePreferences.get("DbSelection", "DEV"));
//		}
//		
//		
//	}
	

}
