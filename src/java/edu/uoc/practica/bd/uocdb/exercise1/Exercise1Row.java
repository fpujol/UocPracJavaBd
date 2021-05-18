package edu.uoc.practica.bd.uocdb.exercise1;

import java.sql.Time;

public class Exercise1Row {

	private int id_album;
	private int num_long_songs;
	private int num_composers;
	
	private double percent_male_musicians;
	private double percent_female_musicians;
	
	private Time average_album_length;


	public Exercise1Row (int id_album,
						 int num_long_songs,
						 int num_composers,
						 double percent_male_musicians,
						 double percent_female_musicians,
						 Time average_album_length)
	{
		super();

		this.set_id_album(id_album);
		
		this.set_num_long_songs(num_long_songs);
		
		this.set_num_composers(num_composers);
			
		this.set_percent_male_musicians(percent_male_musicians);

		this.set_percent_female_musicians(percent_female_musicians);
		
		this.set_average_album_length(average_album_length);
	}
		
	
	public int get_id_album() { return this.id_album; }
	
	public int get_num_long_songs() { return this.num_long_songs; }
	
	public int get_num_composers() { return this.num_composers; }
	
	public double get_percent_male_musicians() { return this.percent_male_musicians; }
	
	public double get_percent_female_musicians() { return this.percent_female_musicians; }
	
	public Time get_average_album_length() { return this.average_album_length; }
		
	
	public void set_id_album(int id_album) { this.id_album = id_album; }
	
	public void set_num_long_songs(int num_long_songs) { this.num_long_songs = num_long_songs; }
	
	public void set_num_composers(int num_composers) { this.num_composers = num_composers;}
		
	public void set_percent_male_musicians(double percent_male_musicians) { this.percent_male_musicians = percent_male_musicians;}

	public void set_percent_female_musicians(double percent_female_musicians) { this.percent_female_musicians = percent_female_musicians; }
	
	public void set_average_album_length(Time average_album_length) { this.average_album_length = average_album_length; }
	
}