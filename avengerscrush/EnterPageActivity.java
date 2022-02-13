package com.example.avengerscrush;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EnterPageActivity extends AppCompatActivity {
    MediaPlayer finishBtn;
    static boolean isMuted =false;
    Dialog instructions_page;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_page);
        instructions_page = new Dialog(this);
        Button instructionDirect = findViewById(R.id.instructionsdirect);
        Button beginnerBtn = findViewById(R.id.beginner_btn);
        Button advanceBtn = findViewById(R.id.advance_btn);
        Button expertBtn = findViewById(R.id.expert_btn);
        Button leaderDirect=findViewById(R.id.leaderDirect);
        instructionDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWinDialog();
            }
        });
        beginnerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishBtn =MediaPlayer.create(EnterPageActivity.this,R.raw.press_sound);
                finishBtn.start();
                Intent intent = new Intent(EnterPageActivity.this, GamePageActivity.class);
                intent.putExtra("level","1");
                startActivity(intent);
            }
        });

        advanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishBtn = MediaPlayer.create(EnterPageActivity.this,R.raw.press_sound);
                finishBtn.start();
                Intent intent = new Intent(EnterPageActivity.this, GamePageActivity.class);
                intent.putExtra("level", "2");
                startActivity(intent);
            }
        });

        expertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishBtn = MediaPlayer.create(EnterPageActivity.this,R.raw.press_sound);
                finishBtn.start();
                Intent intent = new Intent(EnterPageActivity.this, GamePageActivity.class);
                intent.putExtra("level", "3");
                startActivity(intent);
            }
        });
        leaderDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishBtn = MediaPlayer.create(EnterPageActivity.this,R.raw.press_sound);
                finishBtn.start();
                Intent intent = new Intent(EnterPageActivity.this, LeaderBoardActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sound_icon:
                isMuted=!isMuted;
                if(isMuted){
                    manageMusic(true);
                    item.setIcon(R.drawable.ic_mute);
                }
                else {
                    manageMusic(false);
                    item.setIcon(R.drawable.ic_mute);
                }
                break;
            case R.id.home_icon:
                Intent intent = new Intent(this, EnterPageActivity.class);
                finishAffinity();
                startActivity(intent);
        }
        return true;
    }

    public void manageMusic(boolean forceShutdown) {
        if ( isMuted || forceShutdown)
            MusicPlayer.pause();
        else
            MusicPlayer.start(this, MusicPlayer.MUSIC_MENU);
    }
    @Override
    protected void onPause() {
        super.onPause();
        manageMusic(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        manageMusic(false);
    }


    public void openWinDialog() {

        instructions_page.setContentView(R.layout.instructions_page);
        instructions_page.show();

        Button finish = instructions_page.findViewById(R.id.finishBtn);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instructions_page.cancel();
            }
        });


    }
}
