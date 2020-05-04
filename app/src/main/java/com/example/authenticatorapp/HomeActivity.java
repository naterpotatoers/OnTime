package com.example.authenticatorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private ArrayAdapter adapter;
    final ArrayList<String> schedule = new ArrayList<>();
    private CollectionReference colRef;
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
    private String appointmentDate;
    private String appointmentTime;
    private ListView listViewSchedule;

    private String TAG = "HomeActivity";

    private Button btnLogout;
    private TextView textViewMiniTitle;
    private Button buttonSetSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        buttonSetSchedule = (Button) findViewById(R.id.buttonGoToHome);
        listViewSchedule = (ListView) findViewById(R.id.listViewSchedule);
        textViewMiniTitle = (TextView) findViewById(R.id.textViewMiniTitle);
        btnLogout = findViewById(R.id.buttonUpdateSchedule);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, schedule);
        //Get signed in user's company name (Display name set in register activity)
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        companyName = user.getDisplayName();
        //Set title to company name
        textViewMiniTitle.setText(companyName);

        //Gets the days with appointments scheduled
        colRef = FirebaseFirestore.getInstance().collection(PATH_PROVIDER_COLLECTION).document(companyName).collection(PATH_DAILY_SCHEDULE);
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        schedule.add(document.getId());
                    }
                    listViewSchedule.setAdapter(adapter);
                    Log.d(TAG, schedule.toString());
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        //Date list click functionality - Go to ScheduleViewActivity for specific date selected
        listViewSchedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                Intent goToSelectedBusinessActivity = new Intent(HomeActivity.this, ScheduleViewActivity.class);
                appointmentDate = (String) adapter.getItemAtPosition(position).toString();
                goToSelectedBusinessActivity.putExtra(APPOINTMENT_DATE, appointmentDate);
                goToSelectedBusinessActivity.putExtra(COMPANY_NAME, companyName);
                startActivity(goToSelectedBusinessActivity);
            }
        });

        //Logout button functionality
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToRegistration = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intToRegistration);
                Toast.makeText(HomeActivity.this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        //Go to setAvailabilityActivity button functionality
        buttonSetSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSetSchedule = new Intent(HomeActivity.this, SetAvailabilityActivity.class);
                startActivity(goToSetSchedule);
            }
        });
    }
}
/**
 * colRef = db.collection(PATH_PROVIDER_COLLECTION);
 * Query emailQuery = colRef.whereEqualTo("email", companyEmail);
 * //Pulls the data on provider using email
 * emailQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
 *
 * @Override public void onComplete(@NonNull Task<QuerySnapshot> task) {
 * if (task.isSuccessful()) {
 * for (QueryDocumentSnapshot document : task.getResult())
 * {
 * data = document.getData();
 * companyName = document.getString("name");
 * tempCompanyName = document.getString("name");
 * Log.d(TAG, document.getId() + "\nLINE 101: " + document.getData());
 * Log.d(TAG, "\ndata info: " + data.toString());
 * textViewMiniTitle.setText(companyName);
 * }
 * }
 * else {
 * Log.d(TAG, "Error getting documents: " + task.getException());
 * }
 * }
 * });
 * <p>
 * //Get current date
 * //        SimpleDateFormat formatter = new SimpleDateFormat("M-dd-yyyy");
 * //        Date date = new Date();
 * //        String dateStr = formatter.format(date);
 **/