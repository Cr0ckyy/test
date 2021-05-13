package com.myapplicationdev.android.jobby;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    private Button btnLogin, btnRegister;

    // Firebase
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {

            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);

        }


        // Todo: Binding UI elements
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // Todo: Login Clicked Function
        btnLogin.setOnClickListener(v -> login());

        // Todo: btnRegister Clicked Function
        btnRegister.setOnClickListener(v -> register());

    }

    // Todo: Login Method
    public void login() {

        // Todo: creating and initializing relevant variables for Login Method
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.activity_login, null);
        alertDialog.setView(view);
        AlertDialog myAlertDialog = alertDialog.create();

        // Todo: get data from the AlertDialog
        EditText editTextEmail = view.findViewById(R.id.et_email);
        EditText editTextPassword = view.findViewById(R.id.et_password);
        Button buttonLogin = view.findViewById(R.id.btnLoginForActivityLogin);

        editTextEmail.setTextColor(Color.parseColor("#FFFFFF"));
        editTextPassword.setTextColor(Color.parseColor("#FFFFFF"));

        buttonLogin.setOnClickListener(v -> {

            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            // Verification for user inputs
            if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                editTextEmail.setError("Email is required to login.");
                editTextPassword.setError("Password is required to login.");
                return;
            }

            if (TextUtils.isEmpty(email)) {
                editTextEmail.setError("Email is required to login.");
                return;
            } else if (TextUtils.isEmpty(password)) {
                editTextPassword.setError("Password is required to login.");
                return;
            }


            // Todo: linking with firebase for Login Method
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

                // if Login is successfully complete , user will be sent to HomeActivity
                if (task.isSuccessful()) {
                    String toastMsg = String.format("\nEmail: %s\nPassword: %s\nYour Login is successfully complete.", email, password);
                    Log.i("MainActivity\n", toastMsg);
                    Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);

                    // dismiss dialog after Login is successfully complete
                    myAlertDialog.dismiss();
                }

            });

        });

        myAlertDialog.show();
    }

    // Todo: Register Method
    public void register() {

        // Todo: creating and initializing relevant variables for Register Method
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.activity_register, null);
        alertDialog.setView(view);
        AlertDialog myAlertDialog = alertDialog.create();

        // get data from the AlertDialog
        EditText editTextEmail = view.findViewById(R.id.et_email);
        EditText editTextPassword = view.findViewById(R.id.et_password);
        Button buttonRegister = view.findViewById(R.id.btnLoginForActivityRegister);

        editTextEmail.setTextColor(Color.parseColor("#FFFFFF"));
        editTextPassword.setTextColor(Color.parseColor("#FFFFFF"));

        buttonRegister.setOnClickListener(v -> {

            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            // Verification for user inputs
            if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                editTextEmail.setError("Email is required to register.");
                editTextPassword.setError("Password is required to register.");
                return;
            }

            if (TextUtils.isEmpty(email)) {
                editTextEmail.setError("Email is required to register.");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                editTextPassword.setError("Password is required to register.");
                return;
            }

            // Todo: linking with firebase for Register Method
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

                // if registration is successfully complete , user will be sent to HomeActivity
                if (task.isSuccessful()) {

                    String toastMsg = String.format("\nEmail: %s\nPassword: %s\nYour Register is successfully complete.", email, password);
                    Log.i("MainActivity\n", toastMsg);

                    Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);

                    // dismiss dialog after Login is successfully complete
                    myAlertDialog.dismiss();
                }

            });

        });
        myAlertDialog.show();
    }

}