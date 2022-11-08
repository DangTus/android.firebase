package com.dmt.dangtus.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private ImageButton imbEye;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        anhXa();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        imbEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEyePassword();
            }
        });

        edtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE) {
                    login();
                    return true;
                }
                return false;
            }
        });
    }

    private void login() {
        String user = edtUsername.getText().toString().trim();
        String pass = edtPassword.getText().toString().trim();
        if(user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email và password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
                    edtPassword.setText("");
                }
            }
        });
    }

    private void setEyePassword() {
        if((Integer) imbEye.getTag() == R.drawable.ic_eye_hide) {
            imbEye.setImageResource(R.drawable.ic_eye);
            imbEye.setTag(R.drawable.ic_eye);

            //129 la kieu password
            edtPassword.setInputType(129);
        } else if ((Integer) imbEye.getTag() == R.drawable.ic_eye) {
            imbEye.setImageResource(R.drawable.ic_eye_hide);
            imbEye.setTag(R.drawable.ic_eye_hide);

            edtPassword.setInputType(InputType.TYPE_CLASS_TEXT);
        }

        //dua vi tri con tro ve phia cuoi van ban
        int position = edtPassword.length();
        Editable etext = edtPassword.getText();
        Selection.setSelection(etext, position);
    }

    private void anhXa() {
        edtUsername = findViewById(R.id.userNameEditText);
        edtPassword = findViewById(R.id.passwordEditText);
        btnLogin = findViewById(R.id.loginButton);

        imbEye = findViewById(R.id.eyeIMB);
        imbEye.setTag(R.drawable.ic_eye);
    }
}