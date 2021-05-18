package edu.uoc.practica.bd.uocdb.exercise2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.Date;
import java.util.List;

import edu.uoc.practica.bd.util.DBAccessor;
import edu.uoc.practica.bd.util.FileUtilities;


public class Exercise2InsertAndUpdateDataFromFile 
{
	private FileUtilities fileUtilities;
	
	public Exercise2InsertAndUpdateDataFromFile()
	{
		super();
		fileUtilities = new FileUtilities();
	}
	
	public static void main(String[] args) 
	{
		Exercise2InsertAndUpdateDataFromFile app = new Exercise2InsertAndUpdateDataFromFile();
		app.run();
	}

	private void run() 
	{
		List<List<String>> fileContents = null;
		
		try 
		{
			fileContents = fileUtilities.readFileFromClasspath("exercise2.data");
		} 
		catch (FileNotFoundException e) 
		{
			System.err.println("ERROR: File not found");
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			System.err.println("ERROR: I/O error");
			e.printStackTrace();
		}
		
		if (fileContents == null)	return;
		
		DBAccessor dbaccessor = new DBAccessor();
		dbaccessor.init();
		Connection conn = dbaccessor.getConnection();

		if (conn!=null) {
			
			// TODO Prepare everything before updating or inserting
			PreparedStatement updatePreparedMusicianSt = null;
			PreparedStatement insertPreparedMusicianSt = null;
			PreparedStatement selectPreparedComposerSt = null;
			PreparedStatement insertPreparedComposerSt = null;
			
			try {
							
				String updateSql = "UPDATE musician SET name = ? ,birth = ?,death = ?,gender = ?,nationality = ? " +
						   		   "WHERE id_musician = ?" ;

				updatePreparedMusicianSt = conn.prepareStatement(updateSql);
				
				// TODO Update or insert Song and Composer from every row in file
				for (List<String> row : fileContents) {
					
					// TODO Update or insert record from WORKED for every row in file
					updatePreparedMusicianSt.clearParameters();
					setPSUpdateMusician(updatePreparedMusicianSt,row);
					if(0 == updatePreparedMusicianSt.executeUpdate())
					{
						if(null == insertPreparedMusicianSt)
						{
							String insertSql = "INSERT INTO musician(id_musician, name, birth, death, gender, nationality) VALUES(?, ?, ?, ?, ?, ?)";
							insertPreparedMusicianSt = conn.prepareStatement(insertSql);
						}
						
						insertPreparedMusicianSt.clearParameters();
						setPSInsertMusician(insertPreparedMusicianSt, row);
						insertPreparedMusicianSt.executeUpdate();
					}
					
					String selectSql = "Select * from composer where id_musician=? and id_song=?";
					selectPreparedComposerSt = conn.prepareStatement(selectSql);
					selectPreparedComposerSt.clearParameters();
					setPSSelectComposer(selectPreparedComposerSt,row);
					
					ResultSet rs = selectPreparedComposerSt.executeQuery();
					if(!rs.next())
					{
						if(null == insertPreparedComposerSt)
						{
							String insertSql = "INSERT INTO composer(id_musician, id_song) VALUES(?, ?)";
							insertPreparedComposerSt = conn.prepareStatement(insertSql);
						}
						
						insertPreparedComposerSt.clearParameters();
						setPSInsertComposer(insertPreparedComposerSt, row);
						insertPreparedComposerSt.executeUpdate();
					}
					
					rs.close();
				
				}
			
				// TODO Validate transaction
				conn.commit();
				
			} catch (SQLException e) {
				System.err.println("ERROR: Executing sql");

				try {
					conn.rollback();
				} catch (SQLException rollbackException) {
					System.err.println("ERROR: Executing rollback");
					System.err.println(rollbackException.getMessage());
				}

			} finally {
		
				// TODO Close resources and check exceptions
		
				if (updatePreparedMusicianSt != null) {
					try {
						updatePreparedMusicianSt.close();
					} catch (SQLException e) {
						System.err.println("ERROR: Closing updatePreparedMusicianSt");
						System.err.println(e.getMessage());
					}
				}
				
				if (insertPreparedMusicianSt != null) {
					try {
						insertPreparedMusicianSt.close();
					} catch (SQLException e) {
						System.err.println("ERROR: Closing insertPreparedMusicianSt");
						System.err.println(e.getMessage());
					}
				}
				
				if (selectPreparedComposerSt != null) {
					try {
						selectPreparedComposerSt.close();
					} catch (SQLException e) {
						System.err.println("ERROR: Closing insertPreparedMusicianSt");
						System.err.println(e.getMessage());
					}
				}
				
				if (insertPreparedComposerSt != null) {
					try {
						insertPreparedComposerSt.close();
					} catch (SQLException e) {
						System.err.println("ERROR: Closing insertPreparedMusicianSt");
						System.err.println(e.getMessage());
					}
				}
				
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						System.err.println("ERROR: Closing connection");
						System.err.println(e.getMessage());
					}
				}
			}

		}
				
	}
	
	private void  setPSUpdateMusician (PreparedStatement updateStatement, List<String> row) throws SQLException 
	{
		String[] rowArray = (String[]) row.toArray(new String[0]);
		setValueOrNull(updateStatement,  1, getValueIfNotNull(rowArray, 1));								// name
		setValueOrNull(updateStatement,  2, getDateFromStringOrNull(getValueIfNotNull(rowArray, 2)));		// birth 
		setValueOrNull(updateStatement,  3, getDateFromStringOrNull(getValueIfNotNull(rowArray, 3)));		// death
		setValueOrNull(updateStatement,  4, getValueIfNotNull(rowArray, 4));								// gender 
		setValueOrNull(updateStatement,  5, getValueIfNotNull(rowArray, 5));								// nationality 
		setValueOrNull(updateStatement,  6, getIntegerFromStringOrNull(getValueIfNotNull(rowArray, 0)));	// id_song
	}
	
	
	private void  setPSInsertMusician (PreparedStatement insertStatement, List<String> row) throws SQLException 
	{
		String[] rowArray = (String[]) row.toArray(new String[0]);

		setValueOrNull(insertStatement,  1, getIntegerFromStringOrNull(getValueIfNotNull(rowArray, 0))); 	// id_musician
		setValueOrNull(insertStatement,  2, getValueIfNotNull(rowArray, 1));								// name
		setValueOrNull(insertStatement,  3, getDateFromStringOrNull(getValueIfNotNull(rowArray, 2)));		// birth 
		setValueOrNull(insertStatement,  4, getDateFromStringOrNull(getValueIfNotNull(rowArray, 3)));		// death
		setValueOrNull(insertStatement,  5, getValueIfNotNull(rowArray, 4));								// gender 
		setValueOrNull(insertStatement,  6, getValueIfNotNull(rowArray, 5));								// nationality
	
		
	}
	
	private void  setPSSelectComposer (PreparedStatement selectStatement, List<String> row) throws SQLException 
	{
		String[] rowArray = (String[]) row.toArray(new String[0]);
			
		setValueOrNull(selectStatement, 1, getIntegerFromStringOrNull(getValueIfNotNull(rowArray, 0))); // id_musician  
		setValueOrNull(selectStatement, 2, getIntegerFromStringOrNull(getValueIfNotNull(rowArray, 6))); // id_song
		
	}
	
	private void  setPSInsertComposer (PreparedStatement insertStatement, List<String> row) throws SQLException 
	{
		String[] rowArray = (String[]) row.toArray(new String[0]);
			
		setValueOrNull(insertStatement, 1, getIntegerFromStringOrNull(getValueIfNotNull(rowArray, 0))); // id_musician  
		setValueOrNull(insertStatement, 2, getIntegerFromStringOrNull(getValueIfNotNull(rowArray, 6))); // id_song
	}
	
	private Integer getIntegerFromStringOrNull(String integer) 
	{
		return (integer != null) ? Integer.valueOf(integer) : null;
	}
	
	private String getValueIfNotNull(String[] rowArray, int index) 
	{
		return (index < rowArray.length && rowArray[index].length() > 0) ? rowArray[index] : null;
	}

	
	private Date getDateFromStringOrNull(String date) {
		if (date == null) return null;
	    return Date.valueOf(date);
	}
	
	private void setValueOrNull(PreparedStatement preparedStatement, int parameterIndex, Integer value)	throws SQLException 
	{
		if (value == null)	preparedStatement.setNull(parameterIndex, Types.INTEGER);
		else 				preparedStatement.setInt(parameterIndex, value);
	}

	private void setValueOrNull(PreparedStatement preparedStatement, int parameterIndex, Date date) throws SQLException 
	{ 
		if (date == null) preparedStatement.setNull(parameterIndex, Types.DATE);
		else preparedStatement.setDate(parameterIndex, date);
	}
	
	private void setValueOrNull(PreparedStatement preparedStatement, int parameterIndex, String value) throws SQLException 
	{
		if (value == null || value.length() == 0) 	preparedStatement.setNull(parameterIndex, Types.VARCHAR);
		else 										preparedStatement.setString(parameterIndex, value);
	}
}
