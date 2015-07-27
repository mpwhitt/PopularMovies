package udacity.graingersoftware.com.popularmovies;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import udacity.graingersoftware.com.popularmovies.models.Movie;

/**
 * Created by graingersoftware on 7/20/15.
 */
public class ImageAdapter extends BaseAdapter
{
    private Context mContext;
    private ArrayList<Movie> mMovies;
    private String LOG_TAG;

    public ArrayList<Movie> getMovies()
    {
        return mMovies;
    }

    public void setMovies(final ArrayList<Movie> movies)
    {
        mMovies = movies;
    }

    public ImageAdapter(Context c, ArrayList<Movie> movies) {
        mContext = c;
        mMovies = movies;
        LOG_TAG = mContext.getClass().getSimpleName();
    }

    public void clear()
    {
        mMovies.clear();
    }

    public int getCount() {
        return mMovies.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView = setLayoutParams(imageView);
        }
        else {
            imageView = (ImageView) convertView;
        }
        String imageBaseUrl = mContext.getString(R.string.poster_base_url);
        String imagePosterPath = mMovies.get(position).mPosterPath;
        Log.i(LOG_TAG, "The position is " + position);
        String imagePath = imageBaseUrl + imagePosterPath;
        Picasso.with(mContext).load(imagePath).into(imageView);
        return imageView;
    }

    private ImageView setLayoutParams(ImageView imageView)
    {

        Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        float density  = mContext.getResources().getDisplayMetrics().density;
        float dpWidth  = outMetrics.widthPixels / density;

        int imageWidth = 0;
        int imageHeight = 0;
        Configuration config = mContext.getResources().getConfiguration();
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            //Device is in portrait orientation.  Set the image width at 1/2 of the screen width.  The height will be proportional
            //to the width
            imageWidth = (int)(dpWidth / 2);
            imageHeight = (int)(imageWidth * 1.5);
        }
        else
        {
            //Device is in landscape orientation.  Set the image width at 1/3 of the screen width.  The height will be proportional
            //to the width
            imageWidth = (int)(dpWidth / 3);
            imageHeight = (int)(imageWidth * 1.5);
        }
        //Convert the dp units back to pixels for the layout params
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, imageWidth, mContext.getResources().getDisplayMetrics());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, imageHeight, mContext.getResources().getDisplayMetrics());
        imageView.setLayoutParams(new android.widget.AbsListView.LayoutParams(width, height));
        return imageView;
    }
}
