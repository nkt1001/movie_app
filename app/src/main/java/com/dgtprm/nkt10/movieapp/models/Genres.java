package com.dgtprm.nkt10.movieapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Genres implements Parcelable {
    @SerializedName("genres")
    @Expose
    private List<Genre> genres = null;

    public Genres() {
        genres = new ArrayList<>();
    }

    protected Genres(Parcel in) {
        genres = in.createTypedArrayList(Genre.CREATOR);
    }

    public static final Creator<Genres> CREATOR = new Creator<Genres>() {
        @Override
        public Genres createFromParcel(Parcel in) {
            return new Genres(in);
        }

        @Override
        public Genres[] newArray(int size) {
            return new Genres[size];
        }
    };

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(genres);
    }

    public List<Integer> getIdList() {
        List<Integer> list = new ArrayList<>();
        if (genres == null) {
            return list;
        }

        for (Genre genre : genres) {
            list.add(genre.getId());
        }

        return list;
    }

    public List<String> getNameList() {
        List<String> list = new ArrayList<>();
        if (genres == null) {
            return list;
        }

        for (Genre genre : genres) {
            list.add(genre.getName());
        }

        return list;
    }

    public void clearSelection() {
        if (genres == null) {
            return;
        }

        genres.clear();
    }

    @Override
    public String toString() {
        return "Genres{" +
                "genres=" + genres +
                '}';
    }

    public static class Genre implements Parcelable {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;

        public Genre(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        protected Genre(Parcel in) {
            name = in.readString();
        }

        public static final Creator<Genre> CREATOR = new Creator<Genre>() {
            @Override
            public Genre createFromParcel(Parcel in) {
                return new Genre(in);
            }

            @Override
            public Genre[] newArray(int size) {
                return new Genre[size];
            }
        };

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
        }

        @Override
        public String toString() {
            return "Genre{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
