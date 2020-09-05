package com.vertvision.verticaljump;

import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import static android.app.Activity.RESULT_OK;

public class VertFragment extends Fragment {

    double start = -1;
    double end = -1;
    double hang_time = 0.0;
    int factor = 1;
    int time_step = 10;
    double conversion_factor = 39.3701;

    String unit_string = "''";

    // Video
    VideoView videoView;

    MediaController mc;

    // Button
    Button LoadVid;
    Button start_button;
    Button end_button;
    //Button calculate;
    Button next_frame;

    // Spinner
    Spinner spinner;
    Spinner units_spinner;

    // Text
    TextView vertical_text;
    TextView hang_time_text;
    TextView start_text;
    TextView end_text;

    Uri uri;

    public static VertFragment newInstance() {
        VertFragment vertfrag = new VertFragment();
        return vertfrag;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            int a = savedInstanceState.getInt("data");
            // Uri from previous savedInstanceState
            Uri outputFileUri= savedInstanceState.getParcelable("outputFileUri");
            // saves URI from initial uri
            uri = outputFileUri;

            // Data
            String hang_time = savedInstanceState.getString("hangtime");
            hang_time_text.setText(hang_time);

            String starttime = savedInstanceState.getString("start_time");
            start_text.setText(starttime);

            String endtime = savedInstanceState.getString("output_time");
            end_text.setText(endtime);

            String vertical_jump_string = savedInstanceState.getString("vertical_jump");
            vertical_text.setText(vertical_jump_string);

            // Set View
            videoView.setVideoURI(uri);
            videoView.seekTo(a);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.vert_calc, container, false);

        // LoadVideo (Button) - When pressed, sent to gallery
        LoadVid = (Button) fragmentView.findViewById(R.id.btn_play_pause);

        // videoView (VideoView) - When video is selected, a video player is played on the screen.
        videoView = (VideoView) fragmentView.findViewById(R.id.videoView);

        // Inputs and Outputs
        start_button = (Button) fragmentView.findViewById(R.id.set_start_time);
        end_button = (Button) fragmentView.findViewById(R.id.set_end_time);
        //calculate = (Button) fragmentView.findViewById(R.id.calculate);
        next_frame = (Button) fragmentView.findViewById(R.id.nextframe);

        // Text
        vertical_text = (TextView) fragmentView.findViewById(R.id.vertical);
        start_text = (TextView) fragmentView.findViewById(R.id.start_text);
        end_text = (TextView) fragmentView.findViewById(R.id.end_text);
        hang_time_text = (TextView) fragmentView.findViewById(R.id.hang_time);

        // Spinner
        spinner = (Spinner) fragmentView.findViewById(R.id.spinner);
        units_spinner = (Spinner) fragmentView.findViewById(R.id.spinner2);

        return fragmentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // position of video
        outState.putInt("data", videoView.getCurrentPosition());
        // videoFrame Uri
        outState.putParcelable("outputFileUri", uri);
        outState.putString("start_time", start_text.getText().toString());
        outState.putString("output_time", end_text.getText().toString());
        outState.putString("hangtime", hang_time_text.getText().toString());
        outState.putString("vertical_jump", vertical_text.getText().toString());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.speed, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        // Set Default position as 8x
        int spinnerPosition = adapter.getPosition("8x");
        spinner.setSelection(spinnerPosition);

        // Spinner Slow Motion speed selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();

