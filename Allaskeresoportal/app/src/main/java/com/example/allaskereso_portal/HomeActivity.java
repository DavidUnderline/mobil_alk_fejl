package com.example.allaskereso_portal;

import android.annotation.SuppressLint;
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

import java.io.CharArrayReader;
import java.util.ArrayList;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    private static final String LOG = RegisterActivity.class.getName();
    private FirebaseUser user;

    private RecyclerView recViewv;
    private ArrayList<Job> jobs;
    private JobItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        if(user != null){
            Snackbar.make(findViewById(android.R.id.content), "Home page", Snackbar.LENGTH_SHORT).show();
            Log.i(LOG, "user ! null");
        } else{
            Snackbar.make(findViewById(android.R.id.content), "Not registered user", Snackbar.LENGTH_SHORT).show();
            Log.i(LOG, "not registered");
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        jobs = new ArrayList<>();

        recViewv = findViewById(R.id.recview);
        recViewv.setLayoutManager(new GridLayoutManager(this, 1));

        adapter = new JobItemAdapter(this, jobs);

        recViewv.setAdapter(adapter);


        init();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

//    @SuppressLint("NotifyDataSetChanged")
    public void init() {
        //todo: debug with log here and in adapter
        Log.i(LOG, "init-ben");

        String title = "test1";
        String category = "test category";
        String salary = "0";
        String description = "test description";
//    todo: arrays, jobs.clear(), loop;

        jobs.add(new Job(title, category, salary, description));
        adapter.notifyDataSetChanged();
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
                return true;

            case "home_profile":
                Log.d("MenuItem", "profile");
                return true;

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