package com.example.kevin.passpair;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

/**
 * Created by KEVIN on 2017/3/28.
 */

public class Mission extends Activity {
    private TextView grade;
    int score;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("passpair");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade);
        Bundle point = getIntent().getExtras();
        final int plusPoint = point.getInt("grade");
        grade=(TextView)findViewById(R.id.grade);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("分數", String.valueOf(plusPoint));
                score = dataSnapshot.child("grade").getValue(Integer.class);
                myRef.child("grade").setValue(score+plusPoint);
                grade.setText("目前積分 : "+(score+plusPoint));
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG,"Failed to read value.", error.toException());
            }
        });
    }

   /* private void missionComplete(){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int grade = dataSnapshot.child("grade").getValue(Integer.class);
                myRef.child("grade").setValue(grade+plusPoint);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG,"Failed to read value.", error.toException());
            }
        });
    }*/
}