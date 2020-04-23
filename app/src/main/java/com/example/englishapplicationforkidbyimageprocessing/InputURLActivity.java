package com.example.englishapplicationforkidbyimageprocessing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.englishapplicationforkidbyimageprocessing.Database.NgrokDatabaseHelper;

import info.hoang8f.widget.FButton;

public class InputURLActivity extends AppCompatActivity {
    private EditText mEditText;
    private FButton mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_url);

        mButton = findViewById(R.id.ok_button_ngrok);

        mButton.setShadowEnabled(true);
        mButton.setShadowHeight(25);
        mButton.setCornerRadius(25);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetColor();
                NgrokDatabaseHelper helper = new NgrokDatabaseHelper(InputURLActivity.this);
                //SQLiteDatabase db = helper.getWritableDatabase();

                mEditText = findViewById(R.id.input_url);
                String Path = mEditText.getText().toString().trim();

                if(Path.length()==0){
                    Toast.makeText(InputURLActivity.this,"กรุณาใส่ URL",Toast.LENGTH_SHORT).show();
                }
                else if(Path.length() == 8){
                    Toast.makeText(InputURLActivity.this,"บันทึก URL เรียบร้อยแล้ว",Toast.LENGTH_SHORT).show();
                    helper.addNgrokPath(Path);
                    Intent intent = new Intent(InputURLActivity.this,SelectLearnPlayActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(InputURLActivity.this,"กรุณาใส่ URL ให้ถูกต้อง",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void resetColor() {
        mButton.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.pink));
    }
}
