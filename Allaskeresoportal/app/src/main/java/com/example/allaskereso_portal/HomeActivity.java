package com.example.allaskereso_portal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.RouteListingPreference;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.MenuItemCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.FirebaseApp;

import java.io.CharArrayReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    private static final String LOG = RegisterActivity.class.getName();
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirestoreHelper firestoreHelper;
    private RecyclerView recViewv;

    private List<Job> jobList = new ArrayList<>();
    private JobItemAdapter jobAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        Log.v(LOG, "---- USER --- "+ user);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        jobList = new ArrayList<>();

        recViewv = findViewById(R.id.recview);
        recViewv.setLayoutManager(new GridLayoutManager(this, 1));


        FirebaseApp.initializeApp(this);
        firestoreHelper = new FirestoreHelper();

        jobAdapter = new JobItemAdapter(this, jobList);
        recViewv.setAdapter(jobAdapter);
        init();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadJobs() {
        Log.e(LOG, "-------> clicked --" + firestoreHelper);
//        firestoreHelper.exampleUsage();
        firestoreHelper.getJobs(new FirestoreHelper.FirestoreCallback() {
//            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onCallback(List<Job> jobs) {
                jobList.clear();
                jobList.addAll(jobs);
                jobAdapter.notifyDataSetChanged();
                Log.d(LOG, "Number of fetched jobs " + jobList.size());
            }

            @Override
            public void onError(Exception e) {
                Log.e(LOG, "Error while fetching jobs", e);
            }
        });
    }

    public void init() {
        loadJobs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        Log.d("Menu", "onCreateOptionsMenu called");
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.top_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem option){
        String[] selected = getResources().getResourceName(option.getItemId()).split("/");

        switch (selected[selected.length-1]){
            case "home_logout":
                Log.d("MenuItem", "logout");
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent main = new Intent(this, MainActivity.class);
                startActivity(main);
                return true;

            case "home_profile":
                Log.d("MenuItem", "profile");
                if(user != null && !user.isAnonymous()){
                    Snackbar.make(findViewById(android.R.id.content), "Home page", Snackbar.LENGTH_SHORT).show();
                    Intent profile = new Intent(this, ProfileActivity.class);
                    startActivity(profile);
                    return true;
                }
                Snackbar.make(findViewById(android.R.id.content), "Not registered user", Snackbar.LENGTH_SHORT).show();
                return false;

            case "home_jobs":
                Log.d("MenuItem", "jobs");
                return true;

            default:
                return super.onOptionsItemSelected(option);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        return super.onPrepareOptionsMenu(menu);
    }
}