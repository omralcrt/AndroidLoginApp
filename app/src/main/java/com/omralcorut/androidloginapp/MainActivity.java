package com.omralcorut.androidloginapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    LoginButton loginButton;
    CallbackManager callbackManager;
    TextView nameTextView, lastnameTextView, emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTextView = (TextView) findViewById(R.id.name);
        lastnameTextView = (TextView) findViewById(R.id.lastname);
        emailTextView = (TextView) findViewById(R.id.email);

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email")); // Kullanıcının genel profiline ve mail adresine eriş
        //loginButton.setFragment(this); // Eğer fragment kullanıyorsanız ekleyin
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    String name = object.getString("first_name");
                                    String lastName = object.getString("last_name");
                                    String email = object.getString("email");
                                    nameTextView.setText(name);
                                    lastnameTextView.setText(lastName);
                                    emailTextView.setText(email);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle bundle = new Bundle();
                bundle.putString("fields","first_name,last_name,email");
                request.setParameters(bundle);
                GraphRequestBatch batch = new GraphRequestBatch(request);
                batch.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.e("onCancel", "onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("onError", "onError");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
