package com.example.meong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.meong.adapters.ImageAdapter;
import com.example.meong.interfaces.ImageAPIInterface;
import com.example.meong.models.Image;
import com.example.meong.models.ImageResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImageActivity extends AppCompatActivity implements View.OnClickListener {
    static boolean active = false;
    private static final String TAG = "MainActivity";
    private ArrayList<Image> mImages = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private ImageAdapter mAdapter;
    private boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems, page;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        recyclerInit();
        listenerInit(); // Wajib ada di activity yg ada menu bawahnya
        logoIntent();
    }

    private void logoIntent() {
        ImageView pexelsLogo = findViewById(R.id.pexels_logo);
        pexelsLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, (Uri) Uri.parse("https://www.pexels.com/"));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    private void listenerInit() {
        LinearLayout imageMenu = findViewById(R.id.image_menu),
                articleMenu = findViewById(R.id.article_menu),
                hospitalMenu = findViewById(R.id.hospital_menu);

        imageMenu.setOnClickListener(this);
        articleMenu.setOnClickListener(this);
        hospitalMenu.setOnClickListener(this);
    }

    private void recyclerInit() {
        progressBar = findViewById(R.id.progressBar);

        page = 0;

        mLayoutManager = new GridLayoutManager(
                this, 2, GridLayoutManager.VERTICAL, false);

        mRecyclerView = findViewById(R.id.image_recycler);
        mAdapter = new ImageAdapter(mImages, ImageActivity.this);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        fetchImages();

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = mLayoutManager.getChildCount();
                totalItems = mLayoutManager.getItemCount();
                scrollOutItems = mLayoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems-6)) {
                    progressBar.setVisibility(View.VISIBLE);

                    isScrolling = false;
                    fetchImages();
                }
            }
        });
    }

    private void fetchImages() {
        page++;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.pexels.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ImageAPIInterface imageAPIInterface = retrofit.create(ImageAPIInterface.class);
        Call<ImageResult> call = imageAPIInterface.getImages("cat", 20, page);

        Log.d(TAG, "loadImages: start");

        call.enqueue(new Callback<ImageResult>() {
            @Override
            public void onResponse(Call<ImageResult> call, Response<ImageResult> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: response " + response.code());
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                ImageResult imageResult = response.body();

                for (Image image : imageResult.getPhotos()) {
                    mImages.add(image);
                }

                mAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ImageResult> call, Throwable t) {
                Log.d(TAG, "onFailure: Gagal: " + t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: view: " + v.getId());

        switch (v.getId()) {
            case R.id.image_menu:
                if (!active) {
                    Intent intent = new Intent(this, ImageActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.article_menu:
                break;
            case R.id.hospital_menu:
                break;
        }
    }
}
