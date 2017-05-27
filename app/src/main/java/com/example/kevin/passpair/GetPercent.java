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
 * Created by KEVIN on 2017/3/20.
 */

public class GetPercent extends Activity{

    TextView txtMatch;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("eeg");
    String[] names={"103306007","account"};
    long n;
    int[] lowA, highA, lowB, highB, theTa;
    int[][] data, dataMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brainwaverecommend);

        txtMatch = (TextView)findViewById(R.id.match);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int dataLength;
                double matchPoint = 0.0;
                for (String name:names) {
                    int t=names.length;
                    if (dataSnapshot.hasChild(name)){
                        n = dataSnapshot.child(name).getChildrenCount();
                        lowA = new int[(int) n];
                        highA = new int[(int) n];
                        lowB = new int[(int) n];
                        highB = new int[(int) n];
                        theTa = new int[(int) n];
                        data = new int[t][(int) n];
                        Log.d("n大小", String.valueOf(n));
                        for (int i=0;i<5;i++) {
                            lowA[i] = dataSnapshot.child(name).child(String.valueOf(i)).child("Low Alpha").getValue(Integer.class);
                            highA[i] = dataSnapshot.child(name).child(String.valueOf(i)).child("High Alpha").getValue(Integer.class);
                            lowB[i] = dataSnapshot.child(name).child(String.valueOf(i)).child("Low Beta").getValue(Integer.class);
                            highB[i] = dataSnapshot.child(name).child(String.valueOf(i)).child("High Beta").getValue(Integer.class);
                            theTa[i] = dataSnapshot.child(name).child(String.valueOf(i)).child("Theta").getValue(Integer.class);
                            data[t-1][i] = lowA[i]+highA[i]-lowB[i]-highB[i]-theTa[i];
                            Log.d("次數","第i"+i+"次跑");
                        }
                    }
                    Log.d("次數","第"+name+"次跑");
                }
                if (data[0].length>=data[1].length){
                    dataLength = data[1].length;
                }else {
                    dataLength = data[0].length;
                }

                for (int i = 0;i<dataLength;i++){
                    int score = Math.abs(data[0][i]-data[1][i]);
                    if (score < 25000){
                        matchPoint++;
                    }
                }
                Log.d("原始分數", String.valueOf(matchPoint));
                txtMatch.setText("契合度："+(int)Math.ceil(matchPoint/4.1)+"%");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG,"Failed to read value.", error.toException());
            }
        });
    }
}
