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
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.advertproject.R;
import com.example.asus.advertproject.main.MainActivity;
import com.example.asus.advertproject.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEmailField, mPasswordField;
    private Button buttonLogin, buttonRegister, buttonSkip;
    private TextView tvForgot;
    private GoogleSignInClient mGoogleSignInClient;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private final static String TAG = "Login: ";
    //private final static int RC_SIGN_IN = 1;
    private final static int RC_GOOGLE_SIGN_IN = 1;
    public final static String AUTH_ERROR = "FIRBSE_AUTH_ERROR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAction));
        }

        mEmailField = findViewById(R.id.et_email);
        mPasswordField = findViewById(R.id.et_password);

        buttonLogin = findViewById(R.id.btn_login);
        buttonLogin.setOnClickListener(this);
        buttonRegister = findViewById(R.id.btn_register);
        buttonRegister.setOnClickListener(this);
        tvForgot = findViewById(R.id.tv_forgot);
        tvForgot.setOnClickListener(this);
        buttonSkip = findViewById(R.id.btn_skip);
        buttonSkip.setOnClickListener(this);
        findViewById(R.id.btn_google).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //Used by the system to automatically log-in users
        if (user != null) {
            //logIn(user);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                /**
                 * Checks is the validateForm function returns true after the Login button has been pressed
                 */
                if(validateForm()){
                    signIn(mEmailField.getText().toString(),mPasswordField.getText().toString());
                }
                break;

            case R.id.btn_register:
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                break;

            case R.id.btn_google:
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
                break;

            case R.id.tv_forgot:
                Intent i2 = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i2);
                break;

            case R.id.btn_skip:
                finish();
                break;

            default:
                break;
        }
    }

    /**
     * This method gets called when the Login button is pressed and after it passes the validateForm method.
     *
     * @param email a string input by user specifying email address
     * @param password a string input by user specifying password
     */
    private void signIn(final String email, final String password) {
        Log.d(TAG, "signIn: " + email);
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");

                            logIn(mAuth.getCurrentUser());
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            try
                            {
                                throw task.getException();
                            }
                            catch (Exception e)
                            {
                                Log.d(TAG, "onComplete12222: " + e.getMessage());
                                Toast.makeText(LoginActivity.this, "Login failed!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        // hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    /**
     * The user has been successfully logged in via email and password or google sign in.
     * The method displays a greeting toast including the user's first name, then calls the Main Activity.
     *
     * @param user the registered Firebase user
     */
    private void logIn(FirebaseUser user) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        User user = dataSnapshot.getValue(User.class);

                        if (user != null) {
                            Toast.makeText(getApplicationContext(), "Welcome, " + user.getFirstName() + "!", Toast.LENGTH_SHORT)
                                    .show();
                        }
                        else Log.d(TAG, "Error Null user!!!!");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Database query unsuccessful", Toast.LENGTH_SHORT)
                                .show();

                    }
                });
        if (user.isAnonymous()) {
            Toast.makeText(getApplicationContext(), "Welcome anonymous user", Toast.LENGTH_SHORT)
                    .show();
        }

        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "Google1: " + "SignwithGoogle");
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                signInWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getApplicationContext(), "Sign In Failed : " +
                        e.getStatusCode(), Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    /**
     * Then signs in with the recieved Google account.
     *
     * @param acct the Google account the user selected
     */
    private void signInWithGoogle(final GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            addUserToDatabase(user, acct);
                            logIn(user);
                        } else {
                            Log.e(AUTH_ERROR, "Task Unsuccesful, firebase Authentication");
                        }
                    }
                });
    }

    /**
     * Adds user's details to Firebase (first name, last name and email)
     * The user ID is always the same, so no duplicates are created.
     *
     * @param user the current user of the app
     * @param acct the Google account the user selected
     */
    private void addUserToDatabase(FirebaseUser user, GoogleSignInAccount acct) {
        DatabaseReference usersDatabaseReference = databaseReference.child("users").child(user.getUid());
        usersDatabaseReference.child("firstName").setValue(acct.getGivenName());
        usersDatabaseReference.child("lastName").setValue(acct.getFamilyName());
        usersDatabaseReference.child("email").setValue(acct.getEmail());
    }

    /**
     * This function checks if the Login form is valid or not
     *
     * @return whether the fields have been completed
     */
    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }
}