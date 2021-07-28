package com.groupi.onlineshopping.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.groupi.onlineshopping.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {
    private String categoryName;
    private String prodDescription;
    private String prodName;
    private String prodPrice;
    private String saveCurrentDate,saveCurrentTime;
    private EditText productName;
    private EditText productDescription;
    private EditText productPrice;
    private ImageView selectImage;
    private Button addNewProductBtn;
    private static final int gallaryPick=1;
    private Uri imageUri;
    private String productRandomKey,dowloadImageUrl;
    private StorageReference productImagesRef;
    private DatabaseReference productsRef;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        categoryName = getIntent().getExtras().get("category").toString();
        productImagesRef = FirebaseStorage.getInstance().getReference().child("Products Images");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        loadingBar = new ProgressDialog(this);

        productName = (EditText) findViewById(R.id.product_name);
        productDescription = (EditText) findViewById(R.id.product_description);
        productPrice = (EditText) findViewById(R.id.product_price);

        selectImage = (ImageView) findViewById(R.id.select_product_image);
        addNewProductBtn=(Button)findViewById(R.id.add_new_products);


        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 openGallery();

            }
        });


        addNewProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                validateProductData();

            }
        });


    }


    private void openGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,gallaryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==gallaryPick && resultCode==RESULT_OK && data!=null)
        {
            imageUri = data.getData();
            selectImage.setImageURI(imageUri);




        }

    }

    private void validateProductData()
    {

        prodDescription = productDescription.getText().toString();
        prodPrice= productPrice.getText().toString();
        prodName = productName.getText().toString();

        if(imageUri == null)
        {
            Toast.makeText(this,"Please select the image of the product to add",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(prodDescription))
        {
            Toast.makeText(this,"Please write the  description of the product to add",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(prodPrice))
        {
            Toast.makeText(this,"Please write the  price of the product to add",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(prodName))
        {
            Toast.makeText(this,"Please write the name of the product to add",Toast.LENGTH_SHORT).show();
        }
        else
        {
            storeProductInformation();
        }

    }

    private void storeProductInformation()
    {

        loadingBar.setTitle("Add new product");
        loadingBar.setMessage("Please wait while adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate =  new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime =  new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate+saveCurrentTime;
        StorageReference filePath = productImagesRef.child(imageUri.getLastPathSegment()+productRandomKey);

        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e)
            {
               String messsage = e.toString();
               Toast.makeText(AdminAddNewProductActivity.this,"Error:"+messsage,Toast.LENGTH_SHORT).show();
               loadingBar.dismiss();


            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(AdminAddNewProductActivity.this,"Products image uploaded successfully",Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull  Task<UploadTask.TaskSnapshot> task) throws Exception {
                       if(!task.isSuccessful())
                       {
                           throw task.getException();
                       }
                        dowloadImageUrl =filePath.getDownloadUrl().toString();

                       return filePath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull  Task<Uri> task) {

                        if(task.isSuccessful())
                        {
                            dowloadImageUrl =task.getResult().toString();
                            Toast.makeText(AdminAddNewProductActivity.this,"Getting Products image url successfully",Toast.LENGTH_SHORT).show();

                            saveProductInforToDatabase();
                            

                        }
                    }
                });

            }
        });








    }

    private void saveProductInforToDatabase()
    {
        HashMap<String,Object> productMap = new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",prodDescription);
        productMap.put("image",dowloadImageUrl);
        productMap.put("category",categoryName);
        productMap.put("price",prodPrice);
        productMap.put("name",prodName);


        productsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull  Task<Void> task) {


                        if(task.isSuccessful())
                        {

                            Intent intent = new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewProductActivity.this,"Products is added successfully",Toast.LENGTH_SHORT).show();



                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddNewProductActivity.this,"Error:"+message,Toast.LENGTH_SHORT).show();

                        }
                    }
                });




    }

}