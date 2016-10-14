package ca.ualberta.cs.lonelytwitter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class EditTweetActivity extends Activity {

    private String t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tweet);

        TextView tv = (TextView) findViewById((R.id.textTweet));

        //Intent intent = getIntent();
        //t = intent.getStringExtra("tweet");
        //tv.setText(t);

        Bundle extras = getIntent().getExtras();

        tv.setText(extras.getString("Tweet"));
    }
}

