package com.example.lastse;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Check extends AppCompatActivity {
    private final String TAG = "check";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    Set<String> tables = new HashSet<>();


    TextView userNameText, tablesText;
    Button back;
    String userEmail, userName;


    Button searchButton;//조회버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        setTable();
        userNameText = (TextView)findViewById(R.id.userName);
        tablesText = (TextView)findViewById(R.id.tableList);
        if(user != null){
            userEmail = user.getEmail();
            userName = user.getDisplayName();
            if(userName == null){
                String[] eSplit = userEmail.split("@");
                userName = eSplit[0];
            }
        }
        userNameText.setText(userName+"님이 예약하신 내용");

        tablesText.setText("조회버튼을 눌러주세요");
        searchButton=(Button)findViewById(R.id.search_btn);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTable();
                String sumTable="";
                for (Iterator<String> it = tables.iterator(); it.hasNext(); ) {
                    String tableValue = it.next();
                    sumTable = sumTable+tableValue+"\n";
                }
                Toast.makeText(getApplicationContext(),"예약하신 테이블:" +sumTable, Toast.LENGTH_SHORT).show();
                tablesText.setText(sumTable);
            }
        });

        // 돌아가기 버튼
        back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                setResult(4,intent);
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
        setTable();
    }

    public void setTable(){
        Log.d(TAG, "setTable / ");
        Query myTableQuery = mRootRef.child("tables");
        myTableQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "setTable / onDataChange");
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    Table table = postSnapshot.getValue(Table.class);
                    if(table.reserved.equals(userEmail)){
                        String tableName = "table"+String.valueOf(table.table_num);
                        tables.add(tableName);

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, String.valueOf(databaseError));
            }
        });
    }

}