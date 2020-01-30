package com.example.englishapplicationforkidbyimageprocessing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import info.hoang8f.widget.FButton;

public class SelectCategoryActivity extends AppCompatActivity {
    private FButton mButtonVegetable;
    private FButton mButtonFruit;
    private FButton mButtonAnimal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        mButtonVegetable = findViewById(R.id.vegetable_button);
        mButtonFruit = findViewById(R.id.fruit_button);
        mButtonAnimal = findViewById(R.id.animal_button);

        mButtonVegetable.setButtonColor(getResources().getColor(R.color.light_blue));
        mButtonVegetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(SelectCategoryActivity.this,CameraActivity.class);
                startActivity(intent1);
            }
        });

        mButtonFruit.setButtonColor(getResources().getColor(R.color.pink));
        mButtonFruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(SelectCategoryActivity.this,Camera2Activity.class);
                startActivity(intent2);
            }
        });

        mButtonAnimal.setButtonColor(getResources().getColor(R.color.yellow));
        mButtonAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(SelectCategoryActivity.this,Camera3Activity.class);
                startActivity(intent3);
            }
        });
    }
}
