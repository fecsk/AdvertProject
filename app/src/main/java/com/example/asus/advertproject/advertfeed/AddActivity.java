package com.example.asus.advertproject.advertfeed;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.asus.advertproject.R;
import com.example.asus.advertproject.model.Advert;
import com.example.asus.advertproject.permissions.Permissions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity {
    private static final String TAG = "AddActivity";
    ImageView imageView;
    Button addPhotoButton, publishButton, locationButton;
    Uri imageUri;
    ListView lst;
    private List<Bitmap> imageList;
    private static final int PICK_IMAGE=100;
    private final static int MAPS_COORDINATE_ADD = 101;
    private MyListViewAdapter adapter;
    private StorageReference mStorageRef;
    private FirebaseAuth auth;
    private ProgressDialog mProgressDialog;
    private ArrayList<Uri> imguris;
    private EditText descriptionEt,titleEt;
    private DatabaseReference mDatabase;
    private ArrayList<String> urls;
    private Double mlatitude, mlongitude;
    private int counter=0;
    String userID = "000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAction));
        }
        final ListView lvImages = (ListView) findViewById(R.id.listView);
        imguris=new ArrayList<>();
        imageList=new ArrayList<>();
        urls=new ArrayList<>();
        checkFilePermissions();
          adapter= new MyListViewAdapter(this, imageList);
        lvImages.setAdapter(adapter);
        auth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mProgressDialog = new ProgressDialog(AddActivity.this);

        titleEt=(EditText) findViewById(R.id.titleET);
        descriptionEt=(EditText) findViewById(R.id.descriptionET);


        addPhotoButton =(Button) findViewById(R.id.addPhotoBtn);
        publishButton=(Button) findViewById(R.id.publishBtn);
        locationButton = findViewById(R.id.locationBtn);

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
                //finish();
            }
        });
        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddActivity.this, MapActivity.class);
                //Log.d(TAG, "clickeddddddddddd");
                startActivityForResult(i, MAPS_COORDINATE_ADD);
            }
        });
    }

    private void openGallery()
    {
        Intent gallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }
    private void upload() {
        Log.d(TAG, "onClick: Uploading Image.");


        //get the signed in user
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            userID = user.getUid();
        }
        mProgressDialog.setMessage("Uploading Image...");
        mProgressDialog.show();
        for (int i = 0; i < imguris.size(); i++) {

            String name = Long.toString(System.currentTimeMillis());
            Uri uri =imguris.get(i);
            Bitmap helper=null;
            Uri helper2=null;
            try {
                helper = decodeUri(getApplicationContext(), uri, 100);
                helper2=getImageUri(getApplicationContext(),helper);
            }
            catch (Exception e)
            {
                toastMessage("upload error");
            }
            if(helper2==null)
            {
                toastMessage("upload error helper2");
                finish();
            }
            StorageReference storageReference = mStorageRef.child("images/users/" + userID + "/" + name + ".jpg");
            storageReference.putFile(helper2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    // Get a URL to the uploaded content
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    urls.add(downloadUrl.toString());
                    counter++;
                    if(counter==imguris.size()) {
                        toastMessage("Upload Success");
                        if(urls.size()>0) {

                            Advert uj = new Advert(titleEt.getText().toString(), descriptionEt.getText().toString(), userID, Long.toString(System.currentTimeMillis()),
                                    mlatitude, mlongitude, urls.get(0), "gs://advertproject-10f39.appspot.com/profilepics/harambe.jpg", urls,"no");
                            String key = mDatabase.child("adverts").push().getKey();
                            mDatabase.child("adverts").child(key).setValue(uj);
                            finish();
                        }
                        else {
                            toastMessage("baj vban");
                        }
                        mProgressDialog.dismiss();

                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    toastMessage("Upload Failed");

                }
            })
            ;

        }


    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case PICK_IMAGE:
                if(resultCode != 0) {
                    imageUri = data.getData();
                    imguris.add(imageUri);
                    Bitmap im = null;
                    try {
                        im = decodeUri(getApplicationContext(), imageUri, 100);
                    } catch (Exception e) {
                        //
                    }
                    if (im != null)
                        adapter.add(im);
                    adapter.notifyDataSetChanged();
                }
                break;

            case MAPS_COORDINATE_ADD:
                if(resultCode != 0) {
                    mlatitude = data.getDoubleExtra("latitude", 0);
                    mlongitude = data.getDoubleExtra("longitude", 0);
                    if(mlatitude == 0 && mlongitude == 0){
                        Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT)
                                .show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Coordinates set", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                break;
            default:
                break;
        }
    }

    public static Bitmap decodeUri(Context c, Uri uri, final int requiredSize)
            throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth
                , height_tmp = o.outHeight;
        int scale = 1;

        while(true) {
            if(width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private void checkFilePermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Permissions.permissionRequest(this, Permissions.permissions, Permissions.PERMISSION_KEY);
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }


}
