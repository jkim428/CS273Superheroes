package edu.orangecoastcollege.cs273.jkim428.cs273superheroes;

import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "CS273 Superheroes";
    private static final int SUPERHEROES_IN_QUIZ = 10;

    private Button[] mButtons = new Button[4];
    private List<Superhero> mAllSuperheroesList;
    private List<Superhero> mQuizSuperheroesList;
    private Superhero mCorrectSuperhero;
    private int mTotalGuesses;
    private int mCorrectGuesses;
    private SecureRandom rng;
    private Handler handler;

    private TextView mQuestionNumberTextView;
    private ImageView mSuperheroImageView;
    private TextView mAnswerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mQuizSuperheroesList = new ArrayList<>(SUPERHEROES_IN_QUIZ);
        rng = new SecureRandom();
        handler = new Handler();

        mQuestionNumberTextView = (TextView) findViewById(R.id.questionNumberTextView);
        mSuperheroImageView = (ImageView) findViewById(R.id.superheroImageView);
        mAnswerTextView = (TextView) findViewById(R.id.answerTextView);

        mButtons[0] = (Button) findViewById(R.id.button);
        mButtons[1] = (Button) findViewById(R.id.button2);
        mButtons[2] = (Button) findViewById(R.id.button3);
        mButtons[3] = (Button) findViewById(R.id.button4);

        mQuestionNumberTextView.setText(getString(R.string.question, 1, SUPERHEROES_IN_QUIZ));

        try {
            mAllSuperheroesList = JSONLoader.loadJSONFromAsset(this);
        } catch (IOException e) {
            Log.e(TAG, "Error loading JSON file", e);
        }

        resetQuiz();
    }

    /**
     * Sets up and starts a new quiz.
     */
    public void resetQuiz()
    {
        mCorrectGuesses = 0;
        mTotalGuesses = 0;
        mQuizSuperheroesList.clear();

        int size = mAllSuperheroesList.size();
        int randomPosition;
        Superhero randomSuperhero;
        while (mQuizSuperheroesList.size() < SUPERHEROES_IN_QUIZ)
        {
            randomPosition = rng.nextInt(size);
            randomSuperhero = mAllSuperheroesList.get(randomPosition);
            if (!mQuizSuperheroesList.contains(randomSuperhero))
                mQuizSuperheroesList.add(randomSuperhero);
        }

        loadNextSuperhero();
    }

    /**
     * Method initiates the process of loading the next superhero for the quiz, showing
     * the superhero's image and then 4 buttons, one of which contains the correct answer.
     */
    private void loadNextSuperhero()
    {
        mCorrectSuperhero = mQuizSuperheroesList.remove(0);
        mAnswerTextView.setText("");

        int questionNumber = mCorrectGuesses + 1;
        mQuestionNumberTextView.setText(getString(R.string.question, questionNumber, SUPERHEROES_IN_QUIZ));

        AssetManager am = getAssets();

        try {
            InputStream stream = am.open(mCorrectSuperhero.getFileName());
            Drawable image = Drawable.createFromStream(stream, mCorrectSuperhero.getName());
            mSuperheroImageView.setImageDrawable(image);

        } catch (IOException e) {
            Log.e(TAG, "Error loading image " + mCorrectSuperhero.getFileName(), e);
        }

        do {
            Collections.shuffle(mAllSuperheroesList);
        }
        while (!mAllSuperheroesList.subList(0, mButtons.length).contains(mCorrectSuperhero));

        for (int i = 0; i < mButtons.length; i++)
        {
            mButtons[i].setEnabled(true);
            mButtons[i].setText(mAllSuperheroesList.get(i).getName());
        }
    }

    /**
     * Handles the click event of one of the 4 buttons indicating the guess of a superhero's name, superpower, or one thing
     * to match the superhero image displayed.  If the guess is correct, the superhero's name, superpower, or one thing (in GREEN) will be shown,
     * followed by a slight delay of 2 seconds, then the next superhero will be loaded.  Otherwise, the
     * word "Incorrect Guess" will be shown in RED and the button will be disabled.
     * @param v
     */
    public void makeGuess(View v)
    {
        Button clickedButton = (Button) v;
        String guess = clickedButton.getText().toString();

        mTotalGuesses++;

        if (guess.equals(mCorrectSuperhero.getName()))
        {
            for (Button b : mButtons)
                b.setEnabled(false);

            mCorrectGuesses++;
            mAnswerTextView.setText(mCorrectSuperhero.getName());
            mAnswerTextView.setTextColor(ContextCompat.getColor(this, R.color.correct_answer));

            if (mCorrectGuesses < SUPERHEROES_IN_QUIZ)
            {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextSuperhero();
                    }
                }, 2000);
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.results, mTotalGuesses, (double) mCorrectGuesses / mTotalGuesses * 100));
                builder.setPositiveButton(getString(R.string.reset_quiz), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resetQuiz();
                    }
                });
                builder.setCancelable(false);
                builder.create();
                builder.show();
            }
        }

        else
        {
            clickedButton.setEnabled(false);
            mAnswerTextView.setText(getString(R.string.incorrect_answer));
            mAnswerTextView.setTextColor(ContextCompat.getColor(this, R.color.incorrect_answer));
        }
    }

}
