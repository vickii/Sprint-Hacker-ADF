package com.clashofcoders.rest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

public class ConnectionFactory {
	
	public static DataSource ds;
	public static Connection con;

	public static DataSource getDataSource() {
		BasicDataSource bds = new BasicDataSource();
		bds.setUrl("jdbc:mysql://dev-rds.adfdata.net:3306/hacker");
		//jdbc:mysql://dev-rds.adfdata.net:3306/hacker
		bds.setUsername("root");
		bds.setPassword("aerosoL1*_");

		return bds;
	}
	public static void getconnection(DataSource ds) {
		ConnectionFactory.ds = ds;
		ds = getDataSource();
		try {
			con = ds.getConnection();
			System.out.println(con);
			System.out.println("connection is closed "+con.isClosed());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static String executeQuery(String query) {		
		Statement stmt=null;  
		ResultSet rs = null;
		String result=null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next()){
				result = rs.getString("dl_file_name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
				try {
					if(rs != null) rs.close();
					if(stmt != null) stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return result;
	}
	
	public static void UpdateQuery(String query) {
		Statement statement = null;
		try {
			statement = con.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeDB(){
		if(con!=null){
			try {
				System.out.println("closing DB");
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}