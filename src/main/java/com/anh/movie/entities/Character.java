package com.anh.movie.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
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
@Table(name = "charac", catalog = "movie")
public class Character implements Serializable {

	private pk id;
	private Actor actor;
	private Movie movie;
	private String character;
	private String profilePath;

	@ManyToOne(cascade=CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinColumn(name = "id_actor", nullable = false,insertable=false, updatable=false)
	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "id_movie", nullable = false,insertable=false, updatable=false)
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

	@EmbeddedId
	public pk getId() {
		return id;
	}

	public void setId(pk id) {
		this.id = id;
	}

	@Embeddable
	public static class pk implements Serializable {

		private int idActor;
		private int idMovie;

		public pk() {
		}
		
		public pk(int idActor, int idMovie) {
			this.idActor = idActor;
			this.idMovie = idMovie;
		}

		@Column(name = "id_actor")
		public int getIdActor() {
			return idActor;
		}

		public void setIdActor(int idActor) {
			this.idActor = idActor;
		}

		@Column(name = "id_movie")
		public int getIdMovie() {
			return idMovie;
		}

		public void setIdMovie(int idMovie) {
			this.idMovie = idMovie;
		}

	}
}
