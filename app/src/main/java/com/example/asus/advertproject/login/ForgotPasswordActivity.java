package com.example.asus.advertproject.login;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus.advertproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEmailField;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAction));
        }

        mEmailField = (EditText) findViewById(R.id.et_email);

        Button btn1 = (Button) findViewById(R.id.btn_verification);
        btn1.setOnClickListener(this);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_login:
                String email = mEmailField.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmailField.setError("Required");
                    return;
                }

                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPasswordActivity.this, "Reset email sent!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(ForgotPasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                break;

            default:
                break;
        }
    }
}
