package udacity.graingersoftware.com.popularmovies.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by graingersoftware on 7/20/15.
 */
public class Movie implements Serializable
{
    public long mId;
    public String mBackdropPath;
    public String mPosterPath;
    public String mTitle;
    public String mOverview;
    public double mPopularity;
    public double mVoteAverage;
    public int mVoteCount;
    public Date mReleaseDate;

    public Movie()
    {

    }

    public Movie(long id, String title, String backdropPath, String posterPath,
                 String overview, double popularity, double voteAverage, int voteCount,
                 Date releaseDate)
    {
        this.mId = id;
        this.mTitle = title;
        this.mBackdropPath = backdropPath;
        this.mPosterPath = posterPath;
        this.mOverview = overview;
        this.mPopularity = popularity;
        this.mVoteAverage = voteAverage;
        this.mVoteCount = voteCount;
        this.mReleaseDate = releaseDate;
    }

    public long getId()
    {
        return mId;
    }

    public void setId(final long id)
    {
        mId = id;
    }

    public String getBackdropPath()
    {
        return mBackdropPath;
    }

    public void setBackdropPath(final String backdropPath)
    {
        mBackdropPath = backdropPath;
    }

    public String getPosterPath()
    {
        return mPosterPath;
    }

    public void setPosterPath(final String posterPath)
    {
        mPosterPath = posterPath;
    }

    public String getTitle()
    {
        return mTitle;
    }

    public void setTitle(final String title)
    {
        mTitle = title;
    }

    public String getOverview()
    {
        return mOverview;
    }

    public void setOverview(final String overview)
    {
        mOverview = overview;
    }

    public Date getReleaseDate()
    {
        return mReleaseDate;
    }

    public void setReleaseDate(final Date releaseDate)
    {
        mReleaseDate = releaseDate;
    }

    public double getPopularity()
    {
        return mPopularity;
    }

    public void setPopularity(final double popularity)
    {
        mPopularity = popularity;
    }

    public double getVoteAverage()
    {
        return mVoteAverage;
    }

    public void setVoteAverage(final double voteAverage)
    {
        mVoteAverage = voteAverage;
    }

    public int getVoteCount()
    {
        return mVoteCount;
    }

    public void setVoteCount(final int voteCount)
    {
        mVoteCount = voteCount;
    }


}
