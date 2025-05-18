package com.example.allaskereso_portal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseUser user;
    private FirestoreHelper firestoreHelper;
    private LinearLayout linView;
    private EditText emailInput;
    private EditText nameInput;
    private EditText reminderInput;

    private static final String LOG = RegisterActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        Context context = this;
        reminderInput = findViewById(R.id.reminder_input_text);
        AlarmHelper.createReminderChannel(context);


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        user = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

//        linView = findViewById(R.id.linview);
//        linView.setLayoutManager(new GridLayoutManager(this, 1));

        FirebaseApp.initializeApp(this);
        firestoreHelper = new FirestoreHelper();

        Button submitChangeButton = findViewById(R.id.profile_button_submitedit);
        Button deleteaccountButton = findViewById(R.id.profile_button_deleteaccount);
        emailInput = findViewById(R.id.profile_email_input);
        nameInput = findViewById(R.id.profile_name_input);

        submitChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
        deleteaccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteaccount();
            }
        });

        Button setreminderbutton = findViewById(R.id.set_reminder_button);
        Button deletereminderbutton = findViewById(R.id.delete_reminder_button);
        setreminderbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReminder(v);
            }
        });
        deletereminderbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelReminder(v);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void setReminder(View view) {
//        NotificationHelper.showNotification(this, "reminder", "inside setreminder");
        String message = reminderInput.getText().toString();

//        NotificationHelper.showNotification(context, "asd", message);
        if (!message.isEmpty()) {
            Toast.makeText(this, "Successfully set reminder!", Toast.LENGTH_SHORT).show();
            Log.v(LOG, String.valueOf(this));
            AlarmHelper.setReminderAlarm(this, 2000, message);
        } else {
            Toast.makeText(this, "Please give a reminder message!", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelReminder(View view) {
        AlarmHelper.cancelReminderAlarm(this);
        Toast.makeText(this, "Reminder deleted!", Toast.LENGTH_SHORT).show();
    }

    public void updateProfile(){
        Log.v(LOG,"inside submit");
        String email = emailInput.getText().toString();
        String name = nameInput.getText().toString();

        if(!email.isEmpty()){
            firestoreHelper.updateUserEmail(user.getUid(), email, new FirestoreHelper.UpdateUserProfileCallback() {
                @Override
                public void onSuccess() {
                    Log.i(LOG, "Display name update successful!");
                }

                @Override
                public void onError(Exception e) {
                    Log.e(LOG, "Display name update failed: " + e.getMessage());
                }
            });
        }

        if(!name.isEmpty()){
            Log.v(LOG," ---- USER -----" + user.getUid());
            firestoreHelper.updateUserFirestore(user.getUid(), name, new FirestoreHelper.UpdateUserProfileCallback() {
                @Override
                public void onSuccess() {
                    Log.i(LOG, "Display name update successful!");
                }

                @Override
                public void onError(Exception e) {
                    Log.e(LOG, "Display name update failed: " + e.getMessage());
                }
            });
        }
    }

    public void deleteaccount(){
        firestoreHelper.deleteCurrentUser(new FirestoreHelper.DeleteUserCallback() {
            @Override
            public void onSuccess() {
                Log.v(LOG,"deleted user");
                finish();
                toHome();
            }

            @Override
            public void onError(Exception e) {
                Log.v(LOG,"couldnt delete user");
            }
        });
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
                toHome();
                return true;

            case "home_profile":
                Log.d("MenuItem", "profile");
                return true;

            case "home_jobs":
                Log.d("MenuItem", "jobs");
                toHome();
                return true;

            default:
                return super.onOptionsItemSelected(option);
        }
    }

    public void toHome(){
        Intent home = new Intent(this, HomeActivity.class);
        startActivity(home);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        return super.onPrepareOptionsMenu(menu);
    }
}