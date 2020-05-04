package com.example.authenticatorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SetAvailabilityActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayAdapter adapter;

    //Intent keys for getStringExtra() retrieval
    private static final String COMPANY_NAME = "CompanyName";
    private static final String APPOINTMENT_DATE = "AppointmentDate";
    private static final String APPOINTMENT_TIME = "AppointmentTime";
    private static final String CLIENT_NAME = "ClientName";
    private static final String CLIENT_PHONE_NUMBER = "ClientPhoneNumber";

    //Database collection/path names
    private static final String PATH_PROVIDER_COLLECTION = "Providers";
    private static final String PATH_DAILY_SCHEDULE = "Daily Schedule";
    private static final String PATH_APPOINTMENT_TIMES = "Appointment Times";

    private String companyName;
    private String startTime;
    private String endTime;

    public Spinner spinnerStartTime;
    public Spinner spinnerEndTime;
//    FirebaseAuth mFirebaseAuth;
//    FirebaseAuth.AuthStateListener mAuthStateListener;
//    FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_availability);

        spinnerStartTime = (Spinner) findViewById(R.id.spinnerStartTime);
        spinnerEndTime = (Spinner) findViewById(R.id.spinnerEndTime);

        ArrayList<String> schedule = new ArrayList<>();
        schedule.add("12:00 AM");
        schedule.add("1:00 AM");
        schedule.add("2:00 AM");
        schedule.add("3:00 AM");
        schedule.add("4:00 AM");
        schedule.add("5:00 AM");
        schedule.add("6:00 AM");
        schedule.add("7:00 AM");
        schedule.add("8:00 AM");
        schedule.add("9:00 AM");
        schedule.add("10:00 AM");
        schedule.add("11:00 AM");
        schedule.add("12:00 PM");
        schedule.add("1:00 PM");
        schedule.add("2:00 PM");
        schedule.add("3:00 PM");
        schedule.add("4:00 PM");
        schedule.add("5:00 PM");
        schedule.add("6:00 PM");
        schedule.add("7:00 PM");
        schedule.add("8:00 PM");
        schedule.add("9:00 PM");
        schedule.add("10:00 PM");
        schedule.add("11:00 PM");

        //Setup the adapter for the drop downs
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, schedule);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Populate spinners with the adapter information
        spinnerStartTime.setAdapter(adapter);
        spinnerEndTime.setAdapter(adapter);


        //
        Button btnLogout = (Button) findViewById(R.id.buttonUpdateSchedule);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get signed in user's company name (Display name set in register activity)
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                companyName = user.getDisplayName();
                startTime = spinnerStartTime.getSelectedItem().toString();
                endTime = spinnerEndTime.getSelectedItem().toString();

                DocumentReference docRef = db.collection(PATH_PROVIDER_COLLECTION).document(companyName);
                docRef.update(
                        "startTime", startTime,
                        "endTime", endTime
                ).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SetAvailabilityActivity.this, "Schedule Updated!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        //Go back to HomeActivity
        Button buttonGoHome = (Button) findViewById(R.id.buttonGoToHome);
        buttonGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSetSchedule = new Intent(SetAvailabilityActivity.this, HomeActivity.class);
                startActivity(goToSetSchedule);
            }
        });
    }
}
