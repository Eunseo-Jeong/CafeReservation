package com.example.lastse;



import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Objects;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Reservation extends AppCompatActivity {
    private static final String TAG = "reservation";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final CharSequence[] peopleNum = {"1인", "2인", "3인", "4인", "5인", "6인"};
    String tableName[] ={"table1", "table2","table3","table4","table5","table6","table7","table8","table9","table10"};
    String available="available";
    String reserved="reserved";
    String selected="selected";
    long now ;
    Date date;
    SimpleDateFormat sdfNow;
    String formatDate;


    Map<Button, String> buttonStateList = new HashMap<>();
    Map<Button, String> mapTable = new HashMap<>();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mTableRef, mMemRef, mTimeRef;
    DatabaseReference mInitTable, mInitMemRef, mInitTimeRef;

    Button seat1,seat2,seat3,seat4,seat5,seat6,seat7,seat8,seat9,seat10, reselect_btn, people, back, res;
    GridLayout layout;

    int nSelectItem=-1; //인원수인덱스
    int nCurrentNum = 0;
    //테스트용 버튼
    Button tableInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        mInitTable = mRootRef.child("tables");//table root 참조
        mInitMemRef = mRootRef.child("tables");//table root 참조
        mInitTimeRef = mRootRef.child("tables");//table root 참조
        layout = findViewById(R.id.resTableLayout);

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

        seat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nCurrentNum -= 4;
                changeStateEach(seat1, selected, true);
                availableTable(nCurrentNum);
            }
        });
        seat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nCurrentNum -= 2;
                changeStateEach(seat2, selected, true);
                availableTable(nCurrentNum);
            }
        });
        seat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nCurrentNum -= 2;
                changeStateEach(seat3, selected, true);
                availableTable(nCurrentNum);
            }
        });
        seat4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nCurrentNum -= 4;
                changeStateEach(seat4, selected, true);
                availableTable(nCurrentNum);
            }
        });
        seat5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nCurrentNum -= 2;
                changeStateEach(seat5, selected, true);
                availableTable(nCurrentNum);
            }
        });
        seat6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nCurrentNum -= 2;
                changeStateEach(seat6, selected, true);
                availableTable(nCurrentNum);
            }
        });
        seat7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nCurrentNum -= 6;
                changeStateEach(seat7, selected, true);
                availableTable(nCurrentNum);
            }
        });
        seat8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nCurrentNum -= 1;
                changeStateEach(seat8, selected, true);
                availableTable(nCurrentNum);
            }
        });
        seat9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nCurrentNum -= 1;
                changeStateEach(seat9, selected, true);
                availableTable(nCurrentNum);
            }
        });
        seat10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nCurrentNum -= 1;
                changeStateEach(seat10, selected, true);
                availableTable(nCurrentNum);
            }
        });
        people = (Button) findViewById(R.id.people); //인원 선택 버튼
        people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup();//팝업 메세지로 인원 선택
            }
        });

        res = (Button) findViewById(R.id.res); //최종예약버튼
        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nSelectItem == -1){
                    AlertDialog.Builder alert = new AlertDialog.Builder(Reservation.this);
                    alert.setTitle("메세지");
                    alert.setMessage("인원수를 먼저 입력해 주세요");
                    alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
                    alert.setNegativeButton("no",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
                    alert.show();
                }else
                {
                    Log.d(TAG, "예약버튼 / 예약이 진행됩니다");

                    for(Button button:buttonStateList.keySet()){
                        if (buttonStateList.get(button).equals(selected)){
                            mTableRef = mRootRef.child("tables").child(mapTable.get(button)).child((available));
                            mMemRef = mRootRef.child("tables").child(mapTable.get(button)).child(("reserved"));
                            mTimeRef = mRootRef.child("tables").child(mapTable.get(button)).child(("time"));
                            mTableRef.setValue(false);
                            mMemRef.setValue(user.getEmail());
                            // 현재시간을 msec 으로 구한다.
                            now = System.currentTimeMillis();
                            // 현재시간을 date 변수에 저장한다.
                            date = new Date(now);
                            // 시간을 나타냇 포맷을 정한다
                            sdfNow = new SimpleDateFormat("MM/dd HH:mm:ss");
                            // nowDate 변수에 값을 저장한다.
                            formatDate = sdfNow.format(date);
                            //시간을 디비에 넣는다
                            mTimeRef.setValue(formatDate);
                        }
                    }
                    Toast.makeText(getApplicationContext(),"예약이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    //이후 동작 중지, 새롭게 인원을 선택하면 다시 실행
                    Log.d(TAG, "예약버튼 / 예약 완료");
                    reselect_btn.setVisibility(View.INVISIBLE);//새로고침 비활성화
                    reselect_btn.setEnabled(false);
                    people.setVisibility(View.INVISIBLE);
                    res.setVisibility(View.INVISIBLE);

                    nSelectItem = -1;
                    updateTable(nSelectItem);
                    Log.d(TAG, "res / updateTable");
                }
            }
        });
        reselect_btn = (Button) findViewById(R.id.reselectBtn);
        reselect_btn.setEnabled(false);
        reselect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTable();
                changeStateAll("state", false);
                nCurrentNum = nSelectItem;
                Toast.makeText(getApplicationContext(), "reselect / 예약인원: "+String.valueOf(nCurrentNum), Toast.LENGTH_SHORT).show();
            }
        });
        back = (Button) findViewById(R.id.back); // 돌아가기 버튼
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(3, intent);
                finish();
            }
        });
