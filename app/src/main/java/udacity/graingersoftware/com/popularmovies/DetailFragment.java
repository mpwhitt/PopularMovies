package udacity.graingersoftware.com.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;

import udacity.graingersoftware.com.popularmovies.models.Movie;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment
{

    private String LOG_TAG = getClass().getSimpleName();
    private Context mContext;

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
        Movie wSelectedMovie = (Movie)wBundle.getSerializable("movie");

        //Set the movie title
        TextView wTitleTextView = (TextView)rootView.findViewById(R.id.movieTitle);
        wTitleTextView.setText(wSelectedMovie.getTitle());

        //Set the poster image
        ImageView wPosterImage = (ImageView)rootView.findViewById(R.id.moviePoster);
        String imageBaseUrl = getString(R.string.poster_base_url);
        String imagePosterPath = wSelectedMovie.mPosterPath;
        Log.i(LOG_TAG, "The poster path is " + imagePosterPath);
        String imagePath = imageBaseUrl + imagePosterPath;
        Picasso.with(mContext).load(imagePath).into(wPosterImage);

        //Set the release date
        TextView wReleaseDate = (TextView)rootView.findViewById(R.id.releaseYear);
        Calendar wCalendar = Calendar.getInstance();
        wCalendar.setTime(wSelectedMovie.getReleaseDate());
        wReleaseDate.setText(String.valueOf(wCalendar.get(Calendar.YEAR)));

        //Set the rating
        TextView wRatingTextView = (TextView)rootView.findViewById(R.id.movieRating);
        wRatingTextView.setText(wSelectedMovie.getVoteAverage() + "/10");

        //Set the overview
        TextView wOverview = (TextView)rootView.findViewById(R.id.movieOverview);
        String wOverviewText = wSelectedMovie.getOverview();
        if (wOverviewText != null)
        {
            if (wOverviewText.equals("null"))
            {
                wOverviewText = getString(R.string.synopsis_not_available);
            }
        }
        wOverview.setText(wOverviewText);
        return rootView;
    }
}
