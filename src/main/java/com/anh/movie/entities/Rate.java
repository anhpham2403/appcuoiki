package com.anh.movie.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "rate", catalog = "movie")
public class Rate implements Serializable {
	private User user;
	private Movie movie;
	private int score;
	private pk id;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "username", nullable = false, insertable = false, updatable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_movie", nullable = false, insertable = false, updatable = false)
	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	@Column(name = "score", nullable = false)
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
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
		private String username;
		private int idMovie;

		public pk() {
		}

		public pk(String username, int idMovie) {
			this.username = username;
			this.idMovie = idMovie;
		}

		@Column(name = "username")
		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
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
