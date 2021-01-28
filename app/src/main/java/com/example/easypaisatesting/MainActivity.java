package com.example.easypaisatesting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;

import telenor.com.ep_v1_sdk.config.Easypay;

public class MainActivity extends AppCompatActivity {

    private Button proceedBtn;
    private Easypay easypay;
    private Context context;
    String encryptedValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        proceedBtn = findViewById(R.id.btn_pay);
        easypay = new Easypay();
        context = this;

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String dateString = dateFormat.format(currentDate);
        Log.e("Current Date", dateString);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, 1);
        Date expiryDate = calendar.getTime();
        SimpleDateFormat dateExpiryFormat = new SimpleDateFormat("yyyyMMdd HHmmss");

        String dateExpiryString = dateExpiryFormat.format(expiryDate);
        Log.e("Expiry Date", dateExpiryString);

        String sampleString = "amount=250&emailAddress=auranng@gmail.com&expiryDate="+dateExpiryString+
                "&mobileNum=03009788827&orderRefNum=hashTest&paymentMethod=MA&storeId=11732&timeStamp="+dateString;

        encryptedValue = "";
        try {
            Cipher cipher = null;
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            String key ="8BQG6XSPP2A8IQTJ";
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            //String encryptedValue = new String(Base64.encodeBase64(cipher.doFinal(sampleString.getBytes())));
            encryptedValue = Base64.encodeToString(cipher.doFinal(sampleString.getBytes()), Base64.DEFAULT);
            Toast.makeText(context, encryptedValue, Toast.LENGTH_SHORT).show();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.e("error", e.getMessage());
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            Log.e("error", e.getMessage());

        } catch (BadPaddingException e) {
            e.printStackTrace();
            Log.e("error", e.getMessage());

        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            Log.e("error", e.getMessage());

        } catch (InvalidKeyException e) {
            e.printStackTrace();
            Log.e("error", e.getMessage());

        }


        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easypay.configure(context, "11732",
                        "Auranng", "", "",
                        encryptedValue,
                        "https://easypaystg.easypaisa.com.pk/easypay-service/rest",
                        false,
                        "MA");

                easypay.checkout(context,"auranng@gmail.com",
                       "03009788827", "hashTest", "250");
            }
        });
    }
}