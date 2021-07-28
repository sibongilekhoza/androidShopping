package com.groupi.onlineshopping.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.groupi.onlineshopping.DashboardActivity;
import com.groupi.onlineshopping.LoginActivity;
import com.groupi.onlineshopping.R;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView bakery,cannedFood,capture,cereal,dressings,drinks,frozen_collage;
    private ImageView meatFish,medication,milkeChess,produce,snack,toiletries;

    private Button maintainProductBtn;

    private Button logoutbtn,checkNewOderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        bakery =(ImageView) findViewById(R.id.bakery_bread);
        cannedFood =(ImageView) findViewById(R.id.canned_food);
        capture =(ImageView) findViewById(R.id.capture);
        cereal =(ImageView) findViewById(R.id.cereals);
        dressings =(ImageView) findViewById(R.id.dressing);
        drinks =(ImageView) findViewById(R.id.drinks);
        frozen_collage =(ImageView) findViewById(R.id.frozen_collage);
        meatFish =(ImageView) findViewById(R.id.meatfish);
        medication =(ImageView) findViewById(R.id.medication);
        milkeChess =(ImageView) findViewById(R.id.milk_cheese);
        produce =(ImageView) findViewById(R.id.produce);
        snack =(ImageView) findViewById(R.id.snacks);
        toiletries =(ImageView) findViewById(R.id.toiletries);

         checkNewOderBtn = (Button)findViewById(R.id.check_orders_btn);
         logoutbtn =(Button)findViewById(R.id.admin_logout_btn);

        maintainProductBtn=(Button) findViewById(R.id.maintain_edit_product_btn);


         maintainProductBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view)
             {
                 Intent intent = new Intent(AdminCategoryActivity.this, DashboardActivity.class);
                 intent.putExtra("Admins","Admins");
                 startActivity(intent);
                 finish();

             }
         });


         logoutbtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 Intent intent = new Intent(AdminCategoryActivity.this, LoginActivity.class);
                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                 startActivity(intent);
                 finish();
             }
         });

         checkNewOderBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view)
             {
                 Intent intent = new Intent(AdminCategoryActivity.this, AdminNewOrdersActivity.class);
                 startActivity(intent);

             }
         });

        bakery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","backery");
                startActivity(intent);
            }
        });
        cannedFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","cannedFood");
                startActivity(intent);
            }
        });
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","capture");
                startActivity(intent);
            }
        });
        cereal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","cereal");
                startActivity(intent);
            }
        });
        dressings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","dressings");
                startActivity(intent);
            }
        });
        drinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","drinks");
                startActivity(intent);
            }
        });
        frozen_collage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","frozen_collage");
                startActivity(intent);
            }
        });
        meatFish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","meatFish");
                startActivity(intent);
            }
        });
        medication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","medication");
                startActivity(intent);
            }
        });
        milkeChess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","milkeChess");
                startActivity(intent);
            }
        });
        produce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","produce");
                startActivity(intent);
            }
        });
        snack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","snack");
                startActivity(intent);
            }
        });
        toiletries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","toiletries");
                startActivity(intent);
            }
        });



    }
}