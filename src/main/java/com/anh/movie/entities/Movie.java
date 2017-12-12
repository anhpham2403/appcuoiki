package com.anh.movie.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "movie", catalog = "movie", uniqueConstraints = { @UniqueConstraint(columnNames = { "id_movie" }) })
public class Movie implements Serializable {
	private int idMovie;
	private String mImdbId;
	private String mTitle;
	private String mOriginalTitle;
	private String mOverView;
	private String mPosterPath;
	private String mStatus;
	private Date mReleaseDate;
	private double mVoteAverage;
	private int mVoteCount;
	private double popularity;
	private Set<Genre> genres = new HashSet<Genre>();
	private Set<Character> characters = new HashSet<>();
	private Set<Crew> crews = new HashSet<>();
	private Set<Review> reviews = new HashSet<>();
	private Set<Rate> rates = new HashSet<>();
	@Id
	@Column(name = "id_movie", unique = true, nullable = false)
	public int getIdMovie() {
		return idMovie;
	}

	public void setIdMovie(int idMovie) {
		this.idMovie = idMovie;
	}

	@Column(name = "imdb_id", nullable = false)
	public String getmImdbId() {
		return mImdbId;
	}

	public void setmImdbId(String mImdbId) {
		this.mImdbId = mImdbId;
	}

	@Column(name = "title", length = 45, nullable = false)
	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	@Column(name = "original_title", length = 45, nullable = false)
	public String getmOriginalTitle() {
		return mOriginalTitle;
	}

	public void setmOriginalTitle(String mOriginalTitle) {
		this.mOriginalTitle = mOriginalTitle;
	}

	@Column(name = "overview", length = 255, nullable = false)
	public String getmOverView() {
		return mOverView;
	}

	public void setmOverView(String mOverView) {
		this.mOverView = mOverView;
	}

	@Column(name = "poster_path", length = 45, nullable = false)
	public String getmPosterPath() {
		return mPosterPath;
	}

	public void setmPosterPath(String mPosterPath) {
		this.mPosterPath = mPosterPath;
	}

	@Column(name = "status", length = 45, nullable = false)
	public String getmStatus() {
		return mStatus;
	}

	public void setmStatus(String mStatus) {
		this.mStatus = mStatus;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "release_date", nullable = false)
	public Date getmReleaseDate() {
		return mReleaseDate;
	}

	public void setmReleaseDate(Date mReleaseDate) {
		this.mReleaseDate = mReleaseDate;
	}

	@Column(name = "vote_average", nullable = false)
	public double getmVoteAverage() {
		return mVoteAverage;
	}

	public void setmVoteAverage(double mVoteAverage) {
		this.mVoteAverage = mVoteAverage;
	}

	@Column(name = "vote_count", nullable = false)
	public int getmVoteCount() {
		return mVoteCount;
	}

	public void setmVoteCount(int mVoteCount) {
		this.mVoteCount = mVoteCount;
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "movie_genre", catalog = "movie", joinColumns = {
			@JoinColumn(name = "id_movie", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "id_genre", nullable = false, updatable = false) })
	public Set<Genre> getGenres() {
		return genres;
	}

	public void setGenres(Set<Genre> genres) {
		this.genres = genres;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "movie")
	public Set<Character> getCharacters() {
		return characters;
	}

	public void setCharacters(Set<Character> characters) {
		this.characters = characters;
	}

	@Column(name = "popularity", nullable = false)
	public double getPopularity() {
		return popularity;
	}

	public void setPopularity(double popularity) {
		this.popularity = popularity;
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "crew_movie", catalog = "movie", joinColumns = {
			@JoinColumn(name = "id_movie", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "id_crew", nullable = false, updatable = false) })
	public Set<Crew> getCrews() {
		return crews;
	}

	public void setCrews(Set<Crew> crews) {
		this.crews = crews;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "movie")
	public Set<Review> getReviews() {
		return reviews;
	}

	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "movie")
	public Set<Rate> getRates() {
		return rates;
	}

	public void setRates(Set<Rate> rates) {
		this.rates = rates;
	}
	
	

}
