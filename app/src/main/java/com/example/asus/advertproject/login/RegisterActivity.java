package com.example.asus.advertproject.login;

/**
 * Created by Asus on 2017. 11. 22..
 */


        import android.content.Intent;
        import android.os.Build;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.text.TextUtils;
        import android.util.Log;
        import android.view.View;
        import android.view.WindowManager;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.example.asus.advertproject.R;
        import com.example.asus.advertproject.model.User;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
        import com.google.firebase.auth.FirebaseAuthUserCollisionException;
        import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mFirstNameField;
    private EditText mLastNameField;
    private EditText mPhoneField;
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mConfirmPasswordField;

    private final static String TAG = "Register: ";
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAction));
        }

        mFirstNameField = (EditText)findViewById(R.id.et_fName);
        mLastNameField = (EditText)findViewById(R.id.et_lName);
        mEmailField = (EditText)findViewById(R.id.et_email);
        mPhoneField = (EditText)findViewById(R.id.et_phonenumber);
        mPasswordField = (EditText)findViewById(R.id.et_password);
        mConfirmPasswordField = (EditText)findViewById(R.id.et_confirmpassword);

        Button btn1=(Button)findViewById(R.id.btn_register);
        btn1.setOnClickListener(this);


        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_register:
                if(validateForm()){
                    register(mFirstNameField.getText().toString(), mLastNameField.getText().toString(),
                            mEmailField.getText().toString(),  mPhoneField.getText().toString(),
                            mPasswordField.getText().toString(), mConfirmPasswordField.getText().toString());
                }
                break;


            default:
                break;
        }
    }

    /**
     * This method registers the user if all fields are completed, and adds the data to Firebase.
     * It catches the Firebase errors that can occur, like malformed email or weak password.
     * If it passes, the details are stored under the unique User ID (first name, last name, phone number).
     * After a Toast message, it returns to the Login Activity.
     *
     * @param firstName a string input by the user
     * @param lastName a string input by the user
     * @param email a string input by the user, has to be unique
     * @param phoneNumber a string input by the user
     * @param password a string input by the user
     * @param confirmPassword a string input by the user, has to match the first password
     */
    private void register(final String firstName, final String lastName, final String email, final String phoneNumber, final String password, final String confirmPassword)
    {
        if(validateForm()) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthWeakPasswordException weakPassword) {
                                            Toast.makeText(RegisterActivity.this, "Creating new user: error",
                                                    Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "onComplete: weak_password");

                                            Toast toast = Toast.makeText(getApplicationContext(), "Password must be at least 6 characters!", Toast.LENGTH_SHORT);
                                            toast.show();
                                        } catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                            Toast.makeText(RegisterActivity.this, "Creating new user: error",
                                                    Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "onComplete: malformed_email");
                                            Toast toast = Toast.makeText(getApplicationContext(), "Not a valid email!", Toast.LENGTH_SHORT);
                                            toast.show();
                                        } catch (FirebaseAuthUserCollisionException existEmail) {
                                            Log.d(TAG, "onComplete: exist_email");
                                            Toast.makeText(RegisterActivity.this, "Wrong password!",
                                                    Toast.LENGTH_SHORT).show();
                                            // TODO: Take your action
                                        } catch (Exception e) {
                                            Toast.makeText(RegisterActivity.this, "Creating new user: error",
                                                    Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "onComplete: " + e.getMessage());
                                        }
                                    } else {
                                        Log.d(TAG, "completedYAY");

                                        String uId = mAuth.getCurrentUser().getUid();
                                        Log.d(TAG, "phone: " + phoneNumber);

                                        User user = new User.Builder()
                                                .U_id(uId)
                                                .firstName(firstName)
                                                .lastName(lastName)
                                                .email(email)
                                                .phoneNumber(phoneNumber)
                                                .bulid();
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference myRef = database.getReference("users");

                                        Log.d(TAG, "phone: " + user.getPhoneNumber());

                                        myRef.child(user.getU_id()).child("firstName").setValue(user.getFirstName());
                                        myRef.child(user.getU_id()).child("lastName").setValue(user.getLastName());
                                        myRef.child(user.getU_id()).child("phoneNumber").setValue(user.getPhoneNumber());

                                        Toast.makeText(RegisterActivity.this, "New user created",
                                                Toast.LENGTH_SHORT).show();

                                        Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                                        startActivity(i);
                                    }
                                }
                            }
                    );
        }
    }

    /**
     * This method verifies is the user filled form can be accepted or not.
     * The user can only register if this method returns true.
     *
     * @return true or false depending is all the fields are completed and the passwords match
     */
    private boolean validateForm() {
        boolean valid = true;

        String firstName = mFirstNameField.getText().toString();
        if (TextUtils.isEmpty(firstName)) {
            mFirstNameField.setError("Required");
            valid = false;
        } else {
            mFirstNameField.setError(null);
        }

        String lastName = mLastNameField.getText().toString();
        if (TextUtils.isEmpty(lastName)) {
            mLastNameField.setError("Required");
            valid = false;
        } else {
            mLastNameField.setError(null);
        }

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String phone = mPhoneField.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            mPhoneField.setError("Required");
            valid = false;
        } else {
            mPhoneField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        String confirmPassword = mConfirmPasswordField.getText().toString();
        if (TextUtils.isEmpty(confirmPassword)) {
            mConfirmPasswordField.setError("Required");
            valid = false;
        } else {
            mConfirmPasswordField.setError(null);
        }

        if(!password.equals(confirmPassword)){
            valid = false;
            mConfirmPasswordField.setError("Passwords do not match!");
        }

        return valid;
    }
}