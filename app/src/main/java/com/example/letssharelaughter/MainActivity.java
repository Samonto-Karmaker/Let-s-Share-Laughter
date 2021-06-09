package com.example.letssharelaughter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    ImageView meme_imageView;
    Button share_button;
    Button next_button;
    ProgressBar loading;
    String image_url = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        meme_imageView = (ImageView) findViewById(R.id.meme_imageView);
        share_button = (Button) findViewById(R.id.share_button);
        next_button = (Button) findViewById(R.id.next_button);
        loading = (ProgressBar) findViewById(R.id.loading);

        share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Hey, checkout this funny meme: " + image_url);
                startActivity(Intent.createChooser(intent, "Share this meme using..."));

            }
        });

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                load_meme(MainActivity.this);

            }
        });

        load_meme(this);

    }



    /**
     * In this method we will load the meme by using an API call.
     * To handle the API call I have used Volley. Just search Volley for API calls, then copy the code.
     * As I am using an API with a JSONObject so I had to modify it.
     * This will get the URL of the meme.
     * I have used Glide to load the meme. Just search Glide. You will find the required repositories and dependencies.
     * Now Glide has a method "with" which takes an activity as argument. To pass the activity, I took the current activity
     * in this method as an argument in this method.
     */
    private void load_meme(final Activity activity){

        loading.setVisibility(View.VISIBLE);

        String url ="https://meme-api.herokuapp.com/gimme";

        // Request a JSONObject response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            image_url = response.getString("url");
                            // Image loading with Glide.
                            Glide.with(activity).load(image_url).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    loading.setVisibility(View.INVISIBLE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    loading.setVisibility(View.INVISIBLE);
                                    return false;
                                }
                            }).into(meme_imageView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }

}