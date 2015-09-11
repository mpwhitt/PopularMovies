package udacity.graingersoftware.com.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import udacity.graingersoftware.com.popularmovies.models.Movie;
import udacity.graingersoftware.com.popularmovies.models.Review;
import udacity.graingersoftware.com.popularmovies.models.Trailer;

/**
 * Created by graingersoftware on 9/10/15.
 */
public class DetailListViewAdapter extends BaseAdapter
{
    private Context mContext;
    private Movie mMovie;
    private ArrayList<Review> mReviews;
    private ArrayList<Trailer> mTrailers;
    private final String LOG_TAG = DetailListViewAdapter.class.getSimpleName();

    public DetailListViewAdapter(Context context, Movie movie, ArrayList<Review> reviews, ArrayList<Trailer> trailers)
    {
        mContext = context;
        mMovie = movie;
        mReviews = reviews;
        mTrailers = trailers;
    }

    @Override
    public int getCount()
    {
        return 1 + mReviews.size() + mTrailers.size();
    }

    @Override
    public Object getItem(final int position)
    {
        return null;
    }

    @Override
    public long getItemId(final int position)
    {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent)
    {
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        if (position == 0)
        {
            convertView = inflater.inflate(R.layout.detail_row, null);
            convertView = getDetailView(convertView);
        }
        else if (position > 0 && position <= mTrailers.size()) // This is a trailer row
        {
            int arrayPosition = position - 1;
            if (arrayPosition == 0)
            {
                convertView = inflater.inflate(R.layout.first_trailer_row, null);
            }
            else
            {
                convertView = inflater.inflate(R.layout.trailer_row, null);
            }
            TextView wTextView = (TextView)convertView.findViewById(R.id.textView);
            wTextView.setText("Trailer " + (arrayPosition + 1));
        }
        else if (position > mTrailers.size()) // This is a rating row
        {
            int arrayPosition = position - (mTrailers.size() + 1);
            if (arrayPosition == 0)
            {
                convertView = inflater.inflate(R.layout.first_review_row, null);
            }
            else
            {
                convertView = inflater.inflate(R.layout.review_row, null);
            }
            TextView wTextView = (TextView)convertView.findViewById(R.id.textView);
            wTextView.setText("Review " + (arrayPosition + 1));
        }

        return convertView;
    }

    private View getDetailView(View rootView)
    {



        //Set the poster image
        ImageView wPosterImage = (ImageView)rootView.findViewById(R.id.moviePoster);
        String imageBaseUrl = mContext.getString(R.string.poster_base_url);
        String imagePosterPath = mMovie.mPosterPath;
        Log.i(LOG_TAG, "The poster path is " + imagePosterPath);
        String imagePath = imageBaseUrl + imagePosterPath;
        Picasso.with(mContext).load(imagePath).into(wPosterImage);

        //Set the release date
        TextView wReleaseDate = (TextView)rootView.findViewById(R.id.releaseYear);
        Calendar wCalendar = Calendar.getInstance();
        wCalendar.setTime(mMovie.getReleaseDate());
        wReleaseDate.setText(String.valueOf(wCalendar.get(Calendar.YEAR)));

        //Set the rating
        TextView wRatingTextView = (TextView)rootView.findViewById(R.id.movieRating);
        wRatingTextView.setText(mMovie.getVoteAverage() + "/10");

        //Set the overview
        TextView wOverview = (TextView)rootView.findViewById(R.id.movieOverview);
        String wOverviewText = mMovie.getOverview();
        if (wOverviewText != null)
        {
            if (wOverviewText.equals("null"))
            {
                wOverviewText = mContext.getString(R.string.synopsis_not_available);
            }
        }
        wOverview.setText(wOverviewText);
        return rootView;
    }
}
