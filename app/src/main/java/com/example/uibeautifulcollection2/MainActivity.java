package com.example.uibeautifulcollection2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uibeautifulcollection2.check.ValidatorUtils;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView mToggleTextView;
    private EditText edtPass;
    private EditText edtEmail1;
    private Button btnRegister;
    private Button btnLogin;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        mToggleTextView = (TextView)findViewById(R.id.tv_btn_show);
        edtEmail1 = (EditText)findViewById(R.id.edt_email);
        edtPass = (EditText)findViewById(R.id.edt_pass);
        btnLogin = (Button)findViewById(R.id.btn_login);
        btnRegister = (Button)findViewById(R.id.btn_register);
        progressBar = (ProgressBar)findViewById(R.id.progress_login);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        mToggleTextView.setVisibility(View.GONE);
        Sprite fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        edtPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edtPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edtPass.getText().length()>0){
                    mToggleTextView.setVisibility(View.VISIBLE);
                }
                else
                    mToggleTextView.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mToggleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mToggleTextView.getText()=="SHOW"){
                    mToggleTextView.setText("HIDE");
                    edtPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    edtPass.setSelection(edtPass.length());
                }
                else {
                    mToggleTextView.setText("SHOW");
                    edtPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edtPass.setSelection(edtPass.length());
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(this,ProfileActivity.class));
        }
    }

    private void userLogin() {
        String email = edtEmail1.getText().toString().trim();
        if(email.isEmpty()){
            edtEmail1.setError("Field can't be empty");
            edtEmail1.requestFocus();
            return;
        }else{
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                edtEmail1.setError("Invalid email address");
                edtEmail1.requestFocus();
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
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    finish();
                    Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                userLogin();
                break;
            case R.id.btn_register:
                finish();
                startActivity(new Intent(this,SignUpActivity.class));
                break;
        }
    }
}
