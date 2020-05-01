package com.example.meong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

public class ImageSingleActivity extends AppCompatActivity {
    private static final String TAG = "ImageSingleActivity";
    private String thumbnailUrl;
    private String downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_single);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        thumbnailUrl = intent.getStringExtra("THUMBNAIL_URL");
        downloadUrl = intent.getStringExtra("DOWNLOAD_URL");

        ImageView thumbnail = findViewById(R.id.thumbnail);
        Glide.with(this)
                .load(thumbnailUrl)
                .into(thumbnail);

        ImageView pexelsLogo = findViewById(R.id.pexels_logo);
        pexelsLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, (Uri) Uri.parse("https://www.pexels.com/"));
                startActivity(intent);
            }
        });

        LinearLayout download = findViewById(R.id.downloadImg);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webpage = Uri.parse(downloadUrl);

                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        this.finish();

        return true;
    }
}
