package com.example.lastse;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.time.Instant;

public class Login extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    private static final int REGISTER_CODE = 1001;
    private final String TAG ="login";
    private FirebaseAuth mAuth;// ...



    private Button register, login, back;//회원가입

    private EditText edit_email;//아이디입력
    private EditText edit_pw;//패스워드 입력
    private GoogleSignInClient mGoogleSignInClient;


    FirebaseAuth firebaseAuth;
    SignInButton google_login;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();//firebase auth 받기
        mAuth = FirebaseAuth.getInstance();


        edit_email = (EditText) findViewById(R.id.editId);//이메일 전달받음
        edit_pw = (EditText) findViewById(R.id.editPassword);//패스워드 전달 받음

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        google_login = findViewById(R.id.register_google);
        google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "google login button");
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        //가입버튼
        register = (Button) findViewById(R.id.register_email);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUpActivity.class);
                startActivityForResult(intent, REGISTER_CODE);
                overridePendingTransition(R.anim.slide_left2, R.anim.slide_left);
            }
        });

        //로그인 버튼
        login = (Button) findViewById(R.id.login_btn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = edit_email.getText().toString().trim();
                final String pwd = edit_pw.getText().toString().trim();
                if (email.equals("")||pwd.equals("")){
                    Toast.makeText(getApplicationContext(), "ID 혹은 비밀번호를 다시 확인해 주세요", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.signInWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Login.this, "환영합니다", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        Bundle bundle = new Bundle();

                                        bundle.putBoolean("isLogin",true);

                                        intent.putExtras(bundle);
                                        setResult(2, intent);
                                        finish();

                                    } else {
                                        Toast.makeText(getApplicationContext(), "ID 혹은 비밀번호를 다시 확인해 주세요", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
        back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                bundle.putBoolean("isLogin",false);

                intent.putExtras(bundle);
                setResult(2, intent);
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left2, R.anim.slide_left);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "onActivityResult");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }else if(requestCode == REGISTER_CODE){
            String ID = data.getStringExtra("ID");
            String password = data.getStringExtra("password");
            edit_email.setText(ID);
            edit_pw.setText(password);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, user.getEmail());
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();

                            bundle.putBoolean("isLogin",true);

                            intent.putExtras(bundle);
                            setResult(2, intent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }

                        // ...
                    }
                });
    }


}