package com.myapplicationdev.android.jobby;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myapplicationdev.android.jobby.model.JobListing;

import java.text.DateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {


    String firebaseKey, globalJobTitle, globalJobDescription, globalExpectedMonthlySalary, globalQualification, globalPostedDate;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    // firebase elements
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // TODO: Binding firebase elements
        firebaseAuth = FirebaseAuth.getInstance();
        // Todo: create unique Firebase User so that each user can only see & access their own data and not others
        FirebaseUser firebaseCurrentUser = firebaseAuth.getCurrentUser();
        // Todo: create distinc variable to store user ID
        String firebaseCurrentUserId = firebaseCurrentUser.getUid();

        // TODO: ****creating database needed sql codes ****
        databaseReference = FirebaseDatabase.getInstance().getReference().child("JobbyJobListing").child(firebaseCurrentUserId);


        // TODO: Binding UI elements
        floatingActionButton = findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.recyclerView);

        Toolbar toolbar = findViewById(R.id.toolBar);
        //  toolbar.setTitle("test application");
        // toolbar.setTitle("Jobby");
        setSupportActionBar(toolbar);

        // Todo: create LinearLayoutManager to display a list of items with item text and, optionally,
        //  an icon to identify the type of item.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        // Todo: applying following methods to ensure data is displayed in descending order
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(linearLayoutManager);


        floatingActionButton.setOnClickListener(v -> add());


    }

    public void add() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.activity_add, null);
        alertDialog.setView(view);
        AlertDialog dialog = alertDialog.create();

        // obtain data from the AlertDialog View
        final EditText etJobTitle = view.findViewById(R.id.etJobTitle);
        final EditText etJobDescription = view.findViewById(R.id.etJobDescription);
        final EditText etExpectedMonthlySalary = view.findViewById(R.id.etExpectedMonthlySalary);
        final EditText etQualification = view.findViewById(R.id.etQualification);
        final Button buttonAdd = view.findViewById(R.id.btnAdd);

        buttonAdd.setOnClickListener(v -> {

            // get text value from the AlertDialog View's data
            String jobTitle = etJobTitle.getText().toString().trim();
            String jobDescription = etJobDescription.getText().toString().trim();
            String expectedMonthlySalary = etExpectedMonthlySalary.getText().toString().trim();
            String qualification = etQualification.getText().toString().trim();


            // Verification for user inputs when adding data
            if (TextUtils.isEmpty(jobTitle) && TextUtils.isEmpty(jobDescription) && TextUtils.isEmpty(expectedMonthlySalary) && TextUtils.isEmpty(qualification)) {
                etJobTitle.setError("job Title is required to input.");
                etJobDescription.setError("job Description is required to input.");
                etExpectedMonthlySalary.setError("Expected Monthly Salary is required to input.");
                etQualification.setError("Qualification is required to input.");

                return;
            }

            if (TextUtils.isEmpty(jobTitle)) {
                etJobTitle.setError("job Title is required to input.");
                return;
            } else if (TextUtils.isEmpty(jobDescription)) {
                etJobDescription.setError("job Description is required to input.");
                return;
            } else if (TextUtils.isEmpty(expectedMonthlySalary)) {
                etExpectedMonthlySalary.setError("Expected Monthly Salary is required to input.");
                return;
            } else if (TextUtils.isEmpty(qualification)) {
                etQualification.setError("Qualification is required to input.");
                return;
            }

            // Todo: generate id for firebase
            String id = databaseReference.push().getKey();
            String date = DateFormat.getDateInstance().format(new Date());

//            String jobTitle;
//            String jobDescription;
//            String expectedMonthlySalary;
//            String qualification;
//            String id;

            JobListing jobListingData = new JobListing(jobTitle, jobDescription, expectedMonthlySalary, qualification, date, id);
            databaseReference.child(id).setValue(jobListingData);

            String logInfo = String.format("%S", databaseReference.child(id).setValue(jobListingData));
            Log.i("Inserted Data", logInfo);

            dialog.dismiss();


        });


        dialog.show();


    }

    protected void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<JobListing, MyViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<JobListing, MyViewHolder>(JobListing.class, R.layout.activity_get, MyViewHolder.class, databaseReference) {

            @Override
            protected void populateViewHolder(MyViewHolder myViewHolder, JobListing jobListing, int i) {

                myViewHolder.setJobTitle(jobListing.getJobTitle());
                myViewHolder.setJobDescription(jobListing.getJobDescription());
                myViewHolder.setExpectedMonthlySalary(jobListing.getExpectedMonthlySalary());
                myViewHolder.setQualification(jobListing.getQualification());
                myViewHolder.setPostedDate(jobListing.getpostedDate());

                myViewHolder.myView.setOnClickListener(v -> {


                    firebaseKey = getRef(i).getKey();
                    globalJobTitle = jobListing.getJobTitle();
                    globalJobDescription = jobListing.getJobDescription();
                    globalExpectedMonthlySalary = jobListing.getExpectedMonthlySalary();
                    globalQualification = jobListing.getQualification();
                    globalPostedDate = jobListing.getpostedDate();

                    edit();
                });


            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public void edit() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.activity_edit_delete, null);
        alertDialog.setView(view);
        AlertDialog dialog = alertDialog.create();
        //dialog.show();

        // obtain data from the AlertDialog View
        final EditText etJobTitle = view.findViewById(R.id.etJobTitle);
        final EditText etJobDescription = view.findViewById(R.id.etJobDescription);
        final EditText etExpectedMonthlySalary = view.findViewById(R.id.etExpectedMonthlySalary);
        final EditText etQualification = view.findViewById(R.id.etQualification);


        // updating data
        etJobTitle.setText(globalJobTitle);
        etJobTitle.setSelection(globalJobTitle.length());

        etJobDescription.setText(globalJobDescription);
        etJobDescription.setSelection(globalJobDescription.length());

        etExpectedMonthlySalary.setText(globalExpectedMonthlySalary);
        etExpectedMonthlySalary.setSelection(globalExpectedMonthlySalary.length());

        etQualification.setText(globalQualification);
        etQualification.setSelection(globalQualification.length());


        final Button buttonDelete = view.findViewById(R.id.btnDelete);
        final Button buttonUpdate = view.findViewById(R.id.btnUpdate);


        buttonUpdate.setOnClickListener(v -> {

            globalJobTitle = etJobTitle.getText().toString().trim();
            globalJobDescription = etJobTitle.getText().toString().trim();
            globalExpectedMonthlySalary = etJobTitle.getText().toString().trim();
            globalQualification = etJobTitle.getText().toString().trim();
            String globalPostedDate = DateFormat.getDateInstance().format(new Date());


            JobListing updatedJobListing = new JobListing(globalJobTitle, globalJobDescription, globalExpectedMonthlySalary, globalQualification, globalPostedDate, firebaseKey);

            databaseReference.child(firebaseKey).setValue(updatedJobListing);

            dialog.dismiss();

        });

        buttonDelete.setOnClickListener(v -> {
            databaseReference.child(firebaseKey).removeValue();
            dialog.dismiss();
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                firebaseAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        View myView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myView = itemView;
        }

        // set Job Title from AlertDialog
        public void setJobTitle(String title) {
            TextView myJobTitle = myView.findViewById(R.id.tvJobTitle);
            myJobTitle.setText(title);
        }

        // set Job Description from AlertDialog
        public void setJobDescription(String jobDescription) {
            TextView myJobDescription = myView.findViewById(R.id.tvJobDescription);
            myJobDescription.setText(jobDescription);
        }

        // set Expected Monthly Salary from AlertDialog
        public void setExpectedMonthlySalary(String expectedMonthlySalary) {
            TextView myExpectedMonthlySalary = myView.findViewById(R.id.tvExpectedMonthlySalary);
            myExpectedMonthlySalary.setText(expectedMonthlySalary);
        }

        // set Qualification from AlertDialog
        public void setQualification(String qualification) {
            TextView myQualification = myView.findViewById(R.id.tvQualification);
            myQualification.setText(qualification);
        }

        // set DAte from AlertDialog
        public void setPostedDate(String postedDate) {
            TextView myPostedDate = myView.findViewById(R.id.tvPostedDate);
            myPostedDate.setText(postedDate);
        }

    }

}