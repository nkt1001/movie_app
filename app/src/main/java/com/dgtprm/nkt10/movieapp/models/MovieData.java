package com.dgtprm.nkt10.movieapp.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MovieData implements Parcelable {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private List<Result> results = new ArrayList<>();
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;

    public MovieData(){
        page = 0;
        totalPages = 0;
        totalResults = 0;
    }

    protected MovieData(Parcel in) {
        page = in.readInt();
        totalPages = in.readInt();
        totalResults = in.readInt();
        in.readTypedList(results, Result.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        dest.writeInt(totalPages);
        dest.writeInt(totalResults);
        dest.writeTypedList(results);
    }

    public static final Creator<MovieData> CREATOR = new Creator<MovieData>() {
        @Override
        public MovieData createFromParcel(Parcel in) {
            return new MovieData(in);
        }

        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };

    /**
     *
     * @return
     * The page
     */
    public Integer getPage() {
        return page;
    }

    /**
     *
     * @param page
     * The page
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     *
     * @return
     * The results
     */
    public List<Result> getResults() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setResults(List<Result> results) {
        this.results = results;
    }

    /**
     *
     * @return
     * The totalPages
     */
    public Integer getTotalPages() {
        return totalPages;
    }

    /**
     *
     * @param totalPages
     * The total_pages
     */
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    /**
     *
     * @return
     * The totalResults
     */
    public Integer getTotalResults() {
        return totalResults;
    }

    /**
     *
     * @param totalResults
     * The total_results
     */
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }


    public static class Result implements Parcelable {

        @SerializedName("backdrop_path")
        @Expose
        private String backdropPath;
        @SerializedName("id")
        @Expose
        private Long id;
        @SerializedName("overview")
        @Expose
        private String overview;
        @SerializedName("release_date")
        @Expose
        private String releaseDate;
        @SerializedName("poster_path")
        @Expose
        private String posterPath;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("vote_average")
        @Expose
        private Double voteAverage;

        private boolean isFavorite;

        public Result(long id, String poster, String title) {
            this.id = id;
            this.posterPath = poster;
            this.title = title;
            backdropPath = "";
            overview = "";
            releaseDate = "";
            voteAverage = 0d;
            isFavorite = false;
        }

        protected Result(Parcel in) {
            backdropPath = in.readString();
            overview = in.readString();
            releaseDate = in.readString();
            posterPath = in.readString();
            title = in.readString();
            id = in.readLong();
            voteAverage = in.readDouble();
            isFavorite = in.readInt() > 0;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(backdropPath);
            dest.writeString(overview);
            dest.writeString(releaseDate);
            dest.writeString(posterPath);
            dest.writeString(title);

            dest.writeLong(id);
            dest.writeDouble(voteAverage);
            dest.writeInt(isFavorite ? 1 : 0);
        }

        public static final Creator<Result> CREATOR = new Creator<Result>() {
            @Override
            public Result createFromParcel(Parcel in) {
                return new Result(in);
            }

            @Override
            public Result[] newArray(int size) {
                return new Result[size];
            }
        };

        public String getBackdropPath() {
            return backdropPath;
        }

        public void setBackdropPath(String backdropPath) {
            this.backdropPath = backdropPath;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }

        public String getPosterPath() {
            return posterPath;
        }

        public void setPosterPath(String posterPath) {
            this.posterPath = posterPath;
        }


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Double getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(Double voteAverage) {
            this.voteAverage = voteAverage;
        }

        public boolean isFavorite() {
            return isFavorite;
        }

        public void setFavorite(boolean favorite) {
            isFavorite = favorite;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "backdropPath='" + backdropPath + '\'' +
                    ", id=" + id +
                    ", overview='" + overview + '\'' +
                    ", releaseDate='" + releaseDate + '\'' +
                    ", posterPath='" + posterPath + '\'' +
                    ", title='" + title + '\'' +
                    ", voteAverage=" + voteAverage +
                    ", isFavorite=" + isFavorite +
                    '}';
        }
    }


}
