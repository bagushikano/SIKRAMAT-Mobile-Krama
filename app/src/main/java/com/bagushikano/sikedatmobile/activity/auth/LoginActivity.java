package com.bagushikano.sikedatmobile.activity.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bagushikano.sikedatmobile.R;
import com.bagushikano.sikedatmobile.activity.HomeActivity;
import com.bagushikano.sikedatmobile.activity.register.RegisterCheckActivity;
import com.bagushikano.sikedatmobile.api.ApiRoute;
import com.bagushikano.sikedatmobile.api.RetrofitClient;
import com.bagushikano.sikedatmobile.model.AuthResponse;
import com.bagushikano.sikedatmobile.model.PendudukGetResponse;
import com.bagushikano.sikedatmobile.model.ResponseGeneral;
import com.bagushikano.sikedatmobile.util.CloseKeyboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextView registerButton;
    private Button loginButton, forgotPassButton;
    private TextInputLayout loginUsernameLayout, loginPasswordLayout;
    private TextInputEditText loginUsernameField, loginPasswordField;
    private LinearLayout loginProgressLayout;

    private SharedPreferences loginPreferences, userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar snackbar = Snackbar.make(
                        getWindow().getDecorView().findViewById(android.R.id.content),
                        R.string.login_screen_welcome_string,Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }, 1000);

        forgotPassButton = findViewById(R.id.forgot_pass_button);
        loginUsernameLayout = findViewById(R.id.username_form);
        loginUsernameField = findViewById(R.id.username_text_field);
        loginPasswordLayout = findViewById(R.id.password_form);
        loginPasswordField = findViewById(R.id.password_text_field);
        loginProgressLayout = findViewById(R.id.login_progress_layout);

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        userPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);

        registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getApplicationContext(), RegisterCheckActivity.class);
                startActivity(registerIntent);
            }
        });

        forgotPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://sikramat.ngaeapp.com/password/reset";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });

        if (loginPreferences.getInt("login_status", 0) == 1) {
            Intent home = new Intent(this, HomeActivity.class);
            startActivity(home);
            finish();
        }
    }

    public void submitData() {
        CloseKeyboard.CloseKeyboard(getCurrentFocus(), getApplicationContext());
        loginButton.setVisibility(View.GONE);
        loginProgressLayout.setVisibility(View.VISIBLE);
        ApiRoute submitData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<AuthResponse> authResponseCall = submitData.loginUser(loginUsernameField.getText().toString(), loginPasswordField.getText().toString());
        authResponseCall.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.code() == 200 && response.body().getStatusCode() == 200
                        && response.body().getMessage().equals("Login berhasil")) {
                    SharedPreferences.Editor loginPrefEditor = loginPreferences.edit();
                    loginPrefEditor.putInt("login_status", 1);
                    loginPrefEditor.putString("token", response.body().getToken());
                    loginPrefEditor.apply();
                    sendFirebaseToken(response.body().getToken());
                    getPenduduk(response.body().getToken());
                } else if (response.code() == 200 && response.body().getStatusCode() == 401 && response.body().getMessage().equals("Username/Email/No HP atau password salah")) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Username/email/No hp atau password salah", Snackbar.LENGTH_SHORT).show();
                    loginButton.setVisibility(View.VISIBLE);
                    loginProgressLayout.setVisibility(View.GONE);
                } else if (response.code() == 200 && response.body().getStatusCode() == 403
                        && response.body().getMessage().equals("email not verified")) {
                    loginButton.setVisibility(View.VISIBLE);
                    loginProgressLayout.setVisibility(View.GONE);
                    Intent emailVerif = new Intent(getApplicationContext(), EmailVerificationActivity.class);
                    startActivity(emailVerif);
                }
                else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                    loginButton.setVisibility(View.VISIBLE);
                    loginProgressLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                loginButton.setVisibility(View.VISIBLE);
                loginProgressLayout.setVisibility(View.GONE);
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void getPenduduk(String token) {
        ApiRoute getPendudukData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<PendudukGetResponse> pendudukGetResponseCall = getPendudukData.getPenduduk("Bearer " + token);
        pendudukGetResponseCall.enqueue(new Callback<PendudukGetResponse>() {
            @Override
            public void onResponse(Call<PendudukGetResponse> call, Response<PendudukGetResponse> response) {
                if (response.code() == 200 && response.body().getStatusCode() == 200
                        && response.body().getMessage().equals("data penduduk sukses")) {
                    String namaFormated = response.body().getPenduduk().getNama();
                    if (response.body().getPenduduk().getGelarDepan() != null) {
                        namaFormated = String.format("%s %s",
                                response.body().getPenduduk().getGelarDepan(),
                                response.body().getPenduduk().getNama()
                        );
                    }
                    if (response.body().getPenduduk().getGelarBelakang() != null) {
                        namaFormated = String.format("%s %s",
                                namaFormated,
                                response.body().getPenduduk().getGelarBelakang()
                        );
                    }
                    SharedPreferences.Editor userPrefEditor = userPreferences.edit();
                    userPrefEditor.putString("nama_user", response.body().getPenduduk().getNama());
                    userPrefEditor.apply();
                } else {
                    SharedPreferences.Editor userPrefEditor = userPreferences.edit();
                    userPrefEditor.putString("nama_user", null);
                    userPrefEditor.apply();
                }
                loginButton.setVisibility(View.VISIBLE);
                loginProgressLayout.setVisibility(View.GONE);
                Intent homeActivity = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(homeActivity);
                finishAffinity();
            }

            @Override
            public void onFailure(Call<PendudukGetResponse> call, Throwable t) {
                SharedPreferences.Editor userPrefEditor = userPreferences.edit();
                userPrefEditor.putString("nama_user", null);
                userPrefEditor.apply();
                Intent homeActivity = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(homeActivity);
                finishAffinity();
            }
        });
    }

    public void sendFirebaseToken(String tokenUser) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("fcm", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        String token = task.getResult();
                        Log.d("fcm", token);
                        ApiRoute storeToken = RetrofitClient.buildRetrofit().create(ApiRoute.class);
                        Call<ResponseGeneral> fcmTokenResponseCall = storeToken.sendFcmToken("Bearer " + tokenUser, token);
                        fcmTokenResponseCall.enqueue(new Callback<ResponseGeneral>() {
                            @Override
                            public void onResponse(Call<ResponseGeneral> call, Response<ResponseGeneral> response) {
                                if (response.code() == 200 && response.body().getMessage().equals("Berhasil memperbarui token")) {
                                    Log.d("fcm", "token fcm dikirim");
                                    FirebaseMessaging.getInstance().subscribeToTopic("all");
                                } else {
                                    Log.d("fcm", "token fcm gagal dikirim");
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseGeneral> call, Throwable t) {
                                Log.d("fcm", "token fcm gagal dikirim");
                            }
                        });

                    }
                });
    }
}