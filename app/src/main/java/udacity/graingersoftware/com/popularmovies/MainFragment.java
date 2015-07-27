package udacity.graingersoftware.com.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import udacity.graingersoftware.com.popularmovies.models.Movie;
import udacity.graingersoftware.com.popularmovies.models.MovieList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment
{
    private String LOG_TAG = getClass().getSimpleName();
    private GridView mGridView;
    private ImageAdapter mImageAdapter;
    private String mSortOrder;
    private SharedPreferences mPrefs;

    public MainFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //Get the current sort order
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSortOrder = mPrefs.getString(getString(R.string.prefs_sort_key), getString(R.string.prefs_sort_default));

        mGridView = (GridView)rootView.findViewById(R.id.gridView);
        mImageAdapter = new ImageAdapter(getActivity(), new ArrayList<Movie>());
        mGridView.setAdapter(mImageAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id)
            {
                Intent wIntent = new Intent(getActivity(), DetailActivity.class);
                //Bundle the data to pass to the new intent
                Movie wSelectedMovie = (Movie) mImageAdapter.getMovies().get(position);
                wIntent.putExtra("movie", wSelectedMovie);
                startActivity(wIntent);
            }
        });

        if (savedInstanceState != null)
        {
            //See if the sort order has changed since the last time this screen was visible
            mSortOrder = savedInstanceState.getString("sortOrder");
            String wSortOption = mPrefs.getString(getString(R.string.prefs_sort_key), getString(R.string.prefs_sort_default));
            if (!mSortOrder.equals(wSortOption))
            {
                //The user has changed the sort order in preferences.  Update the list to the new
                //sort order
                mSortOrder = wSortOption;
                updateMovies();
            }
            else
            {
                MovieList wMovieList = (MovieList)savedInstanceState.getSerializable("movies");
                mImageAdapter = new ImageAdapter(getActivity(), wMovieList.getMovies());
                mGridView.setAdapter(mImageAdapter);
            }
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(final Bundle outState)
    {
        super.onSaveInstanceState(outState);
        //Save the first viewable position of the gridview so this position can be reset
        //when the view is loaded.
        outState.putInt("gridviewScrollPosition", mGridView.getFirstVisiblePosition());
        outState.putString("sortOrder", mSortOrder);
        //Create an instance of MovieList to pass as a serializable to the onSaveInstanceState method
        MovieList wMovieList = new MovieList();
        wMovieList.setMovies(mImageAdapter.getMovies());
        outState.putSerializable("movies", wMovieList);
    }

    public void updateMovies()
    {
        mImageAdapter.clear();
        mImageAdapter.notifyDataSetChanged();
        SharedPreferences wPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSortOrder = wPrefs.getString(getString(R.string.prefs_sort_key), getString(R.string.prefs_sort_default));
        FetchMoviesTask wFetchMoviesTask = new FetchMoviesTask();
        wFetchMoviesTask.execute(new String[]{mSortOrder});
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Configuration config = getResources().getConfiguration();
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            mGridView.setNumColumns(2);
        }
        else
        {
            mGridView.setNumColumns(3);
        }
        String wSortOption = mPrefs.getString(getString(R.string.prefs_sort_key), getString(R.string.prefs_sort_default));
        if (mGridView.getAdapter().getCount() == 0)
        {
            updateMovies();
        }
        else if (!mSortOrder.equals(wSortOption))
        {
            //The user has changed the sort order in preferences.  Update the list to the new
            //sort order
            mSortOrder = wSortOption;
            updateMovies();
        }
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, String>
    {
        //Portions of the following code were copied from the Sunshine app.  The weather data
        //code was replaced as necessary to parse the movie json
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        protected String doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonString = null;

            String apiKey = getString(R.string.movie_api_key);

            try {
                // Construct the URL for the MoviesDB query

                //https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=@APIKEY@
                final String MOVIE_BASE_URL = getString(R.string.movie_base_url);
                final String SORT_BY_PARAM = "sort_by";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY_PARAM, params[0])
                        .appendQueryParameter(API_KEY_PARAM, apiKey)
                        .build();

                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to MoviesDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                moviesJsonString = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the movie data, there's no point in attemping
                // to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try
            {
                return moviesJsonString;
            }
            catch (Exception e)
            {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(String result) {
            mImageAdapter.clear();
            try
            {
                mImageAdapter.setMovies(getMovieDataFromJson(result));
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
            mImageAdapter.notifyDataSetChanged();
        }
        private ArrayList<Movie> getMovieDataFromJson(String movieJsonStr)
                throws JSONException
        {
            ArrayList<Movie> wMovies = new ArrayList<>();

            // These are the names of the JSON objects that need to be extracted.
            final String MOVIE_RESULTS = "results";
            final String MOVIE_BACKDROP_PATH = "backdrop_path";
            final String MOVIE_ID = "id";
            final String MOVIE_OVERVIEW = "overview";
            final String MOVIE_RELEASE_DATE = "release_date";
            final String MOVIE_POSTER_PATH = "poster_path";
            final String MOVIE_POPULARITY = "popularity";
            final String MOVIE_TITLE = "title";
            final String MOVIE_VOTE_AVERAGE = "vote_average";
            final String MOVIE_VOTE_COUNT = "vote_count";

            JSONObject forecastJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = forecastJson.getJSONArray(MOVIE_RESULTS);

            for(int i = 0; i < movieArray.length(); i++)
            {
                try
                {
                    String backdropPath;
                    long id = 0;
                    String overview;
                    Date releaseDate;
                    String posterPath;
                    double popularity = 0;
                    String title;
                    double voteAverage = 0;
                    int voteCount = 0;

                    JSONObject thisMovie = movieArray.getJSONObject(i);

                    backdropPath = thisMovie.getString(MOVIE_BACKDROP_PATH);
                    id = Long.parseLong(thisMovie.getString(MOVIE_ID));
                    overview = thisMovie.getString(MOVIE_OVERVIEW);
                    DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                    try
                    {
                        releaseDate = format.parse(thisMovie.getString(MOVIE_RELEASE_DATE));
                    } catch (ParseException e)
                    {
                        e.printStackTrace();
                        releaseDate = new Date();
                    }
                    posterPath = thisMovie.getString(MOVIE_POSTER_PATH);
                    popularity = Double.parseDouble(thisMovie.getString(MOVIE_POPULARITY));
                    title = thisMovie.getString(MOVIE_TITLE);
                    voteAverage = Double.parseDouble(thisMovie.getString(MOVIE_VOTE_AVERAGE));
                    voteCount = Integer.parseInt(thisMovie.getString(MOVIE_VOTE_COUNT));

                    Movie thisMovieObject = new Movie(id, title, backdropPath, posterPath,
                            overview, popularity, voteAverage, voteCount, releaseDate);
                    wMovies.add(thisMovieObject);
                }
                catch (Exception ex)
                {
                    Log.e(LOG_TAG, "Failed to add movie. Error: " +  ex.toString());
                }
            }
            return wMovies;
        }
    }
}
