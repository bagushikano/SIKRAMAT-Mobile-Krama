package com.bagushikano.sikedatmobile.activity.cacahkrama;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bagushikano.sikedatmobile.R;
import com.bagushikano.sikedatmobile.activity.mutasi.kelahiran.KelahiranPengajuanBaruComplete;
import com.bagushikano.sikedatmobile.api.ApiRoute;
import com.bagushikano.sikedatmobile.api.RetrofitClient;
import com.bagushikano.sikedatmobile.model.kelahiran.KelahiranPengajuanResponse;
import com.bagushikano.sikedatmobile.model.profile.ProfileEditFotoResponse;
import com.bagushikano.sikedatmobile.util.ChangeDateTimeFormat;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CacahKramaEditFotoActivity extends AppCompatActivity {

    private Button submitFotoButton, kramaEditFotoProfilePictSelectButton,  kramaEditFotoProfilePictCaptureButton;
    Uri resultUri, tempUri, aktaKelahiranFileUri, captureCameraTempUri;
    private ImageView kramaEditFotoProfilePict;
    UCrop.Options options = new UCrop.Options();

    ApiRoute retro;

    LinearLayout fotoProgressContainer;

    SharedPreferences loginPreferences;

    private static final int REQUEST_CAMERA = 100;
    private static final int MY_PERMISSION_REQUEST_CAMERA = 101;

    private Toolbar homeToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cacah_krama_edit_foto);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        submitFotoButton = findViewById(R.id.krama_edit_foto_submit_button);
        kramaEditFotoProfilePictSelectButton = findViewById(R.id.krama_edit_foto_profile_pict_select_button);
        kramaEditFotoProfilePict = findViewById(R.id.krama_edit_foto_image);
        fotoProgressContainer = findViewById(R.id.krama_edit_foto_progress_layout);

        if (getIntent().getStringExtra("FOTO") != null) {
            Picasso.get()
                    .load(getResources().getString(R.string.image_endpoint) + getIntent().getStringExtra("FOTO"))
                    .into(kramaEditFotoProfilePict, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(R.drawable.paceholder_krama_pict).into(kramaEditFotoProfilePict);
                        }
                    });
        }
        else {
            Picasso.get().load(R.drawable.paceholder_krama_pict).into(kramaEditFotoProfilePict);
        }

        retro = RetrofitClient.buildRetrofit().create(ApiRoute.class);

        submitFotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });

        kramaEditFotoProfilePictSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                String[] mimeTypes = {"image/png", "image/jpg", "image/jpeg"};
                chooseFile.setType("*/*");
                chooseFile.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                chooseFile = Intent.createChooser(chooseFile, "Pilih foto");
                startActivityForResult(chooseFile, 1);
            }
        });


        kramaEditFotoProfilePictCaptureButton = findViewById(R.id.krama_edit_foto_profile_pict_capture_button);
        kramaEditFotoProfilePictCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPermissionCameraGranted()) {
                    captureImage();
                }
            }
        });
    }


    public void cropPicture(Uri sourceUri) {
        try {
            File.createTempFile("profile.jpg", null, getApplicationContext().getCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
        }
        File profilePictTemp = new File(getApplicationContext().getCacheDir(), "profile.jpg");
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(90);
        UCrop.of(sourceUri, Uri.fromFile(profilePictTemp))
                .withOptions(options)
                .withAspectRatio(3, 4)
                .withMaxResultSize(600, 800)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            resultUri = UCrop.getOutput(data);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                kramaEditFotoProfilePict.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }

        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            File out = new File(getApplicationContext().getCacheDir(), "profile_camera.jpg");
//            if (!out.exists()) {
//                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Gagal dalam mengambil gambar", Snackbar.LENGTH_SHORT).show();
//                return;
//            }
            cropPicture(Uri.fromFile(out));
        }

        if (resultCode == RESULT_OK && requestCode == 1) {
            tempUri = data.getData();
            cropPicture(tempUri);
        }
    }

    private void captureImage() {
        try {
            File.createTempFile("profile_camera.jpg", null, getApplicationContext().getCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
        }
        File profilePictCamera = new File(getApplicationContext().getCacheDir(), "profile_camera.jpg");
        PackageManager pm = getPackageManager();
        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
            i.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
                    getApplicationContext(),
                    "com.bagushikano.sikedatmobile.fileprovider",
                    profilePictCamera));
            startActivityForResult(i, REQUEST_CAMERA);
        } else {
            Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Kamera tidak tersedia", Snackbar.LENGTH_SHORT).show();
        }
    }

    private boolean isPermissionCameraGranted() {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Log.v("TAG", "Permission is granted");
            return true;
        } else {
            Log.v("TAG", "Permission is revoked");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CAMERA);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                captureImage();
            } else {
                Log.d("camera_permission", "REQUEST_CAMERA :: " + "not granted");
            }
        }
    }

    public void submitData() {
        RequestBody fotoData;
        Call<ProfileEditFotoResponse> profileEditFotoResponseCall = null;
        MultipartBody.Part fotoFile = null;

        /**
         * multi part foto
         */
        if (resultUri != null ) {
            File imageFile = new File(getApplicationContext().getCacheDir(), "profile.jpg");
            fotoData = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
            fotoFile = MultipartBody.Part.createFormData("foto", imageFile.getName(), fotoData);
        }

        profileEditFotoResponseCall = retro.profileEditFoto(
                "Bearer " + loginPreferences.getString("token", "empty"),
                fotoFile
        );

        submitFotoButton.setVisibility(View.GONE);
        fotoProgressContainer.setVisibility(View.VISIBLE);

        profileEditFotoResponseCall.enqueue(new Callback<ProfileEditFotoResponse>() {
            @Override
            public void onResponse(Call<ProfileEditFotoResponse> call, Response<ProfileEditFotoResponse> response) {
                submitFotoButton.setVisibility(View.VISIBLE);
                fotoProgressContainer.setVisibility(View.GONE);
                if (response.code() == 200 && response.body().getStatusCode() == 200 && response.body().getMessage().equals("update data foto berhasil")) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Foto berhasil di ganti, akan di butuhkan beberapa saat hingga perubahan terlihat pada sistem.", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileEditFotoResponse> call, Throwable t) {
                submitFotoButton.setVisibility(View.VISIBLE);
                fotoProgressContainer.setVisibility(View.GONE);
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}