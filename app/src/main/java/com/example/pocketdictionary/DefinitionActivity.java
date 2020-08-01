package com.example.pocketdictionary;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Locale;

public class DefinitionActivity extends AppCompatActivity {

    private TextView word;
    private TextView definition;
    private TextToSpeech textToSpeech;
    private String wordToDisplay;
    private int wordToDisplayId;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definition);

        //Setup the ActionBar
        setTitle("Definition");

        //Set the back button <-
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialize database helper
        db = new DBHelper(getApplicationContext());

        //Setup the display
        word = findViewById(R.id.txtWord);
        definition = findViewById(R.id.txtDefinition);

        wordToDisplay = getIntent().getStringExtra("word");
        Cursor res = db.getAWord(wordToDisplay);
        if (res.getCount() == 0)
        {
            Log.d("PocketDictionary", "Empty data");
        }
        else
        {
            while (res.moveToNext())
            {
                word.setText(res.getString(DBHelper.COL_WORD));
                definition.setText(res.getString(DBHelper.COL_DEFINITION));
                wordToDisplayId = res.getInt(DBHelper.COL_ID);
                break;
            }
        }

        TextView voice = (TextView) findViewById(R.id.voice);
        voice.setOnClickListener(new View.OnClickListener() {
           /* @Override
            public void onClick(View v) {
                textToSpeech = new TextToSpeech(DefinitionActivity.this, new TextToSpeech.OnInitListener() {

                    @Override
                    public void onInit(int status) {
                        textToSpeech.setLanguage(Locale.US);
                        textToSpeech.setSpeechRate(0.75f);
                        if (status == TextToSpeech.SUCCESS) {
                            //Toast.makeText(getApplicationContext(), "Status" + status, Toast.LENGTH_LONG).show();


                            textToSpeech.speak(word.getText().toString() + definition.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                });
            }*/
           @Override
           public void onClick(View v) {
               textToSpeech = new TextToSpeech(DefinitionActivity.this, new TextToSpeech.OnInitListener() {
                   @Override
                   public void onInit(int status) {
                       if (status != TextToSpeech.ERROR) {
                           textToSpeech.setLanguage(Locale.US);
                           textToSpeech.speak(word.getText().toString() , TextToSpeech.QUEUE_FLUSH, null);
                           try {
                               Thread.sleep(1500);
                           } catch (InterruptedException e) {
                               e.printStackTrace();
                           }
                           textToSpeech.speak(definition.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);

                       }
                   }
               });
           }
        });

        TextView wiki = findViewById(R.id.wiki);
        wiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DefinitionActivity.this, WikiActivity.class);
                intent.putExtra("word", word.getText().toString());
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Add icons in the action bar
        getMenuInflater().inflate(R.menu.definition_menu, menu);
        setFavoriteIcon(menu.getItem(0), getFavoriteState(wordToDisplayId));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.miFavorite:
                setFavoriteIcon(item,
                        db.setFavoriteWordById(wordToDisplayId));
                break;
            case R.id.miShare:
                shareDefinition();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void shareDefinition()
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, word.getText().toString() + " - " + definition.getText().toString());
        startActivity(Intent.createChooser(intent, "Share using"));
    }

    private boolean getFavoriteState(int wordId)
    {
        boolean state = false;
        Cursor resFavorite = db.getFavoriteWord(wordToDisplay);
        if (resFavorite.getCount() <= 0)
        {
            //Word is not yet a favorite. Set icon
            Log.d("PocketDictionary", "Not yet a favorite : " + wordToDisplay);
            state = false;
        }
        else
            state = true;

        return state;
    }

    private void setFavoriteIcon(MenuItem item, boolean isFavorite)
    {
        if (isFavorite){
            item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_white));
        }
        else{
            item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white));
        }
    }

}
