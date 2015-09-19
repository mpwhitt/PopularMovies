package udacity.graingersoftware.com.popularmovies.models;

import java.io.Serializable;

/**
 * Created by graingersoftware on 9/6/15.
 */
public class Trailer implements Serializable
{
    public String mId;
    public String mKey;
    public String mName;

    public String getId()
    {
        return mId;
    }

    public void setId(final String id)
    {
        mId = id;
    }

    public String getKey()
    {
        return mKey;
    }

    public void setKey(final String key)
    {
        mKey = key;
    }

    public String getName()
    {
        return mName;
    }

    public void setName(final String name)
    {
        mName = name;
    }

}
