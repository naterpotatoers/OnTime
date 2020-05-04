package com.example.authenticatorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;

public class SelectTimeActivity extends AppCompatActivity {
    private ArrayAdapter adapter;
    //Intent key words
    private static final String COMPANY_NAME = "CompanyName";
    private static final String APPOINTMENT_DATE = "AppointmentDate";
    private static final String APPOINTMENT_TIME = "AppointmentTime";
    private static final String CLIENT_NAME = "ClientName";
    private static final String CLIENT_PHONE_NUMBER = "ClientPhoneNumber";
    //Database collection/path names
    private static final String PATH_PROVIDER_COLLECTION = "Providers";
    private static final String PATH_DAILY_SCHEDULE = "Daily Schedule";
    private static final String PATH_APPOINTMENT_TIMES = "Appointment Times";
    //Database setup
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference colRef = db.collection(PATH_PROVIDER_COLLECTION);
    //Logging information
    private final String TAG = "SelectTime";

    private String companyName;
    private String appointmentDate;
    private String appointmentTime;

    ListView listViewSchedule;
    TextView textViewCompanyName;
    Provider provider;
    String startTime;
    String endTime;
    ArrayList<String> schedule = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);

        listViewSchedule = (ListView) findViewById(R.id.listViewSchedule);
        textViewCompanyName = (TextView) findViewById(R.id.textViewBusinessName);

        //Getting the passed variables from previous Activity.
        Intent extraIntentInfo = getIntent();
        companyName = extraIntentInfo.getStringExtra(COMPANY_NAME);
        appointmentDate = extraIntentInfo.getStringExtra(APPOINTMENT_DATE);
        String nameAndDate = companyName + " " + appointmentDate;
        textViewCompanyName.setText(nameAndDate);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, schedule);

        //Temporary schedule till business registration completed
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

        //Gets the provider information
        final DocumentReference docRef = db.collection(PATH_PROVIDER_COLLECTION).document(companyName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        provider = document.toObject(Provider.class);
                        startTime = provider.getStartTime();
                        endTime = provider.getEndTime();
                        //Need to remove times outside set schedule
                        int from = schedule.indexOf(startTime);
                        int to = schedule.indexOf(endTime);
                        schedule.subList(to+1, schedule.size()).clear();
                        schedule.subList(0, from).clear();
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Log.d(TAG, "provider: " + provider.toString());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "Pulling data failed with ", task.getException());
                }
            }
        });

        //Gets the scheduled appointments
        colRef.document(companyName).collection(PATH_DAILY_SCHEDULE).document(appointmentDate).collection(PATH_APPOINTMENT_TIMES).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        schedule.remove(document.getId());
                    }
                    listViewSchedule.setAdapter(adapter);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        //Need to remove times outside set schedule

        listViewSchedule.setAdapter(adapter);

        listViewSchedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                Intent goToClientInformation = new Intent(SelectTimeActivity.this, ClientInformationActivity.class);
                appointmentTime = (String) adapter.getItemAtPosition(position).toString();
                goToClientInformation.putExtra(COMPANY_NAME, companyName);
                goToClientInformation.putExtra(APPOINTMENT_DATE, appointmentDate);
                goToClientInformation.putExtra(APPOINTMENT_TIME, appointmentTime);
                startActivity(goToClientInformation);
            }
        });
    }

}
