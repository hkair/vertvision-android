package com.vertvision.verticaljump;

import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    VertFragment vertfrag;
    private ImageButton helpButton;
    VideoView video;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        helpButton = (ImageButton) findViewById(R.id.help_button);

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(),"Help Guide!",Toast.LENGTH_SHORT).show();

                VideoView video = (VideoView) findViewById(R.id.videoView);
                String path = "android.resource://" + getPackageName() + "/raw/vert";
                video.setVideoURI(Uri.parse(path));
                video.start();
            }
        });

        if (savedInstanceState == null) {
            vertfrag = new VertFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container ,vertfrag,"TAG").commit();
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
        }

    }
}



