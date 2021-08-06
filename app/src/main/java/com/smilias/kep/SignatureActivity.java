package com.smilias.kep;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.smilias.kep.model.Functions;

import java.io.File;
import java.io.FileOutputStream;

import androidx.appcompat.app.AppCompatActivity;

public class SignatureActivity extends AppCompatActivity {
    SignaturePad signaturePad;
    Button saveButton, clearButton;
    Bitmap bitmap;
    private String name, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");

        signaturePad = (SignaturePad) findViewById(R.id.signaturePad);
        saveButton = (Button) findViewById(R.id.saveButton);
        clearButton = (Button) findViewById(R.id.clearButton);

        //disable both buttons at start
        saveButton.setEnabled(false);
        clearButton.setEnabled(false);

        //change screen orientation to landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                saveButton.setEnabled(true);
                clearButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                saveButton.setEnabled(false);
                clearButton.setEnabled(false);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = signaturePad.getSignatureBitmap();
                try {
                    Functions functions = new Functions(name, email);
                    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    // Output the file
                    FileOutputStream mFileOutStream = new FileOutputStream(path + "/signature.bmp");
                    // Convert the output file to Image such as .png
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, mFileOutStream);
                    mFileOutStream.close();
                    String file = path + "/" + name + ".pdf";
                    PdfReader pdfReader = new PdfReader(file);
                    PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(path + "/" + name + "_2.pdf"));
                    Image i = Image.getInstance(path + "/signature.bmp");
                    i.scalePercent(10);
                    i.setAbsolutePosition(250, 100);
                    pdfStamper.getOverContent(1).addImage(i);
                    pdfStamper.close();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                functions.sendemail();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
//---close the activity---
                    finish();

                } catch (Exception e) {
                    Log.v("log_tag", e.toString());
                }
                Toast.makeText(getApplicationContext(), "Email sent", Toast.LENGTH_SHORT).show();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signaturePad.clear();
            }
        });

    }
}