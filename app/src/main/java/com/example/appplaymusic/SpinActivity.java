package com.example.appplaymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SpinActivity extends AppCompatActivity {
    Handler handler = new Handler();
    public static Button button;
    public static TextView textView;
    public static ImageView img1, img2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin);
        final ProgressBar progressBar = findViewById(R.id.ProgressBar01);

        // show the progress bar
        progressBar.getProgress();
    }
}