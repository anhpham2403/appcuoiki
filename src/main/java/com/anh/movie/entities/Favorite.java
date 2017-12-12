package com.anh.movie.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "favorite")
public class Favorite {
	private User user;
	private Movie movie;
	private pk id;

	/**
	 * @return the user
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "username", nullable = false, insertable = false, updatable = false)
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the movies
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_movie", nullable = false, insertable = false, updatable = false)
	public Movie getMovie() {
		return movie;
	}

	/**
	 * @param movies
	 *            the movies to set
	 */
	public void setMovies(Movie movie) {
		this.movie = movie;
	}

	/**
	 * @return the id
	 */
	@EmbeddedId
	public pk getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
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
