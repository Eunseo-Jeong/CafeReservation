package com.example.lastse;


import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private final String TAG = "signUp";
    EditText email_join;
    EditText pwd_join;
    EditText name_join;
    private Button register_btn, back;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    boolean resSuccess=false;

    private DatabaseReference mPostReference;
    String email, password, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String TAG1 = "register";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        firebaseAuth = FirebaseAuth.getInstance();


        email_join = (EditText) findViewById(R.id.edit_email);
        pwd_join = (EditText) findViewById(R.id.edit_password);
        name_join =(EditText) findViewById(R.id.edit_name);

        register_btn = (Button) findViewById(R.id.register);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = email_join.getText().toString();
                password = pwd_join.getText().toString();
                name= name_join.getText().toString();

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    Log.d(TAG, "signInWithEmail:success / user name: "+name);
                                    Toast.makeText(getApplicationContext(), "축하합니다. 회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show();
                                    user = firebaseAuth.getCurrentUser();
                                    resSuccess = true;
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(name)
                                            .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                                            .build();

                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "User profile updated."+user.getDisplayName());
                                                    }
                                                }
                                            });
                                    email_join.setEnabled(false);
                                    pwd_join.setEnabled(false);
                                    name_join.setEnabled(false);
                                    register_btn.setEnabled(false);
                                    register_btn.setVisibility(View.INVISIBLE);
                                } else {
                                    Toast.makeText(SignUpActivity.this, "등록 에러", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
            }
        });
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                if(resSuccess){
                    bundle.putString("ID", email_join.getText().toString());
                    bundle.putString("password", pwd_join.getText().toString());

                }else{
                    bundle.putString("ID", "");
                    bundle.putString("password", "");
                }
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left2, R.anim.slide_left);
    }

    public void postFirebaseDatabase(boolean add){
        mPostReference = FirebaseDatabase.getInstance().getReference();
        String key = mPostReference.child("id_list").push().getKey();

        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        String name = name_join.getText().toString();
        String email = email_join.getText().toString();

        if(add){
            User post = new User(name, email);
            postValues = post.toMap();
        }
        childUpdates.put("/id_list/" + key, postValues);
        mPostReference.updateChildren(childUpdates);

    }
}