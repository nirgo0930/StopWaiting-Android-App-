package com.example.stopwaiting.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stopwaiting.R;
import com.example.stopwaiting.ViewHolder.SettingImageViewHolder;

import java.util.List;

public class SettingImageAdapter extends RecyclerView.Adapter<SettingImageViewHolder> {
    private Context mContext;
    private List<Uri> mItemList;
    private int mCheckedPosition = -1;

    public interface OnItemClickEventListener {
        void onItemClick(int a_position);
    }

    private OnItemClickEventListener mItemClickListener = new OnItemClickEventListener() {
        @Override
        public void onItemClick(int a_position) {
            notifyItemChanged(mCheckedPosition, null);
            mCheckedPosition = a_position;
            notifyItemChanged(a_position, null);
        }
    };

    public SettingImageAdapter(List<Uri> a_list) {
        mItemList = a_list;
    }

    public SettingImageAdapter(Context a_context, List<Uri> a_itemList) {
        mContext = a_context;
        mItemList = a_itemList;

    }

    @Override
    public SettingImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.setting_img_item, parent, false);
        return new SettingImageViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(SettingImageViewHolder holder, int position) {
        Uri image_uri = mItemList.get(position);

        Glide.with(mContext)
                .load(image_uri)
                .into(holder.imgItem);

        final int color;
        if (holder.getAdapterPosition() == mCheckedPosition) {
            color = ContextCompat.getColor(holder.itemView.getContext(), R.color.teal_200);
        } else {
            color = ContextCompat.getColor(holder.itemView.getContext(), android.R.color.transparent);
        }
        holder.itemView.setBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public Uri getSelected() {
        if (mCheckedPosition > -1) {
            return mItemList.get(mCheckedPosition);
        }
        return null;
    }

    public int getCheckedPosition() {
        return mCheckedPosition;
    }

    public void clearSelected() {
        mCheckedPosition = -1;
    }
}
