package edu.uoc.practica.bd.uocdb.exercise1;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

import edu.uoc.practica.bd.util.Column;
import edu.uoc.practica.bd.util.DBAccessor;
import edu.uoc.practica.bd.util.Report;

public class Exercise1PrintReportOverQuery {
	public static void main(String[] args) {
		Exercise1PrintReportOverQuery app = new Exercise1PrintReportOverQuery();
		app.run();
	}

	private void run() {
		DBAccessor dbaccessor = new DBAccessor();
		dbaccessor.init();
		Connection conn = dbaccessor.getConnection();

		if (conn != null) {
			Statement cstmt = null;
			ResultSet resultSet = null;

			List<Column> columns = Arrays.asList(new Column("Id album", 8, "id_album"),
					new Column("Num long songs", 14, "num_long_songs"),
					new Column("Avg album length", 16, "average_album_length"),
					new Column("Num composers", 10, "num_composers"),
					new Column("% male mus", 10, "percent_male_musicians"),
					new Column("% female mus", 12, "percent_female_musicians"));

			Report report = new Report();
			report.setColumns(columns);
			List<Object> list = new ArrayList<Object>();
			Statement st = null;
			try {
				// TODO Execute SQL sentence
				st = conn.createStatement();

				// TODO Loop over results and get the main values
				ResultSet rs = st
						.executeQuery("SELECT id_album, " + "num_long_songs, average_album_length, num_composers, "
								+ "percent_male_musicians, percent_female_musicians "
								+ "from report_album order by id_album desc;");

				//Recorrem les files obtingudes del report
				while (rs.next()) {

					int id_album = rs.getInt("id_album");
					int num_long_songs = rs.getInt("num_long_songs");
					int num_composers = rs.getInt("num_composers");

					double percent_male_musicians = rs.getDouble("percent_male_musicians");
					double percent_female_musicians = rs.getDouble("percent_female_musicians");

					Time average_album_length = rs.getTime("average_album_length");

					Exercise1Row exerciseRow = new Exercise1Row(id_album, num_long_songs, num_composers,
							percent_male_musicians, percent_female_musicians, average_album_length);

					list.add(exerciseRow);

				}

				// TODO End loop
				rs.close();

				report.printReport(list);

				if (list.isEmpty())
					System.out.print("List without data");

			} catch (SQLException e) {
				System.out.print("ERROR: List not available");
				
			} finally {

				if (st != null) {
					try {
						st.close();
					} catch (SQLException e) {
						System.err.println("ERROR: Closing statement");
						System.err.println(e.getMessage());
					}
				}			
				
				// TODO Close All resources
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						System.out.print("Error al tancar la base de dades");
					}
				}
			}
			
		}
	}
}
