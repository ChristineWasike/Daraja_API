package com.example.darajaapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.model.AccessToken;
import com.androidstudy.daraja.model.LNMExpress;
import com.androidstudy.daraja.model.LNMResult;
import com.androidstudy.daraja.util.TransactionType;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();
    //views
    @BindView(R.id.sendButton)
    Button sendButton;
    @BindView(R.id.editTextPhoneNumber)
    EditText inputPhoneNumber;

    //Daraja Global variable declaration
    Daraja daraja;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //set custom font
        Typeface openSansFont = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        inputPhoneNumber.setTypeface(openSansFont);
        sendButton.setTypeface(openSansFont);

        //Initialize Daraja
        daraja = Daraja.with("h8unwAoDG9ikP7K3xP4kgTZ0xxAhJJzR", "UEWAnKs3jJkiD4Kx", new DarajaListener<AccessToken>() {
            @Override
            public void onResult(@NonNull AccessToken accessToken) {
                Log.i(TAG, "onResult AccessToken: " + accessToken.getAccess_token());
                Toast.makeText(MainActivity.this, "TOKEN: " + accessToken.getAccess_token(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "onError: " + error);
            }
        });

        sendButton.setOnClickListener(v -> {
            //Get the phone number from user input
            phoneNumber = inputPhoneNumber.getText().toString().trim();

            if (TextUtils.isEmpty(phoneNumber)) {
                inputPhoneNumber.setError("Please provide a phone number");
                return;
            }
            LNMExpress lnmExpress = new LNMExpress(
                    "174379",
                    "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",
                    TransactionType.CustomerPayBillOnline,
                    "1",
                    "254708374149",
                    "174379",
                    phoneNumber,
                    "http://mycallbackurl.com/checkout.php",
                    "001ABC",
                    "Goods Payment"
            );

            daraja.requestMPESAExpress(lnmExpress, new DarajaListener<LNMResult>() {
                @Override
                public void onResult(@NonNull LNMResult lnmResult) {
                    Log.i(TAG, "onResult: "+lnmResult.ResponseDescription);
                }

                @Override
                public void onError(String error) {
                    Log.i(TAG, "onError: "+error);
                }
            });
        });
    }
}
