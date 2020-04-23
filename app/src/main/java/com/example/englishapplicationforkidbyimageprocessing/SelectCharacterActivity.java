package com.example.englishapplicationforkidbyimageprocessing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import info.hoang8f.widget.FButton;

public class SelectCharacterActivity extends AppCompatActivity {
    private FButton mOkButton;

    private ImageView mSelectPlayer1;
    private ImageView mSelectPlayer2;

    private TextView mHeaderPlayer1;
    private TextView mHeaderPlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_character);

        Toast.makeText(SelectCharacterActivity.this, "เลือกเพศของตัวละคร", Toast.LENGTH_SHORT).show();

        mOkButton = findViewById(R.id.ok_button);

        mOkButton.setButtonColor(getResources().getColor(R.color.light_green));

        mSelectPlayer1 = findViewById(R.id.imagePlayer1);
        mSelectPlayer2 = findViewById(R.id.imagePlayer2);

        mHeaderPlayer1 = findViewById(R.id.headerPlayer1);
        mHeaderPlayer2 = findViewById(R.id.headerPlayer2);

        mOkButton.setShadowEnabled(true);
        mOkButton.setShadowHeight(20);
        mOkButton.setCornerRadius(20);
        mSelectPlayer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectPlayer1.setBackgroundColor(getResources().getColor(R.color.green));
                mHeaderPlayer1.setBackgroundColor(getResources().getColor(R.color.green));
                mSelectPlayer2.setBackgroundColor(getResources().getColor(R.color.color_transparent));
                mHeaderPlayer2.setBackgroundColor(getResources().getColor(R.color.color_transparent));
                mHeaderPlayer1.setTextColor(getResources().getColor(R.color.white));
                mHeaderPlayer2.setTextColor(getResources().getColor(R.color.black));
                mOkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SelectCharacterActivity.this,PlayingActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });

        mSelectPlayer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectPlayer2.setBackgroundColor(getResources().getColor(R.color.green));
                mHeaderPlayer2.setBackgroundColor(getResources().getColor(R.color.green));
                mSelectPlayer1.setBackgroundColor(getResources().getColor(R.color.color_transparent));
                mHeaderPlayer1.setBackgroundColor(getResources().getColor(R.color.color_transparent));
                mHeaderPlayer2.setTextColor(getResources().getColor(R.color.white));
                mHeaderPlayer1.setTextColor(getResources().getColor(R.color.black));
                mOkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SelectCharacterActivity.this,Playing2Activity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });

    }
}
