package com.example.uibeautifulcollection2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uibeautifulcollection2.check.ValidatorUtils;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtFull;
    private EditText edtUser;
    private EditText edtEmail;
    private EditText edtPass;
    private EditText edtConfirm;
    private TextView tvLogin;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_form);
        edtFull = (EditText)findViewById(R.id.edt_full_name);
        edtConfirm = (EditText)findViewById(R.id.edt_confirm);
        edtEmail = (EditText)findViewById(R.id.edt_email_resist);
        edtPass = (EditText)findViewById(R.id.edt_password_resist);
        edtUser = (EditText)findViewById(R.id.edt_user_name);
        tvLogin = (TextView)findViewById(R.id.tv_login);
        btnRegister = (Button)findViewById(R.id.btn_register_signup);
        btnRegister.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        Sprite fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        mAuth = FirebaseAuth.getInstance();
    }
    private void registerUser() {
        String fullName = edtFull.getText().toString().trim();
        if(fullName.isEmpty()){
            edtFull.setError("Field can't be empty");
            edtFull.requestFocus();
            return;
        }
        String userName = edtUser.getText().toString().trim();
        if(userName.isEmpty()){
            edtUser.setError("Field can't be empty");
            edtUser.requestFocus();
            return;
        }
        String email = edtEmail.getText().toString().trim();
        if(email.isEmpty()){
            edtEmail.setError("Field can't be empty");
            edtEmail.requestFocus();
            return;
        }else{
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                edtEmail.setError("Invalid email address");
                edtEmail.requestFocus();
                return;
            }
        }
        String password = edtPass.getText().toString().trim();
        if(password.isEmpty()){
            edtPass.setError("Field can't be empty");
            edtPass.requestFocus();
            return;
        }else {
            if(password.length()<6){
                edtPass.setError("Minimum password at least more than 6 characters");
                edtPass.requestFocus();
                return;
            }
        }
        String confirm = edtConfirm.getText().toString().trim();
        if(confirm.isEmpty()){
            edtConfirm.setError("Field can't be empty");
            edtConfirm.requestFocus();
            return;
        }else {
            if(!password.equals(confirm)){
                edtConfirm.setError("Password did not match");
                edtConfirm.requestFocus();
                return;
            }
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
//                    Toast.makeText(getApplicationContext(),"Resisted Successful",Toast.LENGTH_LONG).show();
                    finish();
                    Intent intent = new Intent(SignUpActivity.this,ProfileActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        ///This is to check if the account created
//        String mUserEmail = "thiet@gmail.com";
//        String mPassword = "password";
//
//        mAuth.createUserWithEmailAndPassword(mUserEmail, mPassword)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//
//                        Log.d("SignUpActivity",  " createUserWithEmail:onComplete:" + task.isSuccessful());
//                        // if task is successful  then AuthStateListener  will get notified you can get user details there.
//                        // if task is not successful show error
//                        if (!task.isSuccessful()) {
//                            Log.e("SignUpActivity", "onComplete: Failed=" + task.getException().getMessage());
//                            try {
//                                throw task.getException();
//                            } catch (FirebaseAuthUserCollisionException e) {
//                                // log error here
//
//                            } catch (FirebaseNetworkException e) {
//                                // log error here
//                            } catch (Exception e) {
//                                // log error here
//                            }
//
//                        } else {
//
//                            // successfully user account created
//                            // now the AuthStateListener runs the onAuthStateChanged callback
//
//                        }
//                    }
//
//                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register_signup:
                registerUser();
                break;
            case R.id.tv_login:
                finish();
                startActivity(new Intent(this,MainActivity.class));
                break;

        }
    }
}
