package udacity.graingersoftware.com.popularmovies.models;

/**
 * Created by graingersoftware on 9/6/15.
 */
public class Trailer
{
    public String mId;
    public String mIso_639_1;
    public String mKey;
    public String mName;
    public String mSite;
    public String mSize;
    public String mType;

    public String getId()
    {
        return mId;
    }

    public void setId(final String id)
    {
        mId = id;
    }

    public String getIso_639_1()
    {
        return mIso_639_1;
    }

    public void setIso_639_1(final String iso_639_1)
    {
        mIso_639_1 = iso_639_1;
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

    public String getSite()
    {
        return mSite;
    }

    public void setSite(final String site)
    {
        mSite = site;
    }

    public String getSize()
    {
        return mSize;
    }

    public void setSize(final String size)
    {
        mSize = size;
    }

    public String getType()
    {
        return mType;
    }

    public void setType(final String type)
    {
        mType = type;
    }
}
