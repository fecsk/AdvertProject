package com.example.asus.advertproject.login;

/**
 * Created by Asus on 2017. 11. 22..
 */

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.advertproject.R;
import com.example.asus.advertproject.main.MainActivity;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


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

        mEmailField = findViewById(R.id.et_email);
        mPasswordField = findViewById(R.id.et_password);

        buttonLogin = findViewById(R.id.btn_login);
        buttonLogin.setOnClickListener(this);
        buttonRegister = findViewById(R.id.btn_register);
        buttonRegister.setOnClickListener(this);
        tvForgot = findViewById(R.id.tv_forgot);
        tvForgot.setOnClickListener(this);
        //buttonSkip = findViewById(R.id.btn_skip);
        //buttonSkip.setOnClickListener(this);
        findViewById(R.id.btn_google).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
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

            default:
                break;
        }
    }

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
                            // updateUI(user);
                            Toast.makeText(LoginActivity.this, "Login success",
                                    Toast.LENGTH_SHORT).show();
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

    private void logIn(FirebaseUser user) {
        if (!user.isAnonymous()) {
            Toast.makeText(getApplicationContext(), "Welcome back " + user.getDisplayName(), Toast.LENGTH_SHORT)
                    .show();
        } else {
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

    private void addUserToDatabase(FirebaseUser user, GoogleSignInAccount acct) {
        DatabaseReference usersDatabaseReference = databaseReference.child("users").child(user.getUid());
        usersDatabaseReference.child("firstName").setValue(acct.getGivenName());
        usersDatabaseReference.child("lastName").setValue(acct.getFamilyName());
        usersDatabaseReference.child("email").setValue(acct.getEmail());
    }

//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//        try {
//            Log.d(TAG, "GoogleHandle: " + "googleReturned3");
//            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//            FirebaseUser user = mAuth.getCurrentUser();
//            // Signed in successfully, show authenticated UI.
//            //updateUI(account);
//
//        } catch (ApiException e) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            //updateUI(null);
//        }
//    }

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