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

    private FButton mButtonHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        mButtonVegetable = findViewById(R.id.vegetable_button);
        mButtonFruit = findViewById(R.id.fruit_button);
        mButtonAnimal = findViewById(R.id.animal_button);
        mButtonHome = findViewById(R.id.home_button_cate);

        mButtonVegetable.setShadowEnabled(true);
        mButtonVegetable.setShadowHeight(25);
        mButtonVegetable.setCornerRadius(25);

        mButtonVegetable.setButtonColor(getResources().getColor(R.color.light_blue));
        mButtonVegetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(SelectCategoryActivity.this,CameraActivity.class);
                startActivity(intent1);
            }
        });

        mButtonFruit.setShadowEnabled(true);
        mButtonFruit.setShadowHeight(25);
        mButtonFruit.setCornerRadius(25);

        mButtonFruit.setButtonColor(getResources().getColor(R.color.pink));
        mButtonFruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(SelectCategoryActivity.this,Camera2Activity.class);
                startActivity(intent2);
            }
        });

        mButtonAnimal.setShadowEnabled(true);
        mButtonAnimal.setShadowHeight(25);
        mButtonAnimal.setCornerRadius(25);

        mButtonAnimal.setButtonColor(getResources().getColor(R.color.yellow));
        mButtonAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(SelectCategoryActivity.this,Camera3Activity.class);
                startActivity(intent3);
            }
        });

        mButtonHome.setShadowEnabled(true);
        mButtonHome.setShadowHeight(25);
        mButtonHome.setCornerRadius(25);

        mButtonHome.setButtonColor(getResources().getColor(R.color.green));
        mButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(SelectCategoryActivity.this,SelectLearnPlayActivity.class);
                startActivity(intent4);
                finish();
            }
        });
    }
}
