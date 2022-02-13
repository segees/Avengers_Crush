package com.example.avengerscrush;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.avengerscrush.EnterPageActivity.isMuted;
public class GamePageActivity extends AppCompatActivity {

    int [] Figures = {
            R.drawable.thor,
            R.drawable.iron_man,
            R.drawable.haulk,
            R.drawable.black_widow,
            R.drawable.hawkeye,
            R.drawable.captain_america
    };

    TextView textView;
    TextView scoreResult;
    Handler mHandler;

    SharedPreferences mUserInfo;
    SharedPreferences sp;
    ImageView imageView2;

    MediaPlayer ExplosionSound;
    MediaPlayer SwitchSound;

    Animation switch_left;
    Animation switch_right;
    Animation switch_up;
    Animation switch_down;

    int widthOfBlock,noOfBlocks = 8, widthOfScreen;

    ArrayList<ImageView> figure =  new ArrayList<>();

    int FigureToBeDragged,FigureToBeReplaced;
    int notFigure = R.drawable.transparent;
    int interval = 100;
    static int score = 0;
    CountDownTimer countDownTimer;

    Dialog score_page;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_page);

        mUserInfo = getSharedPreferences("users", MODE_PRIVATE);
        score_page = new Dialog(this);


        SwitchSound = MediaPlayer.create(GamePageActivity.this,R.raw.switch_sound);
        ExplosionSound = MediaPlayer.create(GamePageActivity.this,R.raw.explosion_sound);
        textView=findViewById(R.id.timer);
        countDownTimer=new CountDownTimer(6*1000,1000) {
            @Override
            public void onTick(long l) {
                textView.setText(String.format("%02d:%02d",((l/1000)/60),(l/1000)%60));
            }
            @Override
            public void onFinish() {
                countDownTimer.cancel();
                openWinDialog();
            }
        }.start();

        switch_left = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.switch_left);
        switch_right = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.switch_right);
        switch_up = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.switch_up);
        switch_down = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.switch_down);

        String level = getIntent().getStringExtra("level");

        int curLevel=Integer.parseInt(level);

        scoreResult=findViewById(R.id.score);
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        widthOfScreen=displayMetrics.widthPixels;
        int heightOfScreen=displayMetrics.heightPixels;
        widthOfBlock=widthOfScreen/ noOfBlocks;
        createBoard(curLevel);

        ExplosionSound.setVolume(0,0);
        scoreResult.setText((String.valueOf(1)));
        while (Integer.parseInt(scoreResult.getText().toString())!=0) {
            score = 0;
            scoreResult.setText((String.valueOf(score)));
            checkRowForThree();
            checkColumnForThree();
            moveDownFigures();
        }

        scoreResult.setText((String.valueOf(1)));
        while (Integer.parseInt(scoreResult.getText().toString())!=0) {
            score = 0;
            scoreResult.setText((String.valueOf(score)));
            checkRowForThree();
            checkColumnForThree();
            moveDownFigures();
        }
        ExplosionSound.setVolume(1.0f,1.0f);

        for (final ImageView imageView:figure)
        {
            imageView.setOnTouchListener(new OnSwipeListener(this)
            {
                @Override
                void onSwipeLeft() {
                    super.onSwipeLeft();
                    FigureToBeDragged = imageView.getId();
                    FigureToBeReplaced = FigureToBeDragged-1;
                    imageView.startAnimation(switch_right);
                    imageView2 = findViewById(FigureToBeReplaced);
                    imageView2.startAnimation(switch_left);
                    SwitchSound.start();
                    switch_left.setAnimationListener(new Animation.AnimationListener() {
                        @Override public void onAnimationStart(Animation animation) { }
                        @Override public void onAnimationEnd(Animation animation) {FigureChange();}
                        @Override public void onAnimationRepeat(Animation animation) { }
                    });
                }

                @Override
                void onSwipeRight() {
                    super.onSwipeRight();
                    FigureToBeDragged = imageView.getId();
                    FigureToBeReplaced = FigureToBeDragged+1;
                    imageView.startAnimation(switch_left);
                    imageView2 = findViewById(FigureToBeReplaced);
                    imageView2.startAnimation(switch_right);

                    SwitchSound.start();
                    switch_left.setAnimationListener(new Animation.AnimationListener() {
                        @Override public void onAnimationStart(Animation animation) { }
                        @Override public void onAnimationEnd(Animation animation) {FigureChange(); }
                        @Override public void onAnimationRepeat(Animation animation) { }
                    });
                }

                @Override
                void onSwipeTop() {
                    super.onSwipeTop();
                    FigureToBeDragged = imageView.getId();
                    FigureToBeReplaced = FigureToBeDragged-noOfBlocks;
                    imageView.startAnimation(switch_down);
                    imageView2  = findViewById(FigureToBeReplaced);
                    imageView2.startAnimation(switch_up);
                    SwitchSound.start();
                    switch_up.setAnimationListener(new Animation.AnimationListener() {
                        @Override public void onAnimationStart(Animation animation) {}
                        @Override public void onAnimationEnd(Animation animation) {FigureChange(); }
                        @Override public void onAnimationRepeat(Animation animation) { }
                    });
                }

                @Override
                void onSwipeBottom() {
                    super.onSwipeBottom();
                    FigureToBeDragged = imageView.getId();
                    FigureToBeReplaced = FigureToBeDragged+noOfBlocks;
                    imageView.startAnimation(switch_up);
                    imageView2 = findViewById(FigureToBeReplaced);
                    imageView2.startAnimation(switch_down);
                    SwitchSound.start();
                    switch_up.setAnimationListener(new Animation.AnimationListener() {
                        @Override public void onAnimationStart(Animation animation) { }
                        @Override public void onAnimationEnd(Animation animation) {FigureChange();}
                        @Override public void onAnimationRepeat(Animation animation) { }
                    });
                }
            });
        }

        mHandler = new Handler();
        startRepeat();
    }

    private void checkRowForFour()
    {
        for (int i = 0; i < 62; i++) {
            int chosenFigure = (int) figure.get(i).getTag();
            boolean isBlank = (int) figure.get(i).getTag() == notFigure;
            Integer[] notValid = {5,6,7,13,14,15,21,22,23,29,30,31,37,38,39,45,46,47,53,54,55};
            List<Integer> list = Arrays.asList(notValid);

            if (!list.contains(i)) {
                int x = i;

                if ((int) figure.get(x++).getTag() == chosenFigure && !isBlank &&
                        (int) figure.get(x++).getTag() == chosenFigure &&
                        (int) figure.get(x++).getTag() == chosenFigure &&
                        (int) figure.get(x).getTag() == chosenFigure) {

                    ExplosionSound.start();
                    score = score + 5;
                    scoreResult.setText((String.valueOf(score)));
                    figure.get(x).setImageResource(notFigure);
                    figure.get(x).setTag(notFigure);
                    x--;
                    figure.get(x).setImageResource(notFigure);
                    figure.get(x).setTag(notFigure);
                    x--;
                    figure.get(x).setImageResource(notFigure);
                    figure.get(x).setTag(notFigure);
                    x--;
                    figure.get(x).setImageResource(notFigure);
                    figure.get(x).setTag(notFigure);
                }
            }
        }
        moveDownFigures();
    }

    private void checkColumnForFour()
    {
        for (int i = 0; i < 39;i++){
            int chosenFigure = (int)figure.get(i).getTag();
            boolean isBlank = (int)figure.get(i).getTag() == notFigure;

            int x=i;

            if((int)figure.get(x).getTag() == chosenFigure && !isBlank &&
                    (int)figure.get(x+noOfBlocks).getTag() == chosenFigure &&
                    (int)figure.get(x+3*noOfBlocks).getTag() == chosenFigure &&
                    (int)figure.get(x+2*noOfBlocks).getTag() == chosenFigure)
            {

                ExplosionSound.start();
                score = score+5;
                scoreResult.setText((String.valueOf(score)));
                figure.get(x).setImageResource(notFigure);
                figure.get(x).setTag(notFigure);
                x = x+noOfBlocks;
                figure.get(x).setImageResource(notFigure);
                figure.get(x).setTag(notFigure);
                x = x+noOfBlocks;
                figure.get(x).setImageResource(notFigure);
                figure.get(x).setTag(notFigure);
                x = x+noOfBlocks;
                figure.get(x).setImageResource(notFigure);
                figure.get(x).setTag(notFigure);
            }
        }
        moveDownFigures();
    }

    private void checkRowForThree()
    {
        for (int i = 0 ; i < 62 ; i++){
            int chosenFigure = (int)figure.get(i).getTag();
            boolean isBlank = (int)figure.get(i).getTag() == notFigure;
            Integer[]notValid = {6, 7, 14, 15, 22, 23, 30, 31, 38, 39, 46, 47, 54, 55};

            List<Integer>list = Arrays.asList(notValid);
            if(!list.contains(i))
            {
                int x = i;

                if((int)figure.get(x).getTag() == chosenFigure &&
                        !isBlank &&
                        (int)figure.get(x+1).getTag() == chosenFigure &&
                        (int)figure.get(x+2).getTag() == chosenFigure)
                {
                    ExplosionSound.start();
                    score = score+3;
                    scoreResult.setText((String.valueOf(score)));
                    figure.get(x).setImageResource(notFigure);
                    figure.get(x).setTag(notFigure);
                    figure.get(x+1).setImageResource(notFigure);
                    figure.get(x+1).setTag(notFigure);
                    figure.get(x+2).setImageResource(notFigure);
                    figure.get(x+2).setTag(notFigure);
                }
            }
        }
        moveDownFigures();
    }

    private void checkColumnForThree()
    {
        for (int i = 0; i < 47;i++){
            int chosenFigure = (int)figure.get(i).getTag();
            boolean isBlank = (int)figure.get(i).getTag() == notFigure;
            int x = i;

            if((int)figure.get(x).getTag() == chosenFigure &&
                    !isBlank &&
                    (int)figure.get(x+noOfBlocks).getTag() == chosenFigure &&
                    (int)figure.get(x+2*noOfBlocks).getTag() == chosenFigure)
            {
                ExplosionSound.start();
                score = score+3;
                scoreResult.setText((String.valueOf(score)));
                figure.get(x).setImageResource(notFigure);
                figure.get(x).setTag(notFigure);
                x = x+noOfBlocks;
                figure.get(x).setImageResource(R.drawable.avengers);
                figure.get(x).setTag(R.drawable.avengers);
                figure.get(x).setImageResource(notFigure);
                figure.get(x).setTag(notFigure);
                x = x+noOfBlocks;
                figure.get(x).setImageResource(notFigure);
                figure.get(x).setTag(notFigure);
            }
        }
        moveDownFigures();
    }

    private void moveDownFigures()
    {
        Integer[] firstRow = {0,1,2,3,4,5,6,7};
        List<Integer>list = Arrays.asList(firstRow);
        for(int i = 55 ; i >= 0 ; i--)
        {
            if ((int)figure.get(i+noOfBlocks).getTag() == notFigure)
            {
                figure.get(i+noOfBlocks).setImageResource((int)figure.get(i).getTag());
                figure.get(i+noOfBlocks).setTag(figure.get(i).getTag());
                figure.get(i).setImageResource(notFigure);
                figure.get(i).setTag(notFigure);
                if(list.contains(i)&&(int)figure.get(i).getTag() == notFigure)
                {
                    int randomColor;
                    if(levelSelect() == 1)
                        randomColor = (int)Math.floor(Math.random()*4);
                    else if(levelSelect() == 2)
                        randomColor = (int)Math.floor(Math.random()*5);
                    else
                        randomColor = (int)Math.floor(Math.random()*6);
                    figure.get(i).setImageResource(Figures[randomColor]);
                    figure.get(i).setTag(Figures[randomColor]);
                }
            }
        }

        for (int i = 0 ; i < 8 ; i++)
        {
            if((int)figure.get(i).getTag() == notFigure)
            {
                int randomFigure;

                if(levelSelect() == 1)
                    randomFigure = (int)Math.floor(Math.random()*4);
                else if(levelSelect() == 2)
                    randomFigure = (int)Math.floor(Math.random()*5);
                else
                    randomFigure = (int)Math.floor(Math.random()*6);

                figure.get(i).setImageResource(Figures[randomFigure]);
                figure.get(i).setTag(Figures[randomFigure]);
            }
        }
    }

    Runnable run=new Runnable() {
        @Override
        public void run() {
            try {
                checkRowForFour();
                checkColumnForFour();
                checkRowForThree();
                checkColumnForThree();
                moveDownFigures();
            }
            finally {
                mHandler.postDelayed(this,interval);
            }
        }
    };

    void startRepeat(){run.run();
    }

    private void FigureChange(){
        int background = (int)figure.get(FigureToBeReplaced).getTag();
        int background1 = (int)figure.get(FigureToBeDragged).getTag();
        figure.get(FigureToBeDragged).setImageResource(background);
        figure.get(FigureToBeReplaced).setImageResource(background1);
        figure.get(FigureToBeDragged).setTag(background);
        figure.get(FigureToBeReplaced).setTag(background1);
    }

    private void createBoard(int level) {
        GridLayout gridLayout = findViewById(R.id.board);
        gridLayout.setRowCount(noOfBlocks);
        gridLayout.setColumnCount(noOfBlocks);
        gridLayout.getLayoutParams().width=widthOfScreen;
        gridLayout.getLayoutParams().height=widthOfScreen;

        for (int i=0; i<noOfBlocks*noOfBlocks; i++)
        {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(widthOfBlock,widthOfBlock));
            imageView.setMaxHeight(widthOfBlock);
            imageView.setMaxWidth(widthOfBlock);

            int randomFigure;

            if(levelSelect() == 1)
                randomFigure = (int)Math.floor(Math.random()*4);
            else if(levelSelect() == 2)
                randomFigure=(int)Math.floor(Math.random()*5);
            else
                randomFigure=(int)Math.floor(Math.random()*6);

            imageView.setImageResource(Figures[randomFigure]);
            imageView.setTag(Figures[randomFigure]);
            figure.add(imageView);
            gridLayout.addView(imageView);
        }
    }
    public int levelSelect(){
        String level = getIntent().getStringExtra("level");
        int currentLevel=Integer.parseInt(level);
        if(currentLevel ==  1)
            return 1;
        else if(currentLevel == 2)
            return 2;
        else return 3;
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
                storeScore(score);
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

    private void storeScore(int playerScore)
    {
        sp=getSharedPreferences("playerScore",MODE_PRIVATE);
        SharedPreferences.Editor mEditor=sp.edit();
        mEditor.putInt("score",playerScore);
        mEditor.apply();
    }

    public void openWinDialog() {

        score_page.setContentView(R.layout.score_page);
        score_page.show();

        TextView scoreView = score_page.findViewById(R.id.scoreView);
        EditText nameEt = score_page.findViewById(R.id.name_ET);
        Button submit = score_page.findViewById(R.id.select_btn);

        scoreView.setText(""+score);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = mUserInfo.getInt("size", 0);
                size++;
                SharedPreferences.Editor editor = mUserInfo.edit();
                editor.putInt("size", size);

                String user_name = nameEt.getText().toString().trim().length() > 0 ?
                        nameEt.getText().toString() : "Unknown";
                editor.putString("userName_" + size, user_name);
                editor.putInt("userScore_" + size, score);

                editor.apply();

                Intent scoreActivity = new Intent(GamePageActivity.this, LeaderBoardActivity.class);
                finish();
                startActivity(scoreActivity);
            }
        });


    }
}


