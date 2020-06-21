package com.example.lastse;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "main";

    FirebaseUser user;
    FirebaseAuth mAuth;
    DatabaseReference mRootRef, mReservedRef;
    //test용


    String name;
    TextView textView;
    Boolean isLogin=false;

    private DrawerLayout drawerLayout;
    private View drawerView;
    Button btnReservation, btnCheck, btnCancel, btnCafe, btnLogin, btnLogout,upDate;
    Button seat1, seat2, seat3, seat4, seat5, seat6, seat7, seat8, seat9, seat10;

    Map<Button, String> buttonStateList = new HashMap<>();
    Map<Button, String> mapTable = new HashMap<>();
    Set<Table> tableInfoList = new HashSet<>();
    String available = "available";
    String reserved = "reserved";
    String reservedTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate");
        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mReservedRef = mRootRef.child("tables");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout=(DrawerLayout)findViewById(R.id.layout_main);
        drawerView=(View)findViewById(R.id.drawerView);
        drawerLayout.openDrawer(drawerView);
        textView = findViewById(R.id.clientText);
        textView.setText("고객님");


        seat1 = (Button) findViewById(R.id.seat1);
        seat2 = (Button) findViewById(R.id.seat2);
        seat3 = (Button) findViewById(R.id.seat3);
        seat4 = (Button) findViewById(R.id.seat4);
        seat5 = (Button) findViewById(R.id.seat5);
        seat6 = (Button) findViewById(R.id.seat6);
        seat7 = (Button) findViewById(R.id.seat7);
        seat8 = (Button) findViewById(R.id.seat8);
        seat9 = (Button) findViewById(R.id.seat9);
        seat10 = (Button) findViewById(R.id.seat10);
        buttonStateList.put(seat1, reserved);
        buttonStateList.put(seat2, reserved);
        buttonStateList.put(seat3, reserved);
        buttonStateList.put(seat4, reserved);
        buttonStateList.put(seat5, reserved);
        buttonStateList.put(seat6, reserved);
        buttonStateList.put(seat7, reserved);
        buttonStateList.put(seat8, reserved);
        buttonStateList.put(seat9, reserved);
        buttonStateList.put(seat10, reserved);
        mapTable.put(seat1, "table1");
        mapTable.put(seat2, "table2");
        mapTable.put(seat3, "table3");
        mapTable.put(seat4, "table4");
        mapTable.put(seat5, "table5");
        mapTable.put(seat6, "table6");
        mapTable.put(seat7, "table7");
        mapTable.put(seat8, "table8");
        mapTable.put(seat9, "table9");
        mapTable.put(seat10, "table10");

        //예약하기 버튼
        btnReservation=findViewById(R.id.btnReservation);
        btnReservation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                reservationPopup();
            }
        });
        //예약확인 버튼
        btnCheck=findViewById(R.id.btnCheck);
        btnCheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                reservationCheckPopup();
            }
        });


        //카페정보 버튼
        btnCafe=findViewById(R.id.btnCafe);
        btnCafe.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this, Cafeinfo.class);
                startActivityForResult(intent,1);

                overridePendingTransition(R.anim.slide_left2, R.anim.slide_left);

            }
        });
        //로그인 버튼
        btnLogin=findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivityForResult(intent,1);
                overridePendingTransition(R.anim.slide_left2, R.anim.slide_left);
            }
        });
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        Log.d(TAG, "onCreate/ setTable and set View");
        setTable();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart/ setTable and set View");
        super.onStart();
        user = mAuth.getCurrentUser();
        if(user != null){
            reservedTable = getReservedTable();
            Log.d(TAG, "onStart / "+user.getEmail());
        }else{
            Log.d(TAG, "onStart /  user null");

        }

        setTable();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        user = mAuth.getCurrentUser();
        if(user==null){
            Log.d(TAG, "user null");
        }else{
            Log.d(TAG, user.getEmail());
        }
        if (requestCode == 1) {
            if (resultCode == 2) {   ///로그인 갔다가 돌아오는거
                isLogin = data.getBooleanExtra("isLogin", false);
                if (isLogin) {
                    String userName = null;
                    String userEmail = null;
                    if(user != null){
                        userName= user.getDisplayName();
                        userEmail =user.getEmail();
                    }

                    if (userName != null) {
                        name = userName;
                    } else {
                        String[] eSplit = userEmail.split("@");
                        name = eSplit[0];
                    }
                    textView.setText(name + "님");
                }
            }
            if (resultCode == 3) {  ///예약하기 갔다가 돌아오는거
                Toast.makeText(getApplicationContext(), "예약하기 완료", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == 4) {    ///예약확인 갔다가 돌아오는거
                Toast.makeText(getApplicationContext(), "예약확인 완료", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == 5) {    ///예약삭제 갔다가 돌아오는거
                Toast.makeText(getApplicationContext(), "예약삭제 완료", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == 6) {    ///카페정보 갔다가 돌아오는거
                Toast.makeText(getApplicationContext(), "카페정보 완료", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void reservationPopup(){
        if(isLogin) {//로그인이 된 경우
            Log.d(TAG, "reservationPopup / "+reservedTable);

            if(reservedTable==null){
                Intent intent = new Intent(MainActivity.this, Reservation.class);
                startActivityForResult(intent,1);
                overridePendingTransition(R.anim.slide_left2, R.anim.slide_left);
            }else{
                AlertDialog.Builder reservedAlert = new AlertDialog.Builder(MainActivity.this);
                reservedAlert.setTitle("안내");
                reservedAlert.setMessage("이미 예약된 정보가 존재합니다.(1인 1예약)");
                reservedAlert.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {} });
                reservedAlert.show();
            }
        }
        else{
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("메세지");
            alert.setMessage("로그인이 필요합니다");
            alert.setPositiveButton("로그인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivityForResult(intent,1);
                    overridePendingTransition(R.anim.slide_left2, R.anim.slide_left);
                }
            });
            alert.setNegativeButton("닫기",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {}
            });
            alert.show();
        }
    }
    public void reservationCheckPopup() {
        Log.d(TAG, "reservationCheckPopup");
        if (!isLogin) {
            AlertDialog.Builder logAlert = new AlertDialog.Builder(MainActivity.this);
            logAlert.setTitle("메세지");
            logAlert.setMessage("로그인이 필요합니다");
            logAlert.setPositiveButton("로그인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivityForResult(intent, 1);
                    overridePendingTransition(R.anim.slide_left2, R.anim.slide_left);
                }
            });
            logAlert.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            logAlert.show();
        }
        else {
            AlertDialog.Builder resCheck = new AlertDialog.Builder(MainActivity.this);
            resCheck.setTitle("예약 현황");
            if(reservedTable==null){
                resCheck.setMessage("예약된 정보가 없습니다.");
            }else{
                resCheck.setMessage(reservedTable);
                resCheck.setPositiveButton("예약 취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mReservedRef.child(reservedTable).child(available).setValue(true);
                        mReservedRef.child(reservedTable).child(reserved).setValue("None");
                        reservedTable = null;
                        Toast.makeText(getApplicationContext(), "예약이 취소되었습니다", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            resCheck.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                }
            });
            resCheck.show();
        }
    }
    public void setTable(){
        Query myTableQuery = mRootRef.child("tables");
        myTableQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "setTable / onDataChange");
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    tableInfoList.add(postSnapshot.getValue(Table.class));
                    Table table = postSnapshot.getValue(Table.class);
                    if(table.table_num==1){
                        if(table.available == true){
                            buttonStateList.put(seat1, available);
                        }else{
                            buttonStateList.put(seat1, table.reserved);
                        }
                    }else  if(table.table_num==2){
                        if(table.available == true){
                            buttonStateList.put(seat2, available);
                        }else{
                            buttonStateList.put(seat2, table.reserved);
                        }

                    }else if(table.table_num==3){
                        if(table.available == true){
                            buttonStateList.put(seat3, available);
                        }else{
                            buttonStateList.put(seat3, table.reserved);
                        }
                    }else if(table.table_num==4){
                        if(table.available == true){
                            buttonStateList.put(seat4, available);
                        }else{
                            buttonStateList.put(seat4, table.reserved);
                        }
                    }else if(table.table_num==5){
                        if(table.available == true){
                            buttonStateList.put(seat5, available);
                        }else{
                            buttonStateList.put(seat5, table.reserved);
                        }
                    }else if(table.table_num==6){
                        if(table.available == true){
                            buttonStateList.put(seat6, available);
                        }else{
                            buttonStateList.put(seat6, table.reserved);
                        }
                    }else if(table.table_num==7){
                        if(table.available == true){
                            buttonStateList.put(seat7, available);
                        }else{
                            buttonStateList.put(seat7, table.reserved);
                        }
                    }else if(table.table_num==8){
                        if(table.available == true){
                            buttonStateList.put(seat8, available);
                        }else{
                            buttonStateList.put(seat8, table.reserved);
                        }
                    }else if(table.table_num==9){
                        if(table.available == true){
                            buttonStateList.put(seat9, available);
                        }else{
                            buttonStateList.put(seat9, table.reserved);
                        }
                    }else if(table.table_num==10){
                        if(table.available == true){
                            buttonStateList.put(seat10, available);
                        }else{
                            buttonStateList.put(seat10, table.reserved);
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "table_num exception", Toast.LENGTH_SHORT).show();
                    }
                }
                setMainTablesView();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, String.valueOf(databaseError));
            }
        });
    }//database 에서 정보를 받아서 현재 테이블 셋팅, 항상 초기화용으로 사용해야함
    public void setMainTablesView(){
        Log.d(TAG, "set View");
        for(Button button:buttonStateList.keySet()){
            if(buttonStateList.get(button).equals(available)){
                button.setBackgroundResource(R.drawable.button_background);
                button.setEnabled(false);//이건 의미없는 기능
            }else{
                button.setBackgroundResource(R.drawable.button_reserve);
                button.setEnabled(false);
            }
        }
    }//메인에 있는 테이블 view update
    public String getReservedTable(){
        Log.d(TAG, "getReservedTable");
        String reservedInfo = null;
        if(user != null){
            for(Button button:buttonStateList.keySet()){
                if (buttonStateList.get(button).equals(user.getEmail())){
                    reservedInfo = mapTable.get(button);
                }
            }
        }else{Log.d(TAG, "getReservedTable / user null");}
        return reservedInfo;
    }//예약된 테이블 가져오기
    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        isLogin=false;
                        textView.setText("고객님");
                        Toast.makeText(getApplicationContext(), "로그아웃 되엇습니다.", Toast.LENGTH_SHORT).show();

                    }
                });
    }
}