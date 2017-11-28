package com.anh.movie.entities;
 
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
@Entity
@Table(name = "movie",
  uniqueConstraints = { @UniqueConstraint(columnNames = { "id" }) })
public class Movie {
	private int mId;
	private String mImdbId;
	private String mTitle;
	private String mOriginalTitle;
	private String mOverView;
	private String mPosterPath;
	private String mStatus;
	private Date mReleaseDate;
	private float mVoteAverage;
	private int mVoteCount;
	@Id
	@Column(name = "id")
	public int getmId() {
		return mId;
	}
	public void setmId(int mId) {
		this.mId = mId;
	}
	@Column(name = "imdb_id", nullable = false)
	public String getmImdbId() {
		return mImdbId;
	}
	public void setmImdbId(String mImdbId) {
		this.mImdbId = mImdbId;
	}
	@Column(name = "title",length=45, nullable = false)
	public String getmTitle() {
		return mTitle;
	}
	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}
	@Column(name = "original_title",length=45, nullable = false)
	public String getmOriginalTitle() {
		return mOriginalTitle;
	}
	public void setmOriginalTitle(String mOriginalTitle) {
		this.mOriginalTitle = mOriginalTitle;
	}
	@Column(name = "overview",length=255, nullable = false)
	public String getmOverView() {
		return mOverView;
	}
	public void setmOverView(String mOverView) {
		this.mOverView = mOverView;
	}
	@Column(name = "poster_path",length=45, nullable = false)
	public String getmPosterPath() {
		return mPosterPath;
	}
	public void setmPosterPath(String mPosterPath) {
		this.mPosterPath = mPosterPath;
	}
	@Column(name = "status",length=45, nullable = false)
	public String getmStatus() {
		return mStatus;
	}
	public void setmStatus(String mStatus) {
		this.mStatus = mStatus;
	}
	@Column(name = "release_date", nullable = false)
	public Date getmReleaseDate() {
		return mReleaseDate;
	}
	public void setmReleaseDate(Date mReleaseDate) {
		this.mReleaseDate = mReleaseDate;
	}
	@Column(name = "vote_average", nullable = false)
	public float getmVoteAverage() {
		return mVoteAverage;
	}
	public void setmVoteAverage(float mVoteAverage) {
		this.mVoteAverage = mVoteAverage;
	}
	@Column(name = "vote_count", nullable = false)
	public int getmVoteCount() {
		return mVoteCount;
	}
	public void setmVoteCount(int mVoteCount) {
		this.mVoteCount = mVoteCount;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return new com.google.gson.Gson().toJson(this);
	}
}
