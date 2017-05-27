package com.example.kevin.passpair;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

/**
 * Created by KEVIN on 2017/1/16.
 */

public class Match extends Activity {
    private ImageView smile, brainwave;

    TextView txtMatch;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("passpair");
    String[] names={"103306007","account"};
    long n;
    int[] lowA, highA, lowB, highB, theTa;
    int[][] data, dataMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showmatch_main);

        smile=(ImageView)findViewById(R.id.smile);
        brainwave=(ImageView)findViewById(R.id.brain);
        smile.setOnClickListener(listener);
        brainwave.setOnClickListener(listener);
    }

    private ImageView.OnClickListener listener=new ImageView.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId())
            {
                case R.id.smile:
                    matchSmileData();
                    break;
                case R.id.brain:
                    matchBrainData();
                    break;
            }
        }
    };

    private void matchBrainData(){
        setContentView(R.layout.brainwaverecommend);
        txtMatch = (TextView)findViewById(R.id.match);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int dataLength;
                double matchPoint = 0.0;
                for (String name:names) {
                    int t=names.length;
                    if (dataSnapshot.child("eeg").hasChild(name)){
                        n = dataSnapshot.child("eeg").child(name).getChildrenCount();
                        lowA = new int[(int) n];
                        highA = new int[(int) n];
                        lowB = new int[(int) n];
                        highB = new int[(int) n];
                        theTa = new int[(int) n];
                        data = new int[t][(int) n];
                        Log.d("n大小", String.valueOf(n));
                        for (int i=0;i<5;i++) {
                            lowA[i] = dataSnapshot.child("eeg").child(name).child(String.valueOf(i)).child("Low Alpha").getValue(Integer.class);
                            highA[i] = dataSnapshot.child("eeg").child(name).child(String.valueOf(i)).child("High Alpha").getValue(Integer.class);
                            lowB[i] = dataSnapshot.child("eeg").child(name).child(String.valueOf(i)).child("Low Beta").getValue(Integer.class);
                            highB[i] = dataSnapshot.child("eeg").child(name).child(String.valueOf(i)).child("High Beta").getValue(Integer.class);
                            theTa[i] = dataSnapshot.child("eeg").child(name).child(String.valueOf(i)).child("Theta").getValue(Integer.class);
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

    private void matchSmileData(){
        setContentView(R.layout.smilerecommend);
        txtMatch = (TextView)findViewById(R.id.match);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int dataLength;
                int matchPoint = 0;
                for (String name:names) {
                    int t=names.length;
                    if (dataSnapshot.child("lovehouse").hasChild(name)){
                        n = dataSnapshot.child("lovehouse").child(name).getChildrenCount();
                        data = new int[t][(int) n];
                        Log.d("n大小", String.valueOf(n));
                        for (int i=0;i<5;i++) {
                            data[t-1][i] = dataSnapshot.child("lovehouse").child(name).child(String.valueOf(i)).child("Data").getValue(Integer.class);
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
                    if (score < 3.5){
                        matchPoint++;
                    }
                }
                if (matchPoint > 10){
                    matchPoint = 10;
                }
                Log.d("原始分數", String.valueOf(matchPoint));
                txtMatch.setText("契合度："+matchPoint+"%");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG,"Failed to read value.", error.toException());
            }
        });
    }
}
