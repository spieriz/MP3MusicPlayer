package com.example.spieriz.mp3player;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final Integer music_ids[] = {
            R.raw.alan_walker_hope,
            R.raw.hans_zimmer_epilogue,
            R.raw.interstellar_main_theme,
            R.raw.the_island_simple,
            R.raw.the_lord_of_the_rings_rohan,
            R.raw.two_steps_from_hell_victory,
            R.raw.world_of_warships_ost_premium_port_theme_port,
    };

    private final String music_titles[] = {
            "Alan Walker - Hope",
            "Hans Zimmer - Epilogue",
            "Interstellar - Main theme",
            "The Island - Simple",
            "The Lord of The Rings - Rohan theme",
            "Two Steps From Hell - Victory",
            "World Of Warships OST - Premium port theme",
    };

    public MusicService mService;
    protected boolean mBound = false;

    private ImageButton but_prev;
    private ImageButton but_pp;
    private ImageButton but_play;
    private ImageButton but_ff;
    private ImageButton but_next;

    private TextView txt_current_song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initButtons();

        setListeners();

        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        RecyclerView recyclerView = findViewById(R.id.musiclist);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<MusicClass> createLists = prepareData();
        MyAdapter adapter = new MyAdapter(getApplicationContext(), createLists);
        recyclerView.setAdapter(adapter);
    }

    private void setListeners() {
        but_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mService.playPrevious();
            }
        });

        but_pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mService.moveBackward();
            }
        });

        but_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mService.playPause();
            }
        });

        but_ff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mService.moveForward();
            }
        });

        but_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mService.playNext();
            }
        });
    }

    private void initButtons() {
        but_prev = findViewById(R.id.but_prev);
        but_pp = findViewById(R.id.but_pp);
        but_play = findViewById(R.id.but_play);
        but_ff = findViewById(R.id.but_ff);
        but_next = findViewById(R.id.but_next);
        txt_current_song = findViewById(R.id.txt_current_song);
    }

    private ArrayList<MusicClass> prepareData(){

        ArrayList<MusicClass> musics = new ArrayList<>();
        for(int i = 0; i< music_ids.length; i++){
            musics.add(new MusicClass(music_ids[i], music_titles[i], getApplicationContext()));
        }
        return musics;
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            mService = binder.getService();
            mService.setMusicList(prepareData());
            mService.setTxt_current_song(txt_current_song);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("current_song", txt_current_song.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        txt_current_song.setText(savedInstanceState.getString("current_song"));
    }

}
