package com.anh.movie.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
@Entity
@Table(name = "crew", catalog = "movie", uniqueConstraints = { @UniqueConstraint(columnNames = { "id_crew" }) })
public class Crew {
	private int idCrew;
	private String department;
	private int gender;
	private String job;
	private String name;
	private Date birthday;
	private Date deathday;
	private String biography;
	private String placeOfBirth;
	private String profilePath;
	private String alsoKnownAs;
	private List<Movie> movies;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_crew", unique = true, nullable = false)
	public int getIdCrew() {
		return idCrew;
	}
	public void setIdCrew(int idCrew) {
		this.idCrew = idCrew;
	}
	@Column(name = "department", nullable = false)
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	@Column(name = "gender", nullable = false)
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	@Column(name = "job", nullable = false)
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	@Column(name = "name", nullable = false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "profile_path", nullable = false)
	public String getProfilePath() {
		return profilePath;
	}
	public void setProfilePath(String profilePath) {
		this.profilePath = profilePath;
	}
	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "crews")
	public List<Movie> getMovies() {
		return movies;
	}
	public void setMovies(List<Movie> movies) {
		this.movies = movies;
	}
	@Temporal(TemporalType.DATE)
	@Column(name = "birthday", nullable = false)
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "deathday", nullable = false)
	public Date getDeathday() {
		return deathday;
	}

	public void setDeathday(Date deathday) {
		this.deathday = deathday;
	}

	@Column(name = "biography", length = 255, nullable = false)
	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	@Column(name = "place_of_birth", length = 45, nullable = false)
	public String getPlaceOfBirth() {
		return placeOfBirth;
	}

	public void setPlaceOfBirth(String placeOfBirth) {
		this.placeOfBirth = placeOfBirth;
	}
	@Column(name = "also_known_as", length = 45, nullable = false)
	public String getAlsoKnownAs() {
		return alsoKnownAs;
	}

	public void setAlsoKnownAs(String alsoKnownAs) {
		this.alsoKnownAs = alsoKnownAs;
	}
}
