package com.example.englishapplicationforkidbyimageprocessing;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.englishapplicationforkidbyimageprocessing.Database.NgrokDatabaseHelper;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import info.hoang8f.widget.FButton;

public class PreviewToUploadActivity3 extends AppCompatActivity {

    private FButton cancel_btn;
    private FButton accept_btn;
    private NgrokDatabaseHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_to_upload3);

        cancel_btn = findViewById(R.id.cancel_Button3);
        accept_btn = findViewById(R.id.accept_Button3);

        cancel_btn.setButtonColor(getResources().getColor(R.color.red));
        cancel_btn.setShadowEnabled(true);
        cancel_btn.setShadowHeight(25);
        cancel_btn.setCornerRadius(25);

        accept_btn.setButtonColor(getResources().getColor(R.color.light_green));
        accept_btn.setShadowEnabled(true);
        accept_btn.setShadowHeight(25);
        accept_btn.setCornerRadius(25);

        mHelper = new NgrokDatabaseHelper(this);

        showImage();
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreviewToUploadActivity3.this, SelectCategoryActivity.class);
                startActivity(intent);

            }
        });

        accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resizeImage();
                accept_btn.setVisibility(View.GONE);
//                Toast.makeText(PreviewToUploadActivity3.this,"กำลังอัปโหลดรูป...", Toast.LENGTH_LONG).show();
                uploadImage();
            }
        });
    }

    private void showImage() {
        OutputStream out = null;
        File file = new File(Environment.getExternalStorageDirectory()+"/wordsapp"+".jpg");
        ImageView image = findViewById(R.id.show_images3);
        Matrix matrix = new Matrix();
        Intent intent = getIntent();
        String ImageFrom = intent.getStringExtra("ImageFrom");
        if(ImageFrom.equals("gallery")){
            matrix.postRotate(360);
        }
        else if(ImageFrom.equals("camera")){
            matrix.postRotate(90);
        }
        Bitmap bm = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/wordsapp"+".jpg");
        Bitmap rotated = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight(),matrix,true);

        try {
            out = new FileOutputStream(file);
            rotated.compress(Bitmap.CompressFormat.JPEG, 100, out);
            image.setImageBitmap(rotated);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void resizeImage() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_HH_mm", Locale.KOREA);
        Date now = new Date();
        String path = (Environment.getExternalStorageDirectory()+"/wordsapp"+".jpg");
        String rename = (Environment.getExternalStorageDirectory()+"/"+"words_"+formatter.format(now)+".jpg");

        Bitmap photo = BitmapFactory.decodeFile(path);
        photo = Bitmap.createScaledBitmap(photo,480,640,false);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 70,bytes);

        File f = new File(rename);
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();
            File file = new File(path);
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadImage() {
        Toast.makeText(PreviewToUploadActivity3.this, "รอสักครู่นะคะ...", Toast.LENGTH_LONG).show();

//        Toast.makeText(PreviewToUploadActivity3.this,"อัปโหลดรูปเรียบร้อยเเล้ว", Toast.LENGTH_LONG).show();
        String url = "https://"+mHelper.getNgrokPath()+".ngrok.io";
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_HH_mm", Locale.KOREA);
        Date now = new Date();
        String path = (Environment.getExternalStorageDirectory()+"/"+"words_"+formatter.format(now)+".jpg");
        Ion.with(this)
                .load(url+"/wordsapp/imageupload.php")
                .setMultipartFile("upload_file", new File(path))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        processModel();
//                        Intent intent = new Intent(PreviewToUploadActivity2.this,LearningImageProcessingActivity2.class);
//                        intent.putExtra("results","test2");
//                        startActivity(intent);

                    }
                });
    }


    private void processModel() {
        String url = "https://"+mHelper.getNgrokPath()+".ngrok.io";
//        Toast.makeText(getBaseContext(), "กำลังตรวจสอบการประมวลผล...", Toast.LENGTH_LONG).show();

        Ion.with(this)
                .load(url+"/wordsapp/runclassify.php")
//                .progressBar(progressBar)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if(result != null){
                            Toast.makeText(getBaseContext(), "ประมวลผลสำเร็จ!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(PreviewToUploadActivity3.this,LearningImageProcessingActivity3.class);
                            intent.putExtra("results",result);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getBaseContext(), "ประมวลผลไม่สำเร็จ! เลือกหมวดหมู่เพื่อถ่ายใหม่...", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(PreviewToUploadActivity3.this,SelectCategoryActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }
}
