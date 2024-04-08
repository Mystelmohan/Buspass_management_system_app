package com.example.buspass;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {

    EditText Name, userName, ph,sour,desti;
    Button reg;
    DatabaseReference reference;
    String uid; // Variable to store the UID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Name = findViewById(R.id.name);
        userName = findViewById(R.id.username);
        ph = findViewById(R.id.Phno);
        reg = findViewById(R.id.register);
        sour=findViewById(R.id.source);
        desti=findViewById(R.id.destination);
        // Get the UID from the previous activity
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        reference = FirebaseDatabase.getInstance().getReference().child("User").child(uid);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
                Name.setText("");
                userName.setText("");
                ph.setText("");
                sour.setText("");
                desti.setText("");
                Intent loginIntent = new Intent(getApplicationContext(), Login.class);
                startActivity(loginIntent);
                finish(); // Finish current activity to prevent back navigation
            }
        });
    }

    private void insert() {
        String name1 = Name.getText().toString();
        String username1 = userName.getText().toString();
        String ph1 = ph.getText().toString();
        String sour1=sour.getText().toString();
        String desti1=desti.getText().toString();
        Users users = new Users(name1, username1, ph1,sour1,desti1);

        reference.setValue(users); // Create a field in the Realtime Database with the UID
        Toast.makeText(this, "Registered successfully", Toast.LENGTH_LONG).show();
    }
}
