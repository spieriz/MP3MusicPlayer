package com.example.spieriz.mp3player;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by spieriz on 2018-06-13.
 */

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<MusicClass> musicList;
    private Context context;
    MusicService mService;
    Intent intent;

    public MyAdapter(Context applicationContext, ArrayList<MusicClass> createLists) {
        this.musicList = createLists;
        this.context = applicationContext;

        intent = new Intent(context, MusicService.class);
        context.getApplicationContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {
        holder.title.setText(musicList.get(position).getMusicTitle());
        holder.duration.setText(musicList.get(position).getDurationString());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mService.handleClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView duration;
        private LinearLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_music_title);
            duration = itemView.findViewById(R.id.txt_duration);
            layout = itemView.findViewById(R.id.cell_layout);
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            mService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };
}
