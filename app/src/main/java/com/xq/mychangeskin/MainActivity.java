package com.xq.mychangeskin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    public Button button0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MyApp.theme == 0) {
            //使用默认主题
            setTheme(R.style.AppTheme);
        } else {
            //使用自定义的主题
            setTheme(R.style.DarkTheme);
        }
        setContentView(R.layout.activity_main);

        button0 = (Button) this.findViewById(R.id.button0);
        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApp.theme != 0) {
                    MyApp.theme = R.style.AppTheme;
                    MyApp.theme = 0;
                } else {
                    MyApp.theme = R.style.DarkTheme;
                    MyApp.theme = 10;
                }
                recreate();
            }
        });
    }
}

