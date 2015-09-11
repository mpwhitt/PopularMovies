package udacity.graingersoftware.com.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import udacity.graingersoftware.com.popularmovies.models.Movie;
import udacity.graingersoftware.com.popularmovies.models.Review;
import udacity.graingersoftware.com.popularmovies.models.Trailer;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment
{

    private String LOG_TAG = getClass().getSimpleName();
    private Context mContext;
    private ListView mListView;
    private DetailListViewAdapter mDetailListViewAdapter;
    private Movie mSelectedMovie;
    private ArrayList<Review> mReviews;
    private ArrayList<Trailer> mTrailers;

    public DetailFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mContext = getActivity();
        Bundle wBundle = getActivity().getIntent().getExtras();
        mSelectedMovie = (Movie)wBundle.getSerializable("movie");

        mListView = (ListView)rootView.findViewById(R.id.listView);

        //Set the movie title
        TextView wTitleTextView = (TextView)rootView.findViewById(R.id.movieTitle);
        wTitleTextView.setText(mSelectedMovie.getTitle());

        FetchTrailersAndRatingsTask wFetchTrailersAndRatingsTask = new FetchTrailersAndRatingsTask();
        wFetchTrailersAndRatingsTask.execute(new String[]{ String.valueOf(mSelectedMovie.getId()) });


        return rootView;
    }

    private void populateListview()
    {
        mDetailListViewAdapter = new DetailListViewAdapter(getActivity(), mSelectedMovie, mReviews, mTrailers);




        mListView.setAdapter(mDetailListViewAdapter);
    }




    public class FetchTrailersAndRatingsTask extends AsyncTask<String, String, String[]>
    {
        //Portions of the following code were copied from the Sunshine app.  The weather data
        //code was replaced as necessary to parse the movie json
        private final String LOG_TAG = FetchTrailersAndRatingsTask.class.getSimpleName();

        protected String[] doInBackground(String... params) {
            // Will contain the raw JSON response as a string.
            String trailersJsonString = null;
            String reviewsJsonString = null;
            String apiKey = getString(R.string.movie_api_key);

            final String MOVIE_BASE_URL = getString(R.string.rating_trailer_base_url);
            final String RATING_PATH = "reviews";
            final String TRAILER_PATH = "videos";
            final String API_KEY_PARAM = "api_key";
            final String MOVIE_ID = params[0];

            Uri trailerUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendPath(MOVIE_ID)
                    .appendPath(TRAILER_PATH)
                    .appendQueryParameter(API_KEY_PARAM, apiKey)
                    .build();

            Uri ratingUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendPath(MOVIE_ID)
                    .appendPath(RATING_PATH)
                    .appendQueryParameter(API_KEY_PARAM, apiKey)
                    .build();

            try
            {
                trailersJsonString = makeRequest(trailerUri);
                reviewsJsonString = makeRequest(ratingUri);
                return new String[]{ trailersJsonString, reviewsJsonString };
            }
            catch (Exception e)
            {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
                return null;
            }
        }

        private String makeRequest(Uri uri)
        {
            String responseString;
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                // Construct the URL for the MoviesDB query
                URL url = new URL(uri.toString());
                Log.v(LOG_TAG, "Trailer URI " + uri.toString());
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
                responseString = buffer.toString();
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
            return responseString;
        }

        protected void onPostExecute(String[] result) {
            mReviews = new ArrayList<>();
            mTrailers = new ArrayList<>();
            if (result.length == 2)
            {
                try
                {
                    mTrailers = getTrailersFromJson(result[0]);
                    mReviews = getReviewsFromJson(result[1]);
                }
                catch (JSONException ex)
                {
                    Log.e(LOG_TAG, "Error building arrays. Exception: " + ex);
                }
                populateListview();
            }
        }

        private ArrayList<Review> getReviewsFromJson(String jsonString)
                throws JSONException
        {
            ArrayList<Review> wReviews = new ArrayList<>();
            //Names of json objects
            final String REVIEW_ID = "id";
            final String REVIEW_AUTHOR = "author";
            final String REVIEW_CONTENT = "content";
            final String REVIEW_URL = "url";
            final String REVIEW_RESULTS = "results";

            JSONObject reviewObject = new JSONObject(jsonString);
            JSONArray reviewArray = reviewObject.getJSONArray(REVIEW_RESULTS);
            for (int i = 0; i < reviewArray.length(); i ++)
            {
                try
                {
                    JSONObject thisObject = reviewArray.getJSONObject(i);
                    String reviewId = thisObject.getString(REVIEW_ID);
                    String reviewAuthor = thisObject.getString(REVIEW_AUTHOR);
                    String reviewContent = thisObject.getString(REVIEW_CONTENT);
                    String reviewUrl = thisObject.getString(REVIEW_URL);

                    Review thisReview = new Review();
                    thisReview.setId(reviewId);
                    thisReview.setAuthor(reviewAuthor);
                    thisReview.setContent(reviewContent);
                    thisReview.setUrl(reviewUrl);
                    wReviews.add(thisReview);
                }
                catch (Exception ex)
                {
                    Log.e(LOG_TAG, "Error parsing json object. " + ex);
                }
            }
            return wReviews;
        }

        private ArrayList<Trailer> getTrailersFromJson(String jsonString)
                throws JSONException
        {
            ArrayList<Trailer> wTrailers = new ArrayList<>();
            //Names of json objects
            final String TRAILER_ID = "id";
            final String TRAILER_ISO_639_1 = "iso_639_1";
            final String TRAILER_KEY = "key";
            final String TRAILER_NAME = "name";
            final String TRAILER_SITE = "site";
            final String TRAILER_SIZE = "size";
            final String TRAILER_TYPE = "type";
            final String TRAILER_RESULTS = "results";

            JSONObject trailerObject = new JSONObject(jsonString);
            JSONArray trailerArray = trailerObject.getJSONArray(TRAILER_RESULTS);
            for (int i = 0; i < trailerArray.length(); i ++)
            {
                JSONObject thisObject = trailerArray.getJSONObject(i);
                String trailerId = thisObject.getString(TRAILER_ID);
                String trailerIso = thisObject.getString(TRAILER_ISO_639_1);
                String trailerKey = thisObject.getString(TRAILER_KEY);
                String trailerName = thisObject.getString(TRAILER_NAME);
                String trailerSite = thisObject.getString(TRAILER_SITE);
                String trailerSize = thisObject.getString(TRAILER_SIZE);
                String trailerType = thisObject.getString(TRAILER_TYPE);

                Trailer thisTrailer = new Trailer();
                thisTrailer.setId(trailerId);
                thisTrailer.setIso_639_1(trailerIso);
                thisTrailer.setKey(trailerKey);
                thisTrailer.setName(trailerName);
                thisTrailer.setSite(trailerSite);
                thisTrailer.setSize(trailerSize);
                thisTrailer.setType(trailerType);

                wTrailers.add(thisTrailer);
            }

            return wTrailers;
        }

    }
}
