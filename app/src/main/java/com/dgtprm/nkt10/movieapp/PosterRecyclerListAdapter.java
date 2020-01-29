package com.dgtprm.nkt10.movieapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dgtprm.nkt10.movieapp.fragment.MoviesFragment;
import com.dgtprm.nkt10.movieapp.network.HttpHelper;
import com.dgtprm.nkt10.movieapp.network.ImageLoader;
import com.dgtprm.nkt10.movieapp.models.MovieData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class PosterRecyclerListAdapter extends RecyclerView.Adapter<PosterRecyclerListAdapter.ViewHolder> {

    private List<MovieData.Result> mData;
    private Context mContext;
    private String mUrlWidthType;
    private MoviesFragment.OnListFragmentInteractionListener mListener;

    public PosterRecyclerListAdapter(Context ctx, List<MovieData.Result> data,
                                     MoviesFragment.OnListFragmentInteractionListener listener){
        this.mData = new ArrayList<>(data);
        this.mContext = ctx;
        this.mListener = listener;
        mUrlWidthType = HttpHelper.SizeList.W342.getString();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final MovieData.Result item = mData.get(position);

        holder.tvTitle.setText(item.getTitle());

        String imgPosterUrl="";
        if(item.getPosterPath() != null)
            imgPosterUrl = HttpHelper.getImgUrl(mUrlWidthType, item.getPosterPath());

        if(imgPosterUrl.isEmpty()) {
            Picasso.with(mContext).load(R.mipmap.grid_item_placeholder)
                    .placeholder(R.mipmap.grid_item_placeholder).error(R.mipmap.grid_item_placeholder)
                    .fit()
                    .into(holder.imvPoster);
        } else {
            ImageLoader.getInstance().loadImageWithPlaceholder(mContext, imgPosterUrl,
                    holder.imvPoster, R.mipmap.grid_item_placeholder);
        }
        holder.itemView.setTag(item);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPosterClicked((MovieData.Result) v.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imvPoster;
        final TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            imvPoster = (ImageView) itemView.findViewById(R.id.imv_grid_item);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_movietitle);
        }
    }

    public void updateDataList(List<MovieData.Result> data){
        mData.clear();
        mData.addAll(data);

        notifyDataSetChanged();
    }
}
