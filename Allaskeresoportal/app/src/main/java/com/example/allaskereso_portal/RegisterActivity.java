package com.example.allaskereso_portal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class RegisterActivity extends AppCompatActivity {
    EditText register_nameET;
    EditText register_emailET;
    EditText register_passwordET;
    EditText register_passwordReET;
    private FirebaseAuth fAuth;
    private FirestoreHelper firestoreHelper;
    private FirebaseUser user;

    private static final String LOG = MainActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.menu);

        register_nameET = findViewById(R.id.register_name_input);
        register_emailET = findViewById(R.id.register_email_input);
        register_passwordET = findViewById(R.id.register_password_input);
        register_passwordReET = findViewById(R.id.register_passwordRe_input);
        fAuth = FirebaseAuth.getInstance();

        FirebaseApp.initializeApp(this);
        firestoreHelper = new FirestoreHelper();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

//    private static final String LOG = RegisterActivity.class.getName();

    public void register(View view){
        String name = register_nameET.getText().toString();
        String email = register_emailET.getText().toString();
        String password = register_passwordET.getText().toString();
        String passwordRe = register_passwordReET.getText().toString();

        if(!password.equals(passwordRe)){
            Snackbar.make(findViewById(android.R.id.content), "Passwords doesn't match", Snackbar.LENGTH_SHORT).show();
            return;

        } else if(name.isEmpty() || email.isEmpty() || password.isEmpty() || passwordRe.isEmpty()){
            Snackbar.make(findViewById(android.R.id.content), "Empty field / fields", Snackbar.LENGTH_SHORT).show();
            return;
        }

        firestoreHelper.createUserWithEmailAndPassword(email, password, name, new FirestoreHelper.CreateUserCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                Log.v(LOG, "created successfully");
                auth(email, password);
            }

            @Override
            public void onError(Exception e) {
                Log.v(LOG, "couldnt register");
            }
        });
    }

    public void auth(String email, String password){
        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.v(LOG, "reg ok --> login ok");
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


    public void cancel(View view) {
        finish();
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