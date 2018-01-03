package com.example.asus.advertproject.profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.asus.advertproject.R;
import com.example.asus.advertproject.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewProfileActivity extends AppCompatActivity {
    private static final String TAG = "ViewProfileActivity";
    private TextView firstNameTextView,
            lastNameTextView,
            emailAddressTextView,
            phoneNumberTextView;
    private DatabaseReference databaseReference;
    private String userEmail;
    private ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        firstNameTextView = findViewById(R.id.text_view_first_name);
        lastNameTextView = findViewById(R.id.text_view_last_name);
        emailAddressTextView = findViewById(R.id.text_view_email);
        phoneNumberTextView = findViewById(R.id.text_view_phone_number);
        profileImageView=findViewById(R.id.profileIV);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        User user = dataSnapshot.getValue(User.class);

                        if (user != null) {
                            firstNameTextView.setText(user.getFirstName());
                            lastNameTextView.setText(user.getLastName());

                            FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
                            FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                            userEmail = mFirebaseUser.getEmail();

                            emailAddressTextView.setText(userEmail);
                            phoneNumberTextView.setText(user.getPhoneNumber());
                            Glide.with(getApplicationContext())
                                    .load(user.getPhotoURL())
                                    .into(profileImageView);
                        }
                        else Log.d(TAG, "Error Null user!!!!");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Database query unsuccessful", Toast.LENGTH_SHORT)
                                .show();

                    }
                });
    }
}
