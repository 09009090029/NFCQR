package com.nitish.nfcqrapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nitish.nfcqrapp.data.ProductDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductDetailActivity extends AppCompatActivity {
    private String jsoneData = "";
    private ProductDetail productDetail;
    private String LOGING_TEG = "ProductDetailActivity";
    private TextView productNameTextView;
    private TextView mrpTextView;
    private TextView unitTextView;
    private LinearLayout keyFeatherTextListView;
    private LinearLayout ingredientsListTextView;
    private TextView discripstionTextView;
    private TextView disclaimerTextView;
    private TextView expiryDateTextView;
    private TextView manufacturerDateTextView;
    private ImageView productImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        productNameTextView = (TextView) findViewById(R.id.productNameTextView);
        mrpTextView = (TextView) findViewById(R.id.mrpTextView);
        unitTextView = (TextView) findViewById(R.id.unitTextView);
        keyFeatherTextListView = (LinearLayout) findViewById(R.id.keyFeatherTextListView);
        ingredientsListTextView = (LinearLayout) findViewById(R.id.ingredientsListTextView);
        discripstionTextView = (TextView) findViewById(R.id.discripstionTextView);
        disclaimerTextView = (TextView) findViewById(R.id.disclaimerTextView);
        expiryDateTextView = (TextView) findViewById(R.id.expiryDateTextView);
        manufacturerDateTextView = (TextView) findViewById(R.id.manufacturerDateTextView);
        productImageView = (ImageView) findViewById(R.id.productImageView);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        jsoneData = bundle.getString(HttpUrl.PRODUCT_DETAIS_KEY, "");
//        jsoneData = "{\n" +
//                "\t\"result\": \"OK\",\n" +
//                "\t\"productName\": \"Facebook Strong Teeth Toothpaste\",\n" +
//                "\t\"manufacturerDate\": \"2018-05-05\",\n" +
//                "\t\"expirDate\": \"2018-05-10 00:00:00\",\n" +
//
//                "\t\"manufacturerBy\": \"Face bookpvt. ltd\",\n" +
//                "\t\"mrp\": \"23.34\",\n" +
//                "\t\"imageUrl\": \"www.facebook.com\\/3.png\",\n" +
//                "\t\"unit\": \"500gm\",\n" +
//                "\t\"keyFeatures\": [\"Facebookkall around cavity protection\", \"Fights germ and bacteria in the mouth\", \"Provides whiter teeth and fresher breath\", \"Aids in plaque removal and promotes healthy gums\"],\n" +
//                "\t\"ingredients\": [\"FacebookCarbonate\", \"Sorbitol\", \"Sodium Monofluorophosphate\", \"Sodium Bicarbonate\", \"Benzyl Alcohol\", \"Sodium Saccharin\", \"Triclosan\", \"In Aqueous Base\"],\n" +
//                "\t\"description\": \"Facebook Strong Teeth Toothpaste is enriched with calcium and minerals formula that gives complete all around cavity and germ protection and makes your teeth cleaner and whiter. It repairs early signs of cavities, making your teeth strong and cavity free.\",\n" +
//                "\t\"disclaimer\": \"Facebook effort is made to maintain accuracy of all information. However, actual product packaging and materials may contain more and\\/or different information. It is recommended not to solely rely on the information presented\"\n" +
//                "}";
//
        if (jsoneData != null && !"".equalsIgnoreCase(jsoneData)) {
            Log.d(LOGING_TEG, "jsoneData = " + jsoneData);
            Gson gson = new Gson();
            productDetail = gson.fromJson(jsoneData, ProductDetail.class);
            Log.d(LOGING_TEG, "productDetail = " + productDetail);

        }
        populateView();
    }

    public void populateView() {

        Picasso.get()
                .load(productDetail.getImageUrl())
                .placeholder(R.drawable.defult)
                .error(R.drawable.defult)
                .into(productImageView);

        productNameTextView.setText(productDetail.getProductName());
        mrpTextView.setText(productDetail.getMrp());
        unitTextView.setText(productDetail.getUnit());
        discripstionTextView.setText(productDetail.getDescription());
        disclaimerTextView.setText(productDetail.getDisclaimer());
        expiryDateTextView.setText(productDetail.getExpirDate());
        manufacturerDateTextView.setText(productDetail.getManufacturerDate());
        ArrayList<String> keyFeaturesArrayList = productDetail.getKeyFeatures();

        for (String keyFeature : keyFeaturesArrayList) {
            TextView keyFeatureTextView = new TextView(getApplicationContext());
            keyFeatureTextView.setText(keyFeature);
            keyFeatureTextView.setTextSize(15);
            keyFeatureTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.hedding, null));
            keyFeatherTextListView.addView(keyFeatureTextView);
        }


        ArrayList<String> ingredientsArrayList = productDetail.getIngredients();

        for (String ingredient : ingredientsArrayList) {
            TextView ingredientTextView = new TextView(getApplicationContext());
            ingredientTextView.setText(ingredient);
            ingredientTextView.setTextSize(15);
            ingredientTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.hedding, null));
            ingredientsListTextView.addView(ingredientTextView);
        }

    }
}
