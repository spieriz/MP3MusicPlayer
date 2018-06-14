package com.example.spieriz.mp3player;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by spieriz on 2018-06-13.
 */

public class MusicService extends Service {

    final int MOVE_MILISEC = 10000;
    private static MediaPlayer player;
    private final IBinder mBinder;
    private int currentPosition;
    private ArrayList<MusicClass> musicList;
    private TextView txt_current_song;

    public MusicService(){
        mBinder = new LocalBinder();
        currentPosition = 0;
        musicList = new ArrayList<>();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
    }

    public void setMusicList(ArrayList<MusicClass> list){
        musicList = list;
    }

    public void handleClick(int position) {
        createNewPlayer(musicList.get(position).getMusicId());
        currentPosition = position;
        setCurrentSongName(musicList.get(position).getMusicTitle());
    }


    private void createNewPlayer(int musicId){
        if (player != null)
            player.stop();

        player = MediaPlayer.create(this, musicId);
        player.start();
    }

    public void playPause() {
        if (player == null){
            createNewPlayer(musicList.get(0).getMusicId());
            currentPosition = 0;
            setCurrentSongName(musicList.get(currentPosition).getMusicTitle());
        } else {
            if (player.isPlaying()){
                player.pause();
                //but_play.setImageResource(R.drawable.ic_play_arrow);
            } else {
                player.start();
                //but_play.setImageResource(R.drawable.ic_pause);
            }
        }
    }

    public void playPrevious() {
        if (player == null || currentPosition == 0){
            createNewPlayer(musicList.get(musicList.size() - 1).getMusicId());
            currentPosition = musicList.size() - 1;
            setCurrentSongName(musicList.get(currentPosition).getMusicTitle());
        } else {
            createNewPlayer(musicList.get(currentPosition - 1).getMusicId());
            currentPosition--;
            setCurrentSongName(musicList.get(currentPosition).getMusicTitle());
        }
    }

    public void playNext() {
        if (player == null || currentPosition == musicList.size() - 1){
            createNewPlayer(musicList.get(0).getMusicId());
            currentPosition = 0;
            setCurrentSongName(musicList.get(currentPosition).getMusicTitle());
        } else {
            createNewPlayer(musicList.get(currentPosition + 1).getMusicId());
            currentPosition++;
            setCurrentSongName(musicList.get(currentPosition).getMusicTitle());
        }
    }

    public void moveForward() {
        if (player != null && player.isPlaying()){
            player.seekTo(player.getCurrentPosition() + MOVE_MILISEC);
        }
    }

    public void moveBackward() {
        if (player != null && player.isPlaying()){
            player.seekTo(player.getCurrentPosition() - MOVE_MILISEC);
        }
    }

    public void setTxt_current_song(TextView txt_current_song) {
        this.txt_current_song = txt_current_song;
    }

    private void setCurrentSongName(String str){
        txt_current_song.setText(str);
    }

    public class LocalBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
