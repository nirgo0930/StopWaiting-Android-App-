package com.example.stopwaiting;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyWaitingListItemViewHolder extends RecyclerView.ViewHolder {
    public ImageView imgItem;
    public TextView txtName, txtLocDetail, txtWaitCnt;
    public MyWaitingListAdapter adapter;

<<<<<<< HEAD
    public MyWaitingListItemViewHolder(View view, MyWaitingListAdapter.OnItemClickEventListener mItemClickListener) {
        super(view);
        imgItem = view.findViewById(R.id.imgItem);
        txtName = view.findViewById(R.id.txtName);
        txtLocDetail = view.findViewById(R.id.txtLocDetail);
        txtWaitCnt = view.findViewById(R.id.txtWaitCnt);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a_view) {
//                final int position = getAdapterPosition();
//                if (position != RecyclerView.NO_POSITION) {
//                    a_itemClickListener.onItemClick(position);
//                }
                final int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mItemClickListener.onItemClick(txtName.getText().toString());
                }

            }
        });
=======
    public MyWaitingListItemViewHolder(View a_view) {
        super(a_view);
        imgItem = a_view.findViewById(R.id.imgItem);
        txtName = a_view.findViewById(R.id.txtName);
        txtLocDetail = a_view.findViewById(R.id.txtLocDetail);
        txtWaitCnt = a_view.findViewById(R.id.txtWaitCnt);

>>>>>>> origin/master
    }

    public MyWaitingListItemViewHolder linkAdapter(MyWaitingListAdapter adapter) {
        this.adapter = adapter;
<<<<<<< HEAD

        return this;
    }


=======
        return this;
    }
>>>>>>> origin/master
}
