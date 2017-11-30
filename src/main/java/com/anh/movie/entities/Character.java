package com.anh.movie.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity

@Table(name = "character", catalog = "movie", uniqueConstraints = { @UniqueConstraint(columnNames = { "id_movie" }),
		@UniqueConstraint(columnNames = { "id_actor" }) })
public class Character implements Serializable{
	private int idCharacter;
	private Actor actor;
	private Movie movie;
	private String character;
	private String profilePath;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_actor", nullable = false)
	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_movie", nullable = false)
	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	@Column(name = "character", length = 45, nullable = false)
	public String getCharacter() {
		return character;
	}

	public void setCharacter(String character) {
		this.character = character;
	}

	@Column(name = "profile_path", length = 45, nullable = false)
	public String getProfilePath() {
		return profilePath;
	}

	public void setProfilePath(String profilePath) {
		this.profilePath = profilePath;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_character", unique = true, nullable = false)
	public int getIdCharacter() {
		return idCharacter;
	}

	public void setIdCharacter(int idCharacter) {
		this.idCharacter = idCharacter;
	}
	
}
