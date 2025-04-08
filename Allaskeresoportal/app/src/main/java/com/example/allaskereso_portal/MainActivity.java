package com.example.allaskereso_portal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText emailET;
    EditText passwordET;
    ImageView home;
    ImageView prof_image;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        emailET = findViewById(R.id.login_email_input);
        passwordET = findViewById(R.id.login_password_input);
        fAuth = FirebaseAuth.getInstance();
        home = findViewById(R.id.home_image);

        prof_image = findViewById(R.id.prof_image);
        prof_image.setOnClickListener(v -> prof_image.animate()
            .setDuration(1000)
            .rotationYBy(360f)
            .start());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void login(View view){
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        if(email.isEmpty() || password.isEmpty()){
            Snackbar.make(findViewById(android.R.id.content), "Empty field / fields", Snackbar.LENGTH_SHORT).show();
            return;
        }

        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    toHome();

                } else{
                    Snackbar.make(findViewById(android.R.id.content), "There's no such user with given credentials", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void toHome(){
        Intent home = new Intent(this, HomeActivity.class);
        startActivity(home);
    }

    private static final String LOG = RegisterActivity.class.getName();

    public void toHomeV(View view) {
        Animation ani = AnimationUtils.loadAnimation(this, R.anim.scale);
        home.startAnimation(ani);

        fAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.i(LOG, "home reached");
                    toHome();
                } else{
                    Snackbar.make(findViewById(android.R.id.content), "Can't reach home", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void to_register(View view){
        Intent reg = new Intent(this, RegisterActivity.class);
        startActivity(reg);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}