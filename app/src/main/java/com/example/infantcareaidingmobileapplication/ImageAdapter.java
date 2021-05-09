package com.example.infantcareaidingmobileapplication;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

   
    private Context mContext;
    private List<Uploader> mUploads;
    private OnItemClickListner mListener;
    public ImageAdapter(Context context,List<Uploader> uploads){
        mContext = context;
        mUploads = uploads;
    }   @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item,parent,false);
                return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
                Uploader uploadCurrent = mUploads.get(position);
                holder.textViewName.setText(uploadCurrent.getMonth());

              Picasso.get()
                        .load(uploadCurrent.getmImageUrl())
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(holder.imageView);
      /* Glide.with(mContext)
                .load(mUploads.get(position).getmImageUrl())
                .into(holder.imageView);*/
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView textViewName;
        public ImageView imageView;

        public ImageViewHolder (View itemView){
            super(itemView);

            textViewName = itemView.findViewById(R.id.month_num);
            imageView = itemView.findViewById(R.id.img_up_v);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener!=null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);

                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem UpdateIm = menu.add(Menu.NONE,1,1,"Update");
            MenuItem delete = menu.add(Menu.NONE,1,1,"Delete");

           UpdateIm.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(mListener!=null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:
                            mListener.UpdateIm(position);
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;
                    }

                }
            }
            return false;
        }
    }
    public interface OnItemClickListner{
        void onItemClick(int position);
        void UpdateIm(int position);
        void onDeleteClick(int position);
    }
    public void setOnItemClickListner(OnItemClickListner listner){
        mListener=listner;
    }
}
