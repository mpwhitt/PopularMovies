package udacity.graingersoftware.com.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import udacity.graingersoftware.com.popularmovies.models.Movie;


public class MainActivity extends ActionBarActivity implements MainFragment.Callback
{

    private static final String DETAIL_FRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailFragment(), DETAIL_FRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    public void onItemClick(Movie movie)
    {
        if (mTwoPane)
        {
            DetailFragment wDetailFragment = new DetailFragment();
            Bundle wBundle = new Bundle();
            wBundle.putSerializable("movie", movie);
            wBundle.putBoolean("isTwoPane", mTwoPane);
            wDetailFragment.setArguments(wBundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, wDetailFragment, DETAIL_FRAGMENT_TAG)
                    .commit();
        }
        else
        {
            Intent wIntent = new Intent(this, DetailActivity.class);
            wIntent.putExtra("movie", movie);
            startActivity(wIntent);
        }

    }

    public boolean isTwoPane()
    {
        return mTwoPane;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            Intent wSettingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(wSettingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
