package pers.zy.googleimgservice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

public class ImgAdapter extends RecyclerView.Adapter<ImgAdapter.ImgHolder> {


    private List<String> imgUrlList;
    private Context mContext;

    public ImgAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void notifyData(List<String> imgUrlList) {
        this.imgUrlList = imgUrlList;
        notifyDataSetChanged();

    }

    @Override
    public ImgHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImgHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_img_layout, null, false));
    }

    @Override
    public void onBindViewHolder(ImgHolder holder, int position) {
        Log.d("GFZY", "onBindViewHolder: ");
        Glide.with(mContext)
                .load(imgUrlList.get(position))
                .into(new CustomViewTarget<ImageView, Drawable>(holder.img) {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        Log.e("GFZY", "onLoadFailed: ");
                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        Log.e("GFZY", "onResourceReady: ");
                        holder.img.setImageDrawable(resource);
                    }

                    @Override
                    protected void onResourceCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return imgUrlList != null ? imgUrlList.size() : 0;
    }

    static class ImgHolder extends RecyclerView.ViewHolder {

        ImageView img;

        public ImgHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }
}
