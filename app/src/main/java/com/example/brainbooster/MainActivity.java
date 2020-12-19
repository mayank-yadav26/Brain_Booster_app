package com.example.brainbooster;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Button button0, button1, button2, button3, playAgain;
    TextView question, scoreText, time, result;
    Random rand = new Random();
    int correctAnswer, incorrectAnswer, correctAnswerLocation;
    int score = 0, numberOfQuestions = 0;
    ArrayList<Integer> answer = new ArrayList<Integer>();

    View gridLayout;
    int nTables;

    SoundPool mSoundPool;
    int mSoundCorrectId;
    int mSoundIncorrectId;
    int mSoundCompleteId;

    private final float LEFT_VOLUME = 1.0f;
    private final float RIGHT_VOLUME = 1.0f;
    private final int NO_LOOP = 0;
    private final int PRIORITY = 0;
    private final float NORMAL_PLAY_RATE = 1.0f;

    Vibrator vibe;

    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        question = findViewById(R.id.question);
        scoreText = findViewById(R.id.score);
        time = findViewById(R.id.time);
        playAgain = findViewById(R.id.playAgain);
        result = findViewById(R.id.result);
        gridLayout = findViewById(R.id.gridLayout);
        Intent intent = getIntent();
        String nTable = intent.getStringExtra("nTable");
        nTables = Integer.parseInt(nTable);

        playAgain(playAgain);

        final int NR_OF_SIMULTANEOUS_SOUNDS = 3;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSoundPool = new SoundPool.Builder()
                    .setMaxStreams(NR_OF_SIMULTANEOUS_SOUNDS)
                    .build();
        } else {
            // Deprecated way of creating a SoundPool before Android API 21.
            mSoundPool = new SoundPool(NR_OF_SIMULTANEOUS_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        }
        mSoundCorrectId = mSoundPool.load(getApplicationContext(), R.raw.correct, 1);
        mSoundIncorrectId = mSoundPool.load(getApplicationContext(), R.raw.incorrect, 1);
        mSoundCompleteId = mSoundPool.load(getApplicationContext(), R.raw.airhorn, 1);

        // for vibration
        vibe = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
    }

    void generateQuestion() {
        answer.clear();
        int num1 = rand.nextInt((nTables + 1));
        int num2 = rand.nextInt(11);
        int ansLimit = nTables * 10 + 1;
        question.setText(Integer.toString(num1) + " * " + Integer.toString(num2));
        correctAnswerLocation = rand.nextInt(4);
        correctAnswer = num1 * num2;
        for (int i = 0; i < 4; i++) {
            incorrectAnswer = rand.nextInt(ansLimit);
            if (i == correctAnswerLocation) {
                answer.add(correctAnswer);
            } else {
                while (incorrectAnswer == correctAnswer) {
                    incorrectAnswer = rand.nextInt(ansLimit);
                }
                answer.add(incorrectAnswer);
            }
        }
        button0.setText(Integer.toString(answer.get(0)));
        button1.setText(Integer.toString(answer.get(1)));
        button2.setText(Integer.toString(answer.get(2)));
        button3.setText(Integer.toString(answer.get(3)));
    }

    public void chooseAnswer(View view) {
        if (view.getTag().toString().equals(Integer.toString(correctAnswerLocation))) {
            mSoundPool.play(mSoundCorrectId, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY, NO_LOOP, NORMAL_PLAY_RATE);
            result.setText("Correct!");
            score++;
        } else {
            mSoundPool.play(mSoundIncorrectId, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY, NO_LOOP, NORMAL_PLAY_RATE);
            vibe.vibrate(50);
            result.setText("Incorrect!");
        }
        numberOfQuestions++;
        scoreText.setText(Integer.toString(score) + "/" + Integer.toString(numberOfQuestions));
        generateQuestion();
    }

    public void playAgain(final View view) {
        score = 0;
        numberOfQuestions = 0;
        time.setText("30s");
        scoreText.setText("0/0");
        result.setText("");
        playAgain.setVisibility(View.INVISIBLE);
        generateQuestion();
        gridLayout.setVisibility(View.VISIBLE);
        question.setVisibility(View.VISIBLE);

        countDownTimer = new CountDownTimer(30100, 1000) {
            @Override
            public void onTick(long l) {
                time.setText(String.valueOf(l / 1000) + "s");
            }

            @Override
            public void onFinish() {
                time.setText("0s");
                result.setText("Your score : " + Integer.toString(score) + "/" + Integer.toString(numberOfQuestions));
                playAgain.setVisibility(View.VISIBLE);
                gridLayout.setVisibility(View.INVISIBLE);
                question.setVisibility(View.INVISIBLE);
                mSoundPool.play(mSoundCompleteId, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY, NO_LOOP, NORMAL_PLAY_RATE);
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        countDownTimer.cancel();
        super.onBackPressed();
    }
}
