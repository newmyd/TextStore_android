package com.example.textstore;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ajax.HttpClient;

public class MainActivity extends AppCompatActivity {
    private Button up, down;
    private int mxTextLen = 21000, mxPassLen = 100;

    private HttpClient hc = new HttpClient();

    private void init() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectNetwork()
                .penaltyLog()
                .build());
        return ;
    }

    private int toInt(String s) {
        if (s == null || s.length() < 1 || s.length() > 9)
            return 0;
        int res = 0;
        for (int i = 0; i < s.length(); ++i)
            if (s.charAt(i) > '9' || s.charAt(i) < '0') return res;
            else res = res * 10 + s.charAt(i) - '0';
        return res;
    }

    private int upload(String txt, String password) {
        int res = 0;
        if (txt == null || txt.length() > mxTextLen || txt.length() < 1)
            return 0;
        if (password == null) password = "";


        String url = "http://192.168.19.170:8080/upload";

        res = toInt(hc.doGet(url + "?text=" + txt + "&password=" + password));

        if (res < 1) return 0;

        return res;
    }

    private String download(int textId, String password) {
        String res = "";
        if (textId < 1)
            return res;
        if (password == null)
            password = "";

        String url = "http://192.168.19.170:8080/download";

        res = hc.doGet(url + "?fileId=" + textId + "&password=" + password);

        return res;
    }

    public void change(EditText e, String s){
        CharSequence charSequence = s.subSequence(0, s.length());
        e.getText().replace(0, e.getText().toString().length(), charSequence);
        return ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        init();

        EditText text = (EditText) findViewById(R.id.text);

        EditText textId = (EditText) findViewById(R.id.textId);

        EditText passwordU = (EditText) findViewById(R.id.passwordU);

        EditText passwordD = (EditText) findViewById(R.id.passwordD);

        EditText resU = (EditText) findViewById(R.id.resU);

        EditText resD = (EditText) findViewById(R.id.resD);


        up = findViewById(R.id.upload);

        down = findViewById(R.id.download);

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int res = upload(text.getText().toString(), passwordU.getText().toString());

                String str = "";

                if (res < 1) str = "Upload Error.";
                else str = "Success. Text Id : " + res;

                change(resU, str);

                return ;
            }
        });

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int x = toInt(textId.getText().toString());

                if (x < 0) {
                    change(resD, "Download Error.");
                    return ;
                }

                String str = download(x, passwordD.getText().toString());

                if (str == null || str.equals(""))
                    str = "Download Error.";

                change(resD, str);

                return ;
            }
        });
    }
}