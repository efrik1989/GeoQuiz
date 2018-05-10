package com.example.user.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";
    private static final String Key_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;
    private Button mTrueButonn;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private Button mCheatButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    int numberRigthAnswers = 0;



    private Question[] mQuestionsBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_ocean,true),
            new Question(R.string.question_mideast, true),
            new Question(R.string.question_africa, true),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };

    private int mCurrentIndex = 0;
    private boolean mIsCheat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreat(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(Key_INDEX, 0);
        }

        mTrueButonn = (Button) findViewById(R.id.true_button);
        mTrueButonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);

            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                checkAnswer(false);

            }
        });

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionsBank.length;
                updateQuestion();
            }
        });

        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionsBank.length;
                if (mCurrentIndex == 0) {
                    mPrevButton.setEnabled(false);
                }else mPrevButton.setEnabled(true);
                updateQuestion();
            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionsBank.length;
                mIsCheat = false;
                updateQuestion();
                mTrueButonn.setEnabled(true);
                mFalseButton.setEnabled(true);
                if (mCurrentIndex == mQuestionsBank.length - 1){
                    mNextButton.setEnabled(false);
                }else mNextButton.setEnabled(true);
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionsBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });
        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (resultCode == REQUEST_CODE_CHEAT) {
            if (data == null) {return;}
            mIsCheat = CheatActivity.wasAnswerShown(data);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(Key_INDEX, mCurrentIndex);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void updateQuestion(){
        int question = mQuestionsBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }
    private void checkAnswer(boolean userPressedTrue){
        boolean answearIsTrue = mQuestionsBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if (mIsCheat) {messageResId = R.string.judgment_toast;}
        else {
            if (userPressedTrue == answearIsTrue) {
                messageResId = R.string.correct_toast;
                mTrueButonn.setEnabled(false);
                mFalseButton.setEnabled(false);
                numberRigthAnswers++;
            } else {
                messageResId = R.string.incorrect_toast;
                mTrueButonn.setEnabled(false);
                mFalseButton.setEnabled(false);
            }
        }
        Toast.makeText(this, messageResId,Toast.LENGTH_SHORT).show();
        if(mCurrentIndex == mQuestionsBank.length - 1) {
            Toast.makeText(this, percentOfRigthAnswers(numberRigthAnswers) + "%", Toast.LENGTH_SHORT).show();
        }
    }
    public int percentOfRigthAnswers(int numberRigthAnswers) {

        return (numberRigthAnswers*100)/mQuestionsBank.length;
    }
}
