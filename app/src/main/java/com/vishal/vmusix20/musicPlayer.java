package com.vishal.vmusix20;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class musicPlayer extends AppCompatActivity {
    TextView titletv , currentTimetv, totalTimetv;
    SeekBar seekBar ;
    ImageView pausePlay , previous, next ,musicIcon ;

    ArrayList<AudioModel> songList ;
    AudioModel currentSong;

    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        titletv = findViewById(R.id.songTitle);
        currentTimetv = findViewById(R.id.currentTime);
        totalTimetv = findViewById(R.id.totaltime);
        seekBar = findViewById(R.id.seekbar);
        pausePlay = findViewById(R.id.play);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        musicIcon= findViewById(R.id.icon);
        titletv.setSelected(true);

        songList = (ArrayList<AudioModel>)getIntent().getSerializableExtra("LIST");
        setResourcesWithMusic();
        musicPlayer.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentTimetv.setText(convertToMMSS(mediaPlayer.getCurrentPosition() + ""));
                }
                new Handler().postDelayed(this,100);

            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    void setResourcesWithMusic(){
        currentSong = songList.get(MyMediaPlayer.currentIndex);
        titletv.setText(currentSong.getTitle());
        totalTimetv.setText(convertToMMSS(currentSong.getDuration()));

        pausePlay.setOnClickListener(v->pausePlay());
        previous.setOnClickListener(v->playPreviousSong());
        next.setOnClickListener(v->playNextSong());

        playMusic();

    }

     private void playMusic(){
        mediaPlayer.reset();
        try{
        mediaPlayer.setDataSource(currentSong.getPath());
        mediaPlayer.prepare();
        mediaPlayer.start();
        seekBar.setProgress(0);
        seekBar.setMax(mediaPlayer.getDuration());
     }catch (IOException e){
          e.printStackTrace();
        }}
     private void playNextSong(){
        if(MyMediaPlayer.currentIndex == songList.size() -1 ){
            return;
        }
         MyMediaPlayer.currentIndex+=1;
         mediaPlayer.reset();
         setResourcesWithMusic();
     }
     private void playPreviousSong(){
        if (MyMediaPlayer.currentIndex == 0){
            return;
        }
        MyMediaPlayer.currentIndex -=1 ;
        mediaPlayer.reset();
        setResourcesWithMusic();
     }
private void pausePlay(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            pausePlay.setImageResource(R.drawable.play);
        }else {
            mediaPlayer.start();
            pausePlay.setImageResource(R.drawable.pause);
        }

}
    public static String convertToMMSS(String duration){
        long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
}