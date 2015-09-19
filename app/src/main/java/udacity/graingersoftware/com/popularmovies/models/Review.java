package udacity.graingersoftware.com.popularmovies.models;

import java.io.Serializable;

/**
 * Created by graingersoftware on 9/6/15.
 */
public class Review implements Serializable
{
    public String mId;
    public String mAuthor;
    public String mContent;

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
}
