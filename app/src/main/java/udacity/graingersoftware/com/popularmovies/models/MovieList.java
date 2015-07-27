package udacity.graingersoftware.com.popularmovies.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by graingersoftware on 7/26/15.
 */
public class MovieList implements Serializable
{
    public ArrayList<Movie> mMovies;

    public ArrayList<Movie> getMovies()
    {
        return mMovies;
    }

    public void setMovies(final ArrayList<Movie> movies)
    {
        mMovies = movies;
    }
}
