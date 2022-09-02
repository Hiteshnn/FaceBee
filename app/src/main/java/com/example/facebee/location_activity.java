package com.example.facebee;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.List;
import java.util.Locale;


//public class location_activity extends AppCompatActivity implements LocationListener {
public class location_activity extends AppCompatActivity {
    private Button button;
    private TextView tv;
    private ImageView stored_image,taken_image;
    LocationManager locationManager;
    ActivityResultLauncher<Intent> activityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        button=(Button)findViewById(R.id.button);
        tv=(TextView)findViewById(R.id.textView);
        stored_image=(ImageView)findViewById(R.id.image_view);
        taken_image=(ImageView)findViewById(R.id.image_view2);
        Intent i=getIntent();
        String image1=i.getStringExtra("image");

        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Bundle bundle=result.getData().getExtras();
                Bitmap bitmap=(Bitmap)bundle.get("data");
                taken_image.setImageBitmap(bitmap);
            }
        });
        /*
        if(ContextCompat.checkSelfPermission(location_activity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(location_activity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},100);
        }*/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),image1,Toast.LENGTH_SHORT).show();
                Picasso.get()
                        .load(image1)
                        .into(stored_image);
                BitmapDrawable drawable = (BitmapDrawable) stored_image.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(i.resolveActivity(getPackageManager())!=null){
                    activityResultLauncher.launch(i);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                }
                //getLocation();
            }
        });

    }


    /*
    @SuppressLint("MissingPermission")
    private void getLocation(){
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, location_activity.this);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        }
    @Override
    public void onLocationChanged(@NonNull Location location) {

        try{
            Geocoder geocoder=new Geocoder(location_activity.this, Locale.getDefault());
            List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address=addresses.get(0).getAddressLine(0);
            tv.setText(location.getLatitude()+"\n"+ location.getLongitude()+"\n"+ location.getAltitude()+"\n"+address);

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    */

}
