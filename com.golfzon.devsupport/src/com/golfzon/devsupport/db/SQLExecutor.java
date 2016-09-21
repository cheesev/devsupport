package com.golfzon.devsupport.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.golfzon.devsupport.util.ConsoleCommand;
import com.golfzon.devsupport.util.StringUtil;

public class SQLExecutor {
	
	public static String getSPText(String dbEnv, String dbNm, String catalNm, String spNm) {
		
		StringBuilder sb = new StringBuilder();
//		ConnectionManager.setConnection(dbNm);
		Connection conn = ConnectionManager.getConnnection(dbEnv, dbNm);
		String execCommand = null;
		
		if(conn == null) return null;
		
		if(StringUtil.similContains(ConnectionManager.WEBDB, dbNm)) execCommand = "{ call SOAP3.dbo.dev_select_procedureInfo (?,?) }";			
		else if(StringUtil.similContains(ConnectionManager.READDB, dbNm)) execCommand = "{ call SOAP.dbo.dev_select_procedureInfo (?,?) }";
		else return null;
		
		try {
			
			CallableStatement cs = conn.prepareCall(execCommand);
			cs.setQueryTimeout(ConnectionManager.QUERY_TIME_OUT);
			
			cs.setString(1, catalNm);
			cs.setString(2, spNm);
			
			ResultSet rs = cs.executeQuery();
			
			while(rs.next()) {
				sb.append(new String(rs.getString("Text")));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ConsoleCommand.writeToConsole(e.getMessage());
			try {
				conn.close();
			} catch (SQLException e1) {
				ConsoleCommand.writeToConsole(e1.getMessage());
			}			
			return null;
		} finally {
			try {				
				conn.close();
			} catch (SQLException e1) {
				ConsoleCommand.writeToConsole(e1.getMessage());
			}
		}
		
		return sb.toString();
	}
}