                if (position == 0) {
                    factor = 1;
                    time_step = 8;
                } else if (position == 1) {
                    factor = 2;
                    time_step = 15;
                } else if (position == 2) {
                    factor = 4;
                    time_step = 20;
                } else if (position == 3) {
                    factor = 8;
                    time_step = 20;
                } else if (position == 4) {
                    factor = 16;
                    time_step = 30;
                }
                // showing a toast on selecting an item
                Toast.makeText(parent.getContext(), "Selected " + item + " Slow Mo", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.units, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        units_spinner.setAdapter(adapter2);
        // Set Default position as 8x
        int spinnerPosition2 = adapter2.getPosition("Inches");
        units_spinner.setSelection(spinnerPosition2);


        // Spinner Slow Motion speed selection
        units_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();

                //vertical
                double vertical_jump;

                double scaled_hangtime = hang_time/1000;

                if (position == 0) {
                    conversion_factor = 1;
                    unit_string = " m";

                    // set vertical jump height
                    if (hang_time != 0.0) {
                        vertical_jump = vertical_calculator(scaled_hangtime, factor, conversion_factor);

                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(vertical_jump + unit_string);
                        vertical_text.setText(stringBuilder.toString());
                    }
                } else if (position == 1) {
                    conversion_factor = 100;
                    unit_string = " cm";

                    // set vertical jump height
                    if (hang_time != 0.0) {
                        vertical_jump = vertical_calculator(scaled_hangtime, factor, conversion_factor);

                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(vertical_jump + unit_string);
                        vertical_text.setText(stringBuilder.toString());
                    }

                } else if (position == 2) {
                    conversion_factor = 39.3701;
                    unit_string = "''";

                    // set vertical jump height
                    if (hang_time != 0.0) {
                        vertical_jump = vertical_calculator(scaled_hangtime, factor, conversion_factor);

                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(vertical_jump + unit_string);
                        vertical_text.setText(stringBuilder.toString());
                    }
                } else if (position == 3) {
                    conversion_factor = 3.28084;
                    unit_string = " Feet";

                    // set vertical jump height
                    if (hang_time != 0.0) {
                        vertical_jump = vertical_calculator(scaled_hangtime, factor, conversion_factor);

                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(vertical_jump + unit_string);
                        vertical_text.setText(stringBuilder.toString());
                    }
                }
                // showing a toast on selecting an item
                Toast.makeText(parent.getContext(), "Selected " + item + " as Units", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        // allows the MediaController to be on the videoView
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        /*
                         *  add media controller
                         */
                        mc = new MediaController(getActivity());
                        videoView.setMediaController(mc);
                        /*
                         * and set its position on screen
                         */
                        mc.setAnchorView(videoView);
                    }
                });
            }
        });

        // Gallery
        LoadVid.setOnClickListener(new View.OnClickListener() {
            //starts gallery app and option
            //onclick sends to gallery
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 0);
            }
        });


        // Next Frame Button
        next_frame.setOnClickListener(new View.OnClickListener() {

            // writes start time
            @Override
            public void onClick(View v) {
                videoView.start();
                // Hacky Way of solving the frame by frame problem
                try {
                    TimeUnit.MILLISECONDS.sleep(time_step);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                videoView.pause();
            }

        });

        // Take Off
        start_button.setOnClickListener(new View.OnClickListener() {

            // writes start time
            @Override
            public void onClick(View v) {

                // Button works unless video is loaded
                if (videoView.getDuration() != -1) {

                    start = videoView.getCurrentPosition();

                    double start_time = start / 1000;

                    //to TextView
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(start_time + " sec");
                    start_text.setText(stringBuilder.toString());

                    // if start and end is not empty
                    if (end != -1) {

                        hang_time = end - start;
                        // Hang Time
                        double duration = hang_time / 1000; // to seconds

                        // Hang Time
                        StringBuilder stringBuilder1 = new StringBuilder();
                        stringBuilder1.append(duration + " sec");
                        hang_time_text.setText(stringBuilder1.toString());

                        double scaled_hangtime = hang_time / 1000;

                        //vertical
                        double vertical_jump;

                        vertical_jump = vertical_calculator(scaled_hangtime, factor, conversion_factor);

                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(vertical_jump + unit_string);
                        vertical_text.setText(stringBuilder2.toString());
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),"Please select a video from the Gallery!",Toast.LENGTH_SHORT).show();
                }
            }

        });

        // Landing
        end_button.setOnClickListener(new View.OnClickListener() {

            // Button works unless video is loaded
            @Override
            public void onClick(View v) {

                if (videoView.getDuration() != -1) {

                    end = videoView.getCurrentPosition();

                    double end_time = end / 1000;

                    //to TextView
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(end_time + " sec");
                    end_text.setText(stringBuilder.toString());

                    // if start and end is not empty
                    if (start != -1) {
                        // Hang Time
                        hang_time = end - start;
                        double duration = hang_time / 1000; // to seconds

                        // Hang Time
                        StringBuilder stringBuilder1 = new StringBuilder();
                        stringBuilder1.append(duration + " sec");
                        hang_time_text.setText(stringBuilder1.toString());

                        double scaled_hangtime = hang_time / 1000;

                        //vertical
                        double vertical_jump;

                        vertical_jump = vertical_calculator(scaled_hangtime, factor, conversion_factor);

                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(vertical_jump + unit_string);
                        vertical_text.setText(stringBuilder2.toString());
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),"Please select a video from the Gallery!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // changed from protected
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // Reset Text View
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("");
            start_text.setText(stringBuilder.toString());
            end_text.setText(stringBuilder.toString());
            hang_time_text.setText(stringBuilder.toString());

            try {
                uri = data.getData();
                videoView.setVideoURI(uri);
                // Instant Play
                videoView.seekTo(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public double vertical_calculator(double time, int factor, double conversion) {
        double gravity = 9.81;
        double velocity;
        double height;
        double inch;
        double vert_time = time / (factor * 2);

        velocity = (gravity * vert_time);
        height = ((-0.5) * gravity * vert_time * vert_time) + velocity * vert_time;

        inch = height * conversion;
        // Keeps it at 2 decimal places
        inch = Math.round(inch * 100.0) / 100.0;

        return inch;
    }
}


