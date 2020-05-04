package com.example.authenticatorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private final String TAG = "RegisterActivity";
    private String companyName;
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

    EditText emailId, password, businessName, businessAddress;
    String startTime, endTime;
    Button btnSignUp;
    TextView textSignIn;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        emailId = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        btnSignUp = findViewById(R.id.buttonUpdateSchedule);
        textSignIn = findViewById(R.id.textView3);
        businessName = findViewById(R.id.editTextBusinessName);
        businessAddress = findViewById(R.id.editTextBusinessAddress);

        //Registers user and set display name equal to business name - When signed in can get business name that way
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();

                if (email.isEmpty()) {
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                } else if (pwd.isEmpty()) {
                    password.setError("Please enter a password");
                    password.requestFocus();
                } else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(Register.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                } else if (!(email.isEmpty() && pwd.isEmpty())) {
                    mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(Register.this, "SignUp Unsuccessful, Please Try Again", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Register.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                addDisplayName();
                                saveData();
                                goToLoginActivity();
                            }
                        }
                    });
                } else {
                    Toast.makeText(Register.this, "Error has occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToLoginActivity = new Intent(Register.this, LoginActivity.class);
                startActivity(goToLoginActivity);
            }
        });
    }

    public void saveData() {
        companyName = businessName.getText().toString();
        startTime = "8:00 AM";
        endTime = "5:00 PM";

        Map<String, Object> providerData = new HashMap<>();
        providerData.put("name", companyName);
        providerData.put("email", emailId.getText().toString());
        providerData.put("address", businessAddress.getText().toString());
        providerData.put("startTime", startTime);
        providerData.put("endTime", endTime);

        DocumentReference docRef = db.collection(PATH_PROVIDER_COLLECTION).document(companyName);
        docRef.set(providerData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Saved new provider to database!");
            }
        });
    }

    public void addDisplayName() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(businessName.getText().toString()).build();
        user.updateProfile(profileUpdates);
    }

    public void goToMainActivity(View view) {
        Intent goToPage = new Intent(Register.this, MainActivity.class);
        startActivity(goToPage);
    }

    public void goToLoginActivity(){
        Intent goToPage = new Intent(Register.this, LoginActivity.class);
        startActivity(goToPage);
    }
}
