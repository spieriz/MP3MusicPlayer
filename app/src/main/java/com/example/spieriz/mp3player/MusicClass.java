package com.example.spieriz.mp3player;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by spieriz on 2018-06-13.
 */

class MusicClass {

    private int music_id;
    private String music_title;
    private int duration;


    public MusicClass(Integer music_id, String music_title, Context context) {
        this.music_id = music_id;
        this.music_title = music_title;
        MediaPlayer mp = MediaPlayer.create(context, music_id);
        this.duration = mp.getDuration();
    }

    public String getDurationString(){
        int sec = duration / 1000;
        int min = sec / 60;
        sec = sec % 60;

        String strSec;
        if (sec < 10)
            strSec = "0" + sec;
        else
            strSec = "" + sec;

        return "" + min + ":" + strSec;
    }

    public int getMusicId(){
        return music_id;
    }

    public String getMusicTitle(){
        return music_title;
    }
}