//        tableInit = findViewById(R.id.test_init);
//        tableInit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tempInitTable();
//            }
//        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left2, R.anim.slide_left);
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
        setTable();
        updateTable(nSelectItem);//초기 파라미터 -1으로 인해 버튼 비활성화
    }
    public void popup() {
        Log.d(TAG, "Popup");
        AlertDialog.Builder oDialog = new AlertDialog.Builder(this,
                android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        oDialog.setTitle("인원수를 선택하세요")
                .setSingleChoiceItems(peopleNum, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nSelectItem = which+1;
                        Log.d(TAG, "popup / 예약인원 : "+String.valueOf(nSelectItem));
                    }
                })
                .setNeutralButton("선택", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (nSelectItem > -1){
                            updateTable(nSelectItem);//nSelectItem 으로 구분하여 table 상태 변화. 입력 후 버튼 활성화\
                            nCurrentNum = nSelectItem;
                            availableTable(nCurrentNum);//선택된 인원에 맞게 이용 가능한 테이블 설정

                            reselect_btn.setVisibility(View.VISIBLE);//새로고침 버튼 활성화
                            reselect_btn.setEnabled(true);
                        }
                    }
                })
                .setCancelable(false)
                .show();
    }
    public void updateTable(int nSelectItem){
        Log.d(TAG, "update Table:"+String.valueOf(nSelectItem));
        if (nSelectItem < 0){
            Log.d(TAG, "update Table/ enable= false");
            layout.setBackgroundResource(R.drawable.reserve_background);
            changeStateAll(reserved, true);//전체 이용 불가로 강제 전환
        }else if(nSelectItem >= 0){
            Log.d(TAG, "update Table/ enable= true");
            layout.setBackgroundResource(R.drawable.button_background);
            setTable();
            Log.d(TAG, "updateTable / "+String.valueOf(buttonStateList.get(seat1)));
            changeStateAll("state", false);
        }
    }//초기 인원 선택 여부에 따른 테이블 상태 변화
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
                    if(table.table_num==1){
                        if(table.available == true){
                            buttonStateList.put(seat1, available);
                            Log.d(TAG, "setTable / table1 true");
                        }else{
                            buttonStateList.put(seat1, reserved);
                            Log.d(TAG, "setTable / table1 false");
                        }
                    }else  if(table.table_num==2){
                        if(table.available == true){
                            buttonStateList.put(seat2, available);
                        }else{
                            buttonStateList.put(seat2, reserved);
                        }

                    }else if(table.table_num==3){
                        if(table.available == true){
                            buttonStateList.put(seat3, available);
                        }else{
                            buttonStateList.put(seat3, reserved);
                        }

                    }else if(table.table_num==4){
                        if(table.available == true){
                            buttonStateList.put(seat4, available);
                        }else{
                            buttonStateList.put(seat4, reserved);
                        }
                    }else if(table.table_num==5){
                        if(table.available == true){
                            buttonStateList.put(seat5, available);
                        }else{
                            buttonStateList.put(seat5, reserved);
                        }
                    }else if(table.table_num==6){
                        if(table.available == true){
                            buttonStateList.put(seat6, available);
                        }else{
                            buttonStateList.put(seat6, reserved);
                        }
                    }else if(table.table_num==7){
                        if(table.available == true){
                            buttonStateList.put(seat7, available);
                        }else{
                            buttonStateList.put(seat7, reserved);
                        }
                    }else if(table.table_num==8){
                        if(table.available == true){
                            buttonStateList.put(seat8, available);
                        }else{
                            buttonStateList.put(seat8, reserved);
                        }
                    }else if(table.table_num==9){
                        if(table.available == true){
                            buttonStateList.put(seat9, available);
                        }else{
                            buttonStateList.put(seat9, reserved);
                        }
                    }else if(table.table_num==10){
                        if(table.available == true){
                            buttonStateList.put(seat10, available);
                        }else{
                            buttonStateList.put(seat10, reserved);
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "table_num exception", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, String.valueOf(databaseError));
            }

        });


    }//database 에서 정보를 받아서 현재 테이블 셋팅, 항상 초기화용으로 사용해야함
    public void changeStateAll(String state, boolean compel){
        Log.d(TAG, "changeStageAll // "+state+"/"+String.valueOf(compel));
        if(compel){//강제변환, state parameter에 값으로 변환
            if(state.equals(available)){
                for(Button button:buttonStateList.keySet()){
                    button.setBackgroundResource(R.drawable.button_background);
                    button.setEnabled(true);
                    buttonStateList.put(button, available);
                }
            }else{
                for(Button button:buttonStateList.keySet()) {
                    button.setBackgroundResource(R.drawable.button_reserve);
                    button.setEnabled(false);
                    buttonStateList.put(button, reserved);
                }
            }
        }
        else{//state에 할당된 값으로 변환
            for(Button button:buttonStateList.keySet()){
                if(buttonStateList.get(button).equals(available)){
                    button.setBackgroundResource(R.drawable.button_background);
                    button.setEnabled(true);
                }else if(buttonStateList.get(button).equals(selected)){
                    button.setBackgroundResource(R.drawable.button_select);
                    button.setEnabled(true);
                }else if(buttonStateList.get(button).equals(reserved)){
                    button.setBackgroundResource(R.drawable.button_reserve);
                    button.setEnabled(false);
                }
            }
        }

    }//전체 버튼에 대한 동작, compel이 false일 경우 state parameter는 무의미
    public void changeStateEach(Button button, String state, boolean compel){
        Log.d(TAG, "changeStateEach"+String.valueOf(button)+"/state/"+state+"/ "+String.valueOf(compel));
        if(compel){//강제변환, buttonState에도 반영
            if(state.equals(available)){
                button.setBackgroundResource(R.drawable.button_background);
                button.setEnabled(true);
                buttonStateList.put(button, available);
            }else if(state.equals(selected)){
                button.setBackgroundResource(R.drawable.button_select);
                button.setEnabled(false);
                buttonStateList.put(button, selected);
            }else if(state.equals(reserved)){
                button.setBackgroundResource(R.drawable.button_reserve);
                button.setEnabled(false);
                buttonStateList.put(button, reserved);
            }
        }else{
            if(buttonStateList.get(button).equals(available)){
                button.setBackgroundResource(R.drawable.button_background);
                button.setEnabled(true);
            }else if(buttonStateList.get(button).equals(selected)){
                button.setBackgroundResource(R.drawable.button_select);
                button.setEnabled(true);
            }else if(buttonStateList.get(button).equals(reserved)){
                button.setBackgroundResource(R.drawable.button_reserve);
                button.setEnabled(false);
            }
        }
    } //개별 버튼에 대한 동작
    public void availableTable(int nCurrent){
        Log.d(TAG, "availableTable / 현재 입력된 예약 인원수 : "+String.valueOf(nCurrent));
        Set<Button> tables = new HashSet<>();
        if(nCurrent<1)//0명의 경우 선탠된 자리르 제외한 나머지 자리를 reserved로
        {
            tables.add(seat1);
            tables.add(seat2);
            tables.add(seat3);
            tables.add(seat4);
            tables.add(seat5);
            tables.add(seat6);
            tables.add(seat7);
            tables.add(seat8);
            tables.add(seat9);
            tables.add(seat10);
            for (Iterator<Button> it = tables.iterator(); it.hasNext(); ) {
                Button button = it.next();
                if (!buttonStateList.get(button).equals(selected)){
                    changeStateEach(button, reserved, true);
                }
            }
            tables.clear();
        }
        else if(nCurrent==1){//1명의 경우 1인석 외에 나머지 이용불가
            tables.add(seat1);
            tables.add(seat2);
            tables.add(seat3);
            tables.add(seat4);
            tables.add(seat5);
            tables.add(seat6);
            tables.add(seat7);
            for (Iterator<Button> it = tables.iterator(); it.hasNext(); ) {
                Button button = it.next();
                if (!buttonStateList.get(button).equals(selected)){
                    changeStateEach(button, reserved, true);
                }
            }
            tables.clear();
        }else if(nCurrent==2){//2명의 경우 2인석만 이용
            tables.add(seat1);
            tables.add(seat4);
            tables.add(seat7);
            tables.add(seat8);
            tables.add(seat9);
            tables.add(seat10);
            for (Iterator<Button> it = tables.iterator(); it.hasNext(); ) {
                Button button = it.next();
                if (!buttonStateList.get(button).equals(selected)){
                    changeStateEach(button, reserved, true);
                }
            }
            tables.clear();
        }else if(nCurrent ==3){//3명의 경우 4인석만
            tables.add(seat2);
            tables.add(seat3);
            tables.add(seat5);
            tables.add(seat6);
            tables.add(seat7);
            tables.add(seat8);
            tables.add(seat9);
            tables.add(seat10);
            for (Iterator<Button> it = tables.iterator(); it.hasNext(); ) {
                Button button = it.next();
                if (!buttonStateList.get(button).equals(selected)){
                    changeStateEach(button, reserved, true);
                }
            }
            tables.clear();
        }else if(nCurrent ==4){//4명의 경우 4인석와 6인석까지 이용 가능\
            tables.add(seat2);
            tables.add(seat3);
            tables.add(seat5);
            tables.add(seat6);
            tables.add(seat8);
            tables.add(seat9);
            tables.add(seat10);
            for (Iterator<Button> it = tables.iterator(); it.hasNext(); ) {
                Button button = it.next();
                if (!buttonStateList.get(button).equals(selected)){
                    changeStateEach(button, reserved, true);
                }
            }
            tables.clear();
        }
        else if(nCurrent ==5||nCurrent == 6){//5-6명의 경우 1인석 제외 전부 이용 가능
            tables.add(seat1);
            tables.add(seat2);
            tables.add(seat3);
            tables.add(seat4);
            tables.add(seat5);
            tables.add(seat6);
            tables.add(seat8);
            tables.add(seat9);
            tables.add(seat10);
            for (Iterator<Button> it = tables.iterator(); it.hasNext(); ) {
                Button button = it.next();
                if (!buttonStateList.get(button).equals(selected)){
                    changeStateEach(button, reserved, true);
                }
            }
            tables.clear();
        }else{
            Log.d(TAG, "updateTable / exception error");
        }
    }//nCurrent 값에 따른 선택 가능 테이블 변화 이벤트
    public void tempInitTable(){

        for(String key:tableName){
            mInitTable.child(key).child("available").setValue(true);
            mInitMemRef.child(key).child("reserved").setValue("none");
            mInitTimeRef.child(key).child("time").setValue("none");
        }
        if(buttonStateList != null){
            changeStateAll(available, true);

        }
        Toast.makeText(getApplicationContext(), "testInit", Toast.LENGTH_SHORT).show();

    }//테스트용 테이블 리섹



}