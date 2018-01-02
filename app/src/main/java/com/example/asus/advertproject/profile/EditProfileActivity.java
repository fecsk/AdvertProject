package com.example.asus.advertproject.profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus.advertproject.R;
import com.example.asus.advertproject.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "EditProfileActivity";
    private EditText firstNameEditText,
            lastNameEditText,
            phoneNumberEditText;
    private Button saveChangesButton;
    private DatabaseReference databaseReference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        firstNameEditText = findViewById(R.id.et_first_name);
        lastNameEditText = findViewById(R.id.et_last_name);
        phoneNumberEditText = findViewById(R.id.et_phone_number);

        saveChangesButton = findViewById(R.id.btn_save_changes);
        saveChangesButton.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        User user = dataSnapshot.getValue(User.class);

                        if (user != null) {
                            firstNameEditText.setHint(user.getFirstName());
                            lastNameEditText.setHint(user.getLastName());
                            phoneNumberEditText.setHint(user.getPhoneNumber());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save_changes:
                final String firstName = firstNameEditText.getText().toString();
                final String lastName = lastNameEditText.getText().toString();
                final String phoneNumber = phoneNumberEditText.getText().toString();
                if(verifyEmptyAll(firstNameEditText, lastNameEditText, phoneNumberEditText)) {

                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    User user = dataSnapshot.getValue(User.class);
                                    if (user != null) {
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference myRef = database.getReference("users");

                                        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
                                        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                                        userID = mFirebaseUser.getUid();

                                        if (!TextUtils.isEmpty(firstName)) {
                                            myRef.child(userID).child("firstName").setValue(firstName);
                                        }
                                        if (!TextUtils.isEmpty(lastName)) {
                                            myRef.child(userID).child("lastName").setValue(lastName);
                                        }
                                        if (!TextUtils.isEmpty(phoneNumber)) {
                                            myRef.child(userID).child("phoneNumber").setValue(phoneNumber);
                                        }
                                        Toast.makeText(getApplicationContext(), "Your changes have been saved", Toast.LENGTH_SHORT)
                                                .show();
                                        finish();
                                    } else Log.d(TAG, "Error Null user!!!!");
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(), "Database query unsuccessful", Toast.LENGTH_SHORT)
                                            .show();

                                }
                            });
                    break;
                }
                else break;

            default:
                break;
        }
    }

    public boolean verifyEmptyAll(EditText firstNameEditText, EditText lastNameEditText, EditText phoneNumberEditText){
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        if(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName) && TextUtils.isEmpty(phoneNumber)){
            firstNameEditText.setError("All fields are empty!");
            lastNameEditText.setError("All fields are empty!");
            phoneNumberEditText.setError("All fields are empty!");
            return false;
        }
        else return true;
    }
}
