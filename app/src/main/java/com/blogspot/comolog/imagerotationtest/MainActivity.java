package com.blogspot.comolog.imagerotationtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private View view;
    private View view2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = findViewById(R.id.view);
        view2 = findViewById(R.id.view2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.v("MainActivity", event.toString());
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.INVISIBLE);
                view2.setVisibility(View.VISIBLE);
            } else {
                view2.setVisibility(View.INVISIBLE);
                view.setVisibility(View.VISIBLE);

            }
        }
        return true;
    }
}
