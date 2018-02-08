package com.example.maxvoskr.musicplayer;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static Context contextOfApplication;

    private Button storeButton;
    private Button retrieveButton;
    private EditText keyText;
    private EditText storeText;
    private TextView message;
    private DataAccess dataAccess;
    private Song exampleSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        contextOfApplication = getApplicationContext();
        exampleSong = new Song("Song Title","Album Title","Artist",0);
        exampleSong.setTimeMS(0);
        exampleSong.setDayOfWeek(0);
        exampleSong.setTimeOfDay(0);
        exampleSong.setLikeDislike(0);
        exampleSong.setLocation(null);

        dataAccess = new DataAccess(contextOfApplication);

        storeButton = (Button) findViewById(R.id.buttonStore);
        retrieveButton = (Button) findViewById(R.id.buttonRetrieve);
        keyText = (EditText) findViewById(R.id.songTitle);
        storeText = (EditText) findViewById(R.id.messageToStore);
        message = (TextView) findViewById(R.id.showMessage);

        storeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = keyText.getText().toString();
                String messToStore = storeText.getText().toString();
                exampleSong.setLocation(messToStore);
                exampleSong.setName(key);
                dataAccess.writeData(exampleSong);


            }
        });
        retrieveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = keyText.getText().toString();
                exampleSong.setName(key);
                dataAccess.updateData(exampleSong);
                message.setText(exampleSong.getLocation());


            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
