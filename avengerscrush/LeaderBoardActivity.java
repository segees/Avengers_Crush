package com.example.avengerscrush;

import static com.example.avengerscrush.EnterPageActivity.isMuted;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class LeaderBoardActivity extends AppCompatActivity {
    ArrayList<UserInfo> mUsers = new ArrayList<>();
    SharedPreferences sp;
    SharedPreferences mUserInfo;
    ListViewAdapter mAdapter;
    ImageButton deleteBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_page);
        ListView listView = findViewById(R.id.listView);
        listView.setBackgroundColor(Color.WHITE);
        mUserInfo = getSharedPreferences("users", MODE_PRIVATE);
        int size = mUserInfo.getInt("size", 0);

        for (int i = 1; i <= size; i++) {
            mUsers.add(new UserInfo(mUserInfo.getString("userName_" + i, "Unknown"),
                    mUserInfo.getInt("userScore_" + i, 0),
                    mUserInfo.getBoolean("userIsAsset_" + i, false)));
        }
        Collections.sort(mUsers);
        mAdapter = new ListViewAdapter(this, R.layout.user_score_layout, mUsers);
        listView.setAdapter(mAdapter);
        deleteBtn=findViewById(R.id.deleteMem);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserInfo.edit().clear().commit();
                Toast.makeText(getApplicationContext(),"you have just deleted the mem",Toast.LENGTH_SHORT).show();
            }
        });

    }
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
                finish();
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

}
