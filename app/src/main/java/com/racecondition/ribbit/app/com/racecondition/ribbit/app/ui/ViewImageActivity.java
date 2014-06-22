package com.racecondition.ribbit.app.com.racecondition.ribbit.app.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.racecondition.ribbit.app.R;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;


public class ViewImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        // Show the Up button in the action bar.
        setupActionBar();

        ImageView imageView = (ImageView)findViewById(R.id.imageView);

        Uri imageUri = getIntent().getData();

        Picasso.with(this).load(imageUri.toString()).into(imageView);

        // Display image for 10 seconds then return to InboxFragment
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                finish();
            }
        }, 10*1000);

    }

    /**
     *
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }


    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
