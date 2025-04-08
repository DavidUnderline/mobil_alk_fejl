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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText register_nameET;
    EditText register_emailET;
    EditText register_passwordET;
    EditText register_passwordReET;
    private FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        register_nameET = findViewById(R.id.register_name_input);
        register_emailET = findViewById(R.id.register_email_input);
        register_passwordET = findViewById(R.id.register_password_input);
        register_passwordReET = findViewById(R.id.register_passwordRe_input);
        fAuth = FirebaseAuth.getInstance();

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

        fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    toHome();
                } else{
                    Toast.makeText(RegisterActivity.this, "Error while registration process" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void toHome(){
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