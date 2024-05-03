package com.example.mad_final;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_final.R;
import com.example.mad_final.data.database.PetContract;
import com.example.mad_final.data.database.PetDbHelper;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class VideoFeed extends AppCompatActivity {

    public static final String ACTION_NEW_VIDEOS_AVAILABLE = "com.example.mad_final.NEW_VIDEOS_AVAILABLE";
    private static final int NUM_SEARCH_QUERIES = 1;
    private String youtubeApiKey;
    private RecyclerView recyclerView;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_feed);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.youtubeFeedRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load YouTube API key from config file
        loadYoutubeApiKey();

        // Fetch videos about dogs from YouTube initially
        fetchVideos();

        // Setup handler to refresh feed every 10 seconds
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchVideos();
                // Refresh again after 10 seconds
                handler.postDelayed(this, 10000);
            }
        }, 10000);

        // Set the back button listener
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Destroy the current activity and go back
                finish();
            }
        });
    }


    private BroadcastReceiver newVideosReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("VideoFeed", "New videos broadcast received");
            Toast.makeText(context, "New videos are available!", Toast.LENGTH_SHORT).show();
        }
    };


    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(VideoFeed.ACTION_NEW_VIDEOS_AVAILABLE);
        registerReceiver(newVideosReceiver, filter, Context.RECEIVER_EXPORTED);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(newVideosReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove any pending callbacks to prevent memory leaks
        handler.removeCallbacksAndMessages(null);
    }

    private void loadYoutubeApiKey() {
        try {
            // Load properties from config file
            Properties properties = new Properties();
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("config.properties");
            properties.load(inputStream);

            youtubeApiKey = properties.getProperty("youtube_api_key");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load YouTube API key", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchVideos() {
        new FetchVideosTask(this).execute();
    }

    private class FetchVideosTask extends AsyncTask<Void, Void, List<SearchResult>> {

        private Context context;

        public FetchVideosTask(Context context) {
            this.context = context;
        }

        @Override
        protected List<SearchResult> doInBackground(Void... voids) {
            // Fetch videos about dogs from YouTube
            List<SearchResult> searchResults = fetchVideosAboutDogs();

            // Send broadcast if new videos are available
            if (searchResults != null && !searchResults.isEmpty()) {
                Intent newVideosIntent = new Intent(VideoFeed.ACTION_NEW_VIDEOS_AVAILABLE);
                Log.d("BroadcastLogger", "Sending new videos broadcast...");
                context.sendBroadcast(newVideosIntent);
                Log.d("BroadcastLogger", "Broadcast sent.");

            }

            return searchResults;
        }

        @Override
        protected void onPostExecute(List<SearchResult> searchResults) {
            super.onPostExecute(searchResults);

            // Populate RecyclerView with searchResults
            if (searchResults != null) {
                // Process the search results
                VideoAdapter adapter = new VideoAdapter(searchResults, VideoFeed.this);
                recyclerView.setAdapter(adapter);
            } else {
                System.out.println("No search results found.");
            }
        }
    }

    private List<SearchResult> fetchVideosAboutDogs() {
        // Build a new authorized API client service.
        YouTube youtubeService = null;
        try {
            youtubeService = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    null)
                    .setApplicationName("YourAppName")
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            // Handle error
            return null;
        }

        // Get a list of user-provided species from the database
        List<String> userSpecies = getSpeciesFromDatabase();

        // Define additional keywords (optional)
        List<String> additionalKeywords = Arrays.asList("funny", "pet training", "cute");

        // Define additional details keywords
        // List<String> additionalDetailsKeywords = Arrays.asList("hobbies", "favorite toys", "favorite food", "allergies");

        // Generate random search queries
        List<String> searchQueries = new ArrayList<>();
        for (int i = 0; i < NUM_SEARCH_QUERIES; i++) { // Adjust NUM_SEARCH_QUERIES for desired number
            // Randomly select a species
            int randomSpeciesIndex = new Random().nextInt(userSpecies.size());
            String randomSpecies = userSpecies.get(randomSpeciesIndex);

            // Randomly select additional keywords
            List<String> selectedAdditionalKeywords = new ArrayList<>();
            for (int j = 0; j < additionalKeywords.size(); j++) {
                if (new Random().nextBoolean()) {
                    selectedAdditionalKeywords.add(additionalKeywords.get(j));
                }
            }
            String randomAdditionalKeywords = String.join(" ", selectedAdditionalKeywords);

            // Randomly select additional details keywords
//            List<String> selectedAdditionalDetailsKeywords = new ArrayList<>();
//            for (int j = 0; j < additionalDetailsKeywords.size(); j++) {
//                if (new Random().nextBoolean()) {
//                    selectedAdditionalDetailsKeywords.add(additionalDetailsKeywords.get(j));
//                }
//            }
//            String randomAdditionalDetailsKeywords = String.join(" ", selectedAdditionalDetailsKeywords);

            //String searchQuery = randomSpecies + " " + randomAdditionalKeywords + " " + randomAdditionalDetailsKeywords;
            String searchQuery = randomSpecies + " " + randomAdditionalKeywords;
            searchQueries.add(searchQuery);
        }

        // Combine search results from all queries
        List<SearchResult> allSearchResults = new ArrayList<>();
        for (String searchQuery : searchQueries) {
            try {
                // Define the API request for retrieving search results.
                YouTube.Search.List search = youtubeService.search().list(Collections.singletonList("id,snippet"));

                // Set your developer key from the config file.
                search.setKey(youtubeApiKey);

                // Add parameters to limit search to videos and set the current search query.
                search.setType(Collections.singletonList("video"));
                search.setQ(searchQuery);

                // Restrict the search results to only include videos uploaded within the past month.
                search.setPublishedAfter(String.valueOf(new DateTime(System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000))));

                // Restrict the search results to only include videos in English.
                search.setRelevanceLanguage("en");

                // Add thumbnails to the search results
                search.setFields("items(id/videoId,snippet/title,snippet/description,snippet/thumbnails/default/url)");

                // Call the API and store results
                SearchListResponse searchResponse = search.execute();
                allSearchResults.addAll(searchResponse.getItems());
            } catch (IOException e) {
                e.printStackTrace();
                // Handle error for individual search query
            }
        }

        return allSearchResults;
    }

    public List<String> getSpeciesFromDatabase() {
        List<String> speciesList = new ArrayList<>();

        // Get a readable database instance using 'this' as context
        PetDbHelper dbHelper = new PetDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection to retrieve only the species column
        String[] projection = {
                PetContract.PetEntry.COLUMN_PET_SPECIES
        };

        // Query the database for all species
        Cursor cursor = db.query(
                PetContract.PetEntry.TABLE_NAME,   // The table to query
                projection,                       // The columns to return
                null,                              // Selection criteria (no filter here)
                null,                              // Selection arguments (no arguments here)
                null,                              // Group by (no grouping)
                null,                              // Having (no filtering based on groups)
                null                               // Order by (no sorting)
        );

        try {
            if (cursor.moveToFirst()) {
                do {
                    // Extract the species from the cursor
                    @SuppressLint("Range")
                    String species = cursor.getString(cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_SPECIES));
                    speciesList.add(species);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        // Close the database connection (optional, since PetDbHelper uses close() in onUpgrade)
        // db.close();

        return speciesList;
    }
}
