package com.merati.project.geopost;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    protected EditText username_field;
    protected EditText password_field;
    Model myModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username_field= (EditText)findViewById(R.id.username);
        password_field= (EditText)findViewById(R.id.password);
        myModel = new Model(this);
    }

    protected void onClickLogin(View view){
        String username, password;
        username = username_field.getText().toString();
        password = password_field.getText().toString();
        String sessionId = myModel.loginRequest("username="+username+"&password="+password);
        ((EditText)findViewById(R.id.sessionId)).setText(sessionId);
    }
}
