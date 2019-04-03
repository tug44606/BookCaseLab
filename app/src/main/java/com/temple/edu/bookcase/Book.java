package com.temple.edu.bookcase;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Book implements Parcelable {

    public int id;
    public int published;

    public String title;
    public String author;
    public String coverURL;

    public Book(JSONObject jsonBook) throws JSONException {
        this.title = jsonBook.getString("title");
        this.author = jsonBook.getString("author");
        this.coverURL = jsonBook.getString("cover_url");

        this.id = jsonBook.getInt("book_id");
        this.published = jsonBook.getInt("published");
    }

    protected Book(Parcel in) {
        id = in.readInt();
        published = in.readInt();
        title = in.readString();
        author = in.readString();
        coverURL = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(published);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(coverURL);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPublished() {
        return published;
    }

    public void setPublished(int published) {
        this.published = published;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCoverURL() {
        return coverURL;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }


}
