package udacity.graingersoftware.com.popularmovies.models;

/**
 * Created by graingersoftware on 9/6/15.
 */
public class Review
{
    public String mId;
    public String mAuthor;
    public String mContent;
    public String mUrl;

    public String getId()
    {
        return mId;
    }

    public void setId(final String id)
    {
        mId = id;
    }

    public String getAuthor()
    {
        return mAuthor;
    }

    public void setAuthor(final String author)
    {
        mAuthor = author;
    }

    public String getContent()
    {
        return mContent;
    }

    public void setContent(final String content)
    {
        mContent = content;
    }

    public String getUrl()
    {
        return mUrl;
    }

    public void setUrl(final String url)
    {
        mUrl = url;
    }
}
