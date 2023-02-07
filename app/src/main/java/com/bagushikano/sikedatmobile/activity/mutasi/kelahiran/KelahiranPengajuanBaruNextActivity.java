package com.bagushikano.sikedatmobile.activity.mutasi.kelahiran;

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
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bagushikano.sikedatmobile.R;
import com.bagushikano.sikedatmobile.activity.register.RegisterCompleteActivity;
import com.bagushikano.sikedatmobile.adapter.kelahiran.KelahiranOrangTuaSelectionAdapter;
import com.bagushikano.sikedatmobile.api.ApiRoute;
import com.bagushikano.sikedatmobile.api.RetrofitClient;
import com.bagushikano.sikedatmobile.model.RegisterResponse;
import com.bagushikano.sikedatmobile.model.kelahiran.KelahiranAjuan;
import com.bagushikano.sikedatmobile.model.kelahiran.KelahiranPengajuanResponse;
import com.bagushikano.sikedatmobile.model.krama.AnggotaKramaMipil;
import com.bagushikano.sikedatmobile.model.krama.KramaMipil;
import com.bagushikano.sikedatmobile.model.master.Penduduk;
import com.bagushikano.sikedatmobile.util.ChangeDateTimeFormat;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KelahiranPengajuanBaruNextActivity extends AppCompatActivity {

    private Button kelahiranPengajuanSubmitButton, kelahiranPengajuanProfilePictSelectButton, kelahiranPengajuanProfilePictCaptureButton;
    private TextInputEditText kelahiranPengajuanNamaField, kelahiranPengajuanTempatLahirField,
            kelahiranPengajuanTanggalLahirField, kelahiranPengajuanAlamatField, keteranganPenajuanField;

    private AutoCompleteTextView kelahiranPengajuanJenisKelaminField,
            kelahiranPengajuanGolonganDarahField, kelahiranPengajuanAyahField, kelahiranPengajuanIbuField;

    private TextInputLayout kelahiranPengajuanNamaLayout, kelahiranPengajuanTempatLahirLayout,
            kelahiranPengajuanTanggalLahirLayout, kelahiranPengajuanJenisKelaminLayout, kelahiranPengajuanAlamatLayout,
            kelahiranPengajuanGolonganDarahLayout, kelahiranPengajuanIbuLayout, kelahiranPengajuanAyahLayout;

    Uri resultUri, tempUri, aktaKelahiranFileUri, captureCameraTempUri;
    private ImageView kelahiranPengajuanFotoImage;
    UCrop.Options options = new UCrop.Options();

    String jenisKelamin, nikAnak, noAktaKelahiran;
    Integer ayakKandungId = null, ibuKandungId = null;
    Penduduk ayahPenduduk, ibuPenduduk;

    private String AKTA_FILE_KEY = "AKTA_FILE", AKTA_NO_KEY = "AKTA_NO", NIK_KEY = "NIK", KRAMA_MIPIL_KEY = "KRARMA_MIPIL";

    private boolean isNamaValid = false, isTempatLahirValid = false, isTanggalLahirValid = false,
            isJenisKelaminValid = false, isGoldarValid = false, isAlamatValid = false,
            isAyahSelected = false, isIbuSelected = false;

    ApiRoute retro;

    LinearLayout kelahiranProgressContainer;

    SharedPreferences loginPreferences;

    private static final int REQUEST_CAMERA = 100;
    private static final int MY_PERMISSION_REQUEST_CAMERA = 101;

    private Toolbar homeToolbar;

    private final String KELAHIRAN_RESUBMIT_KEY = "KELAHIRAN_RESUBMIT_DETAIL_KEY";
    Gson gson = new Gson();
    KelahiranAjuan kelahiranAjuan;


    /**
     * TODO
     * 3. Delete temp profile pict setelah requestnya nya berhasil
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelahiran_pengajuan_baru_next);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        kelahiranAjuan = gson.fromJson(getIntent().getStringExtra(KELAHIRAN_RESUBMIT_KEY), KelahiranAjuan.class);

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        kelahiranProgressContainer = findViewById(R.id.kelahiran_pengajuan_progress_layout);

        kelahiranPengajuanNamaField = findViewById(R.id.kelahiran_pengajuan_nama_anak_field);
        kelahiranPengajuanTempatLahirField = findViewById(R.id.kelahiran_pengajuan_tempat_lahir_field);
        kelahiranPengajuanTanggalLahirField = findViewById(R.id.kelahiran_pengajuan_tanggal_lahir_field);
        kelahiranPengajuanJenisKelaminField = findViewById(R.id.kelahiran_pengajuan_jk_field);
        kelahiranPengajuanAlamatField = findViewById(R.id.kelahiran_pengajuan_alamat_field);
        kelahiranPengajuanGolonganDarahField = findViewById(R.id.kelahiran_pengajuan_golongan_darah_field);
        kelahiranPengajuanAyahField = findViewById(R.id.kelahiran_pengajuan_ayah_field);
        kelahiranPengajuanIbuField = findViewById(R.id.kelahiran_pengajuan_ibu_field);
        keteranganPenajuanField = findViewById(R.id.kelahiran_pengajuan_keterangan_field);

        kelahiranPengajuanNamaLayout = findViewById(R.id.kelahiran_pengajuan_nama_anak_form);
        kelahiranPengajuanTempatLahirLayout = findViewById(R.id.kelahiran_pengajuan_tempat_lahir_form);
        kelahiranPengajuanTanggalLahirLayout = findViewById(R.id.kelahiran_pengajuan_tanggal_lahir_form);
        kelahiranPengajuanJenisKelaminLayout = findViewById(R.id.kelahiran_pengajuan_jk_form);
        kelahiranPengajuanAlamatLayout = findViewById(R.id.kelahiran_pengajuan_alamat_form);
        kelahiranPengajuanGolonganDarahLayout = findViewById(R.id.kelahiran_pengajuan_golongan_darah_form);
        kelahiranPengajuanIbuLayout = findViewById(R.id.kelahiran_pengajuan_ibu_form);
        kelahiranPengajuanAyahLayout = findViewById(R.id.kelahiran_pengajuan_ayah_form);

        retro = RetrofitClient.buildRetrofit().create(ApiRoute.class);

        Gson gson = new Gson();
        KramaMipil kramaMipil = gson.fromJson(getIntent().getStringExtra(KRAMA_MIPIL_KEY), KramaMipil.class);
        ArrayList<Penduduk> pendudukArrayList = new ArrayList<>();
        pendudukArrayList.add(kramaMipil.getCacahKramaMipil().getPenduduk());

        for (AnggotaKramaMipil anggotaKramaMipil: kramaMipil.getAnggotaKramaMipilList()) {
            pendudukArrayList.add(anggotaKramaMipil.getCacahKramaMipil().getPenduduk());
        };

        ArrayList<Penduduk> ibuSelectionList = new ArrayList<>();
        ArrayList<Penduduk> ayahSelectionList = new ArrayList<>(pendudukArrayList);

        KelahiranOrangTuaSelectionAdapter kelahiranAyahSelectionAdapter = new KelahiranOrangTuaSelectionAdapter(this, ayahSelectionList);
        KelahiranOrangTuaSelectionAdapter kelahiranIbuSelectionAdapter = new KelahiranOrangTuaSelectionAdapter(this, ibuSelectionList);
        kelahiranPengajuanAyahField.setAdapter(kelahiranAyahSelectionAdapter);
        kelahiranPengajuanIbuField.setAdapter(kelahiranIbuSelectionAdapter);
        kelahiranPengajuanAyahField.setThreshold(100);
        kelahiranPengajuanIbuField.setThreshold(100);

        kelahiranPengajuanAyahField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ayahPenduduk = (Penduduk) adapterView.getItemAtPosition(i);
                kelahiranPengajuanAyahField.setText(ayahPenduduk.getNama());
                if (!ibuSelectionList.isEmpty()) {
                    ibuSelectionList.clear();
                }

                isAyahSelected = true;
                isIbuSelected = false;

                ibuSelectionList.addAll(ayahSelectionList);
                ibuSelectionList.remove(ayahPenduduk);

                kelahiranIbuSelectionAdapter.notifyDataSetChanged();
                kelahiranPengajuanIbuLayout.setHint("Pilih ibu");
                kelahiranPengajuanIbuLayout.setEnabled(true);
                kelahiranPengajuanIbuField.setText("");
            }
        });

        kelahiranPengajuanIbuField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ibuPenduduk = (Penduduk) adapterView.getItemAtPosition(i);
                kelahiranPengajuanIbuField.setText(ibuPenduduk.getNama());
                isIbuSelected = true;
            }
        });


        nikAnak = getIntent().getStringExtra(NIK_KEY);
        noAktaKelahiran = getIntent().getStringExtra(AKTA_NO_KEY);
        if (getIntent().hasExtra(AKTA_FILE_KEY)) {
            aktaKelahiranFileUri = Uri.parse(getIntent().getStringExtra(AKTA_FILE_KEY));
        }

        String[] jkAnak = new String[] {"Laki-Laki", "Perempuan"};
        ArrayAdapter adapterJk = new ArrayAdapter<>(this, R.layout.dropdown_menu_item, jkAnak);
        kelahiranPengajuanJenisKelaminField.setAdapter(adapterJk);
        kelahiranPengajuanJenisKelaminField.setInputType(0);
        kelahiranPengajuanJenisKelaminField.setKeyListener(null);

        String[] goldarAnak = new String[] {"A", "B", "AB", "O", "-"};
        ArrayAdapter adapterGoldar = new ArrayAdapter<>(this, R.layout.dropdown_menu_item, goldarAnak);
        kelahiranPengajuanGolonganDarahField.setAdapter(adapterGoldar);
        kelahiranPengajuanGolonganDarahField.setInputType(0);
        kelahiranPengajuanGolonganDarahField.setKeyListener(null);

        kelahiranPengajuanGolonganDarahField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isGoldarValid = true;
            }
        });

        kelahiranPengajuanJenisKelaminField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    jenisKelamin = "laki-laki";
                }
                else {
                    jenisKelamin = "perempuan";
                }
                isJenisKelaminValid = true;
            }
        });

        MaterialDatePicker.Builder<Long> datePickerBuilderAppointmentDate = MaterialDatePicker.Builder.datePicker();
        datePickerBuilderAppointmentDate.setTitleText("Pilih tanggal lahir anak");
        final MaterialDatePicker<Long> datePickerAppointmentDate = datePickerBuilderAppointmentDate.build();
        datePickerAppointmentDate.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selectedDate) {
                // link: https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
                TimeZone timeZoneUTC = TimeZone.getDefault();
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                SimpleDateFormat simpleFormat = new SimpleDateFormat("EEE, dd-MM-yyyy", Locale.US);
                Date date = new Date(selectedDate + offsetFromUTC);
                kelahiranPengajuanTanggalLahirField.setText(simpleFormat.format(date));
                isTanggalLahirValid = true;
            }
        });

        kelahiranPengajuanTanggalLahirField.setShowSoftInputOnFocus(false);
        kelahiranPengajuanTanggalLahirField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(datePickerAppointmentDate.isVisible())) {
                    datePickerAppointmentDate.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                }
            }
        });

        kelahiranPengajuanTanggalLahirField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    if (!(datePickerAppointmentDate.isVisible())) {
                        datePickerAppointmentDate.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                }
            }
        });

        kelahiranPengajuanNamaField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(kelahiranPengajuanNamaField.getText().toString().length() == 0) {
                    kelahiranPengajuanNamaLayout.setError("Nama anak tidak boleh kosong");
                    kelahiranPengajuanNamaLayout.setErrorEnabled(true);
                    isNamaValid = false;
                }
                else {
                    if (kelahiranPengajuanNamaField.getText().toString().length() < 2) {
                        kelahiranPengajuanNamaLayout.setError("Nama anak tidak boleh kurang dari 2 karakter");
                        kelahiranPengajuanNamaLayout.setErrorEnabled(true);
                        isNamaValid = false;
                    }
                    else {
                        kelahiranPengajuanNamaLayout.setError(null);
                        kelahiranPengajuanNamaLayout.setErrorEnabled(false);
                        isNamaValid = true;
                    }
                }
            }
        });

        kelahiranPengajuanTempatLahirField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(kelahiranPengajuanTempatLahirField.getText().toString().length() == 0) {
                    kelahiranPengajuanTempatLahirLayout.setError("Tempat lahir anak tidak boleh kosong");
                    kelahiranPengajuanTempatLahirLayout.setErrorEnabled(true);
                    isTempatLahirValid = false;
                }
                else {
                    kelahiranPengajuanTempatLahirLayout.setError(null);
                    kelahiranPengajuanTempatLahirLayout.setErrorEnabled(false);
                    isTempatLahirValid = true;
                }
            }
        });

        kelahiranPengajuanAlamatField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(kelahiranPengajuanAlamatField.getText().toString().length() == 0) {
                    kelahiranPengajuanAlamatLayout.setError("Alamat anak tidak boleh kosong");
                    kelahiranPengajuanAlamatLayout.setErrorEnabled(true);
                    isAlamatValid = false;
                }
                else {
                    kelahiranPengajuanAlamatLayout.setError(null);
                    kelahiranPengajuanAlamatLayout.setErrorEnabled(false);
                    isAlamatValid = true;
                }
            }
        });

        kelahiranPengajuanSubmitButton = findViewById(R.id.kelahiran_pengajuan_submit_button);
        kelahiranPengajuanSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });

        kelahiranPengajuanFotoImage = findViewById(R.id.kelahiran_pengajuan_foto_image);
        kelahiranPengajuanProfilePictSelectButton = findViewById(R.id.kelahiran_pengajuan_profile_pict_select_button);
        kelahiranPengajuanProfilePictSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                String[] mimeTypes = {"image/png", "image/jpg", "image/jpeg"};
                chooseFile.setType("*/*");
                chooseFile.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, 1);
            }
        });
        kelahiranPengajuanProfilePictCaptureButton = findViewById(R.id.kelahiran_pengajuan_profile_pict_capture_button);
        kelahiranPengajuanProfilePictCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPermissionCameraGranted()) {
                    captureImage();
                }
            }
        });


        if (kelahiranAjuan != null) {
            if (kelahiranAjuan.getCacahKramaMipil().getPenduduk().getNama() != null) {
                kelahiranPengajuanNamaField.setText(kelahiranAjuan.getCacahKramaMipil().getPenduduk().getNama());
            }
            if (kelahiranAjuan.getCacahKramaMipil().getPenduduk().getTempatLahir() != null) {
                kelahiranPengajuanTempatLahirField.setText(kelahiranAjuan.getCacahKramaMipil().getPenduduk().getTempatLahir());
            }
            if (kelahiranAjuan.getCacahKramaMipil().getPenduduk().getAlamat() != null) {
                kelahiranPengajuanAlamatField.setText(kelahiranAjuan.getCacahKramaMipil().getPenduduk().getAlamat());
            }
            if (kelahiranAjuan.getKeterangan() != null) {
                keteranganPenajuanField.setText(kelahiranAjuan.getKeterangan());
            }
        }
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
                kelahiranPengajuanFotoImage.setImageBitmap(bitmap);
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
        if (isNamaValid && isTempatLahirValid && isTanggalLahirValid &&
                isJenisKelaminValid && isGoldarValid) {

            RequestBody nikData, namaData, tempatLahirData, tanggalLahirData, jenisKelaminData,
                    golonganDarahData, alamatData, ayahKandungData, ibuKandungData, nomorAktaKelahiranData,
                    fotoData, aktaKelahiranData, keteranganData;
            Call<KelahiranPengajuanResponse> pengajuanResponseCall = null;

            namaData = RequestBody.create(MediaType.parse("text/plain"), kelahiranPengajuanNamaField.getText().toString());
            tempatLahirData = RequestBody.create(MediaType.parse("text/plain"), kelahiranPengajuanTempatLahirField.getText().toString());
            tanggalLahirData = RequestBody.create(MediaType.parse("text/plain"),
                    ChangeDateTimeFormat.changeDateFormatForForm(kelahiranPengajuanTanggalLahirField.getText().toString()));
            jenisKelaminData = RequestBody.create(MediaType.parse("text/plain"), jenisKelamin);
            golonganDarahData = RequestBody.create(MediaType.parse("text/plain"), kelahiranPengajuanGolonganDarahField.getText().toString());
            alamatData = RequestBody.create(MediaType.parse("text/plain"), kelahiranPengajuanAlamatField.getText().toString());
            keteranganData = RequestBody.create(MediaType.parse("text/plain"), keteranganPenajuanField.getText().toString());

            ayahKandungData = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(ayahPenduduk.getId()));
            ibuKandungData = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(ibuPenduduk.getId()));

            nikData = RequestBody.create(MediaType.parse("text/plain"), nikAnak);
            nomorAktaKelahiranData = RequestBody.create(MediaType.parse("text/plain"), noAktaKelahiran);

            MultipartBody.Part fotoFile = null;
            MultipartBody.Part aktaKelahiranFile = null;

            /**
             * multi part foto
             */
            if (tempUri != null ) {
                File imageFile = new File(getApplicationContext().getCacheDir(), "profile.jpg");
                fotoData = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
                fotoFile = MultipartBody.Part.createFormData("foto", imageFile.getName(), fotoData);
            }

            /**
             * multi part akta kelahiran
             */

            if (aktaKelahiranFileUri != null) {
                File filesDir = getApplicationContext().getCacheDir();
                File aktaFile = new File(filesDir, DocumentFile.fromSingleUri(this, aktaKelahiranFileUri).getName());
                OutputStream os;
                ContentResolver cr = getApplicationContext().getContentResolver();
                InputStream inputStream = null;
                try {
                    inputStream = cr.openInputStream(aktaKelahiranFileUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    os = new FileOutputStream(aktaFile);
                    int length;
                    byte[] bytes = new byte[1024];
                    while ((length = inputStream.read(bytes)) != -1) {
                        os.write(bytes, 0, length);
                    }
                    os.flush();
                    os.close();
                    inputStream.close();
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Error writing temp file", e);
                }
                aktaKelahiranData = RequestBody.create(MediaType.parse("multipart/form-data"), aktaFile);
                aktaKelahiranFile = MultipartBody.Part.createFormData("file_akta_kelahiran", aktaFile.getName(), aktaKelahiranData);
            }

            pengajuanResponseCall = retro.kelahiranPengajuan(
                    "Bearer " + loginPreferences.getString("token", "empty"),
                    nikData, namaData, tempatLahirData, tanggalLahirData, jenisKelaminData,
                    golonganDarahData, alamatData, ayahKandungData, ibuKandungData,
                    nomorAktaKelahiranData, keteranganData, fotoFile, aktaKelahiranFile);

            kelahiranPengajuanSubmitButton.setVisibility(View.GONE);
            kelahiranProgressContainer.setVisibility(View.VISIBLE);

            pengajuanResponseCall.enqueue(new Callback<KelahiranPengajuanResponse>() {
                @Override
                public void onResponse(Call<KelahiranPengajuanResponse> call, Response<KelahiranPengajuanResponse> response) {
                    kelahiranPengajuanSubmitButton.setVisibility(View.VISIBLE);
                    kelahiranProgressContainer.setVisibility(View.GONE);
                    if (response.code() == 200 && response.body().getStatusCode() == 200 && response.body().getMessage().equals("pengajuan kelahiran berhasil")) {
                        Intent kelahiranPengajuanCompleteIntent = new Intent(getApplicationContext(), KelahiranPengajuanBaruComplete.class);
                        Gson gson = new Gson();
                        String kelahiranJSon = gson.toJson(response.body().getKelahiran());
                        kelahiranPengajuanCompleteIntent.putExtra("KELAHIRAN_KEY", kelahiranJSon);
                        startActivity(kelahiranPengajuanCompleteIntent);
                        setResult(1);
                        finish();
                    } else {
                        Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<KelahiranPengajuanResponse> call, Throwable t) {
                    kelahiranPengajuanSubmitButton.setVisibility(View.VISIBLE);
                    kelahiranProgressContainer.setVisibility(View.GONE);
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Terdapat data yang tidak valid, periksa data kembali", Snackbar.LENGTH_SHORT).show();
        }
    }
}