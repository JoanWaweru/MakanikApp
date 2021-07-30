package com.joan.makanikapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UploadImageListAdapter extends RecyclerView.Adapter<UploadImageListAdapter.ViewHolder> {

    public List<String> fileNamelist;
    public List<String> fileDonelist;

    public UploadImageListAdapter(List<String> fileNamelist, List<String> fileDonelist){
        this.fileDonelist = fileDonelist;
        this.fileNamelist = fileNamelist;

    }


    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.breakdown_images_list,parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull UploadImageListAdapter.ViewHolder holder, int position) {
        String fileName = fileNamelist.get(position);
        holder.fileNameView.setText(fileName);

    }

    @Override
    public int getItemCount() {
        return fileNamelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public TextView fileNameView;
        public ImageView fileDoneView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            fileNameView = mView.findViewById(R.id.upload_filename);
            fileDoneView = mView.findViewById(R.id.upload_icon);
        }
    }
}
