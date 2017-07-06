package com.example.mahmoudheshmat.musicapp;


import android.content.Context;
import android.graphics.Path;
import android.graphics.drawable.shapes.PathShape;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class fileManagerAadapter extends RecyclerView.Adapter<fileManagerAadapter.ViewHolder> {

    private Context context;
    private List<type_file> items;
    public static ArrayList<songs_item> mSongs;
    private File mRoot;
    LinearLayout folder_back;


    public fileManagerAadapter(Context context, List<type_file> items, LinearLayout folder_back) {
        this.context = context;
        this.items = items;
        this.folder_back = folder_back;
        mSongs = new ArrayList<>();
        getSongsForFiles(items);
    }

    @Override
    public fileManagerAadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_file_manager, parent, false);
        return new fileManagerAadapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(fileManagerAadapter.ViewHolder holder, final int position) {

        String folder_title = items.get(position).getFile_name();
        String fileExtension = folder_title.substring(folder_title.lastIndexOf(".") + 1);
        if (fileExtension.equals("mp3") || fileExtension.equals("MP3")){
            holder.folder_image.setImageResource(R.drawable.songs);
        }

        holder.folder_title.setText(folder_title);
        adapterListener(holder, position);
    }

    public static int numOfDic = 0;

    private void adapterListener(final fileManagerAadapter.ViewHolder holder, final int position) {
        holder.folder_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numOfDic =0;
                File f = new File(items.get(position).getFile_path());

                if(f.isDirectory()){
                    updateDataSetAsync(f);
                }else if(f.isFile()){
                    for (int i =0 ;i<items.size();i++){
                        String folder_title = items.get(i).getFile_name();
                        String fileExtension = folder_title.substring(folder_title.lastIndexOf(".") + 1);
                        if (fileExtension.equals("mp3") || fileExtension.equals("MP3")){

                        }else{
                            numOfDic++;
                        }
                    }
                    int pos = position - numOfDic;
                    Log.d("response", String.valueOf(pos));
                    NavigationUtils.file_playingsongActivity(context, pos);
                }
            }
        });

        folder_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File parent = mRoot.getParentFile();
                if (parent != null && parent.canRead()) {
                    updateDataSetAsync(parent);
                }else{

                }

            }
        });
    }

    public void updateDataSetAsync(File newRoot) {
        mRoot = newRoot;
        new NavigateTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mRoot);
    }

    private class NavigateTask extends AsyncTask<File, Void, List<type_file>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<type_file> doInBackground(File... params) {
            List<type_file> files = FileLoader.getFilesFromPath(params[0]);
            getSongsForFiles(files);
            return files;
        }

        @Override
        protected void onPostExecute(List<type_file> files) {
            super.onPostExecute(files);
            if(files.isEmpty()){
                Toast.makeText(context, "Folder is empty", Toast.LENGTH_SHORT).show();
            }else {
                items = files;
                notifyDataSetChanged();
            }
        }
    }

    private void getSongsForFiles(List<type_file> files) {
        mSongs.clear();
        for (int i = 0 ; i<files.size() ; i++){
            if(files.get(i).getFile_name().endsWith(".mp3") || files.get(i).getFile_name().endsWith(".MP3")){
                mSongs.add(FileLoader.getSongFromPath(context, files.get(i).getFile_path()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void notifyDatachanged(List<type_file> newitems){
        items.clear();
        items = newitems;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView folder_image;
        TextView folder_title;

        ViewHolder(View itemView) {
            super(itemView);
            folder_title = (TextView) itemView.findViewById(R.id.folder_title);
            folder_image = (ImageView) itemView.findViewById(R.id.album_art);
        }
    }
}