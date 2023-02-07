package com.bagushikano.sikedatmobile.activity.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bagushikano.sikedatmobile.R;
import com.bagushikano.sikedatmobile.api.ApiRoute;
import com.bagushikano.sikedatmobile.api.RetrofitClient;
import com.bagushikano.sikedatmobile.model.RegisterResponse;
import com.bagushikano.sikedatmobile.model.master.Penduduk;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private Button registerButton;
    private TextInputEditText emailField, passwordField, cPasswordField;
    private TextInputLayout emailLayout, passwordLayout, cPasswordLayout;
    private LinearLayout registerLoadingLayout;
    private MaterialCardView registerEmailUsedCard;
    private Boolean isEmailValid, isPasswordValid, isPasswordCorrect;
    private Toolbar homeToolbar;
    private Penduduk penduduk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Gson gson = new Gson();
        penduduk = gson.fromJson(getIntent().getStringExtra("REGISTER_KEY"), Penduduk.class);

        registerButton = findViewById(R.id.register_submit_button);
        emailField = findViewById(R.id.register_email_field);
        passwordField = findViewById(R.id.register_password_field);
        cPasswordField = findViewById(R.id.register_cpassword_field);
        emailLayout = findViewById(R.id.register_email_form);
        passwordLayout = findViewById(R.id.register_password_form);
        cPasswordLayout = findViewById(R.id.register_cpassword_form);
        registerLoadingLayout = findViewById(R.id.register_progress_layout);
        registerEmailUsedCard = findViewById(R.id.register_email_already_used_card);

        emailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!isEmailValid(emailField.getText().toString())) {
                    emailLayout.setError("Email tidak valid");
                    isEmailValid = false;
                } else {
                    emailLayout.setError(null);
                    isEmailValid = true;
                }
            }
        });

        passwordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // TODO add more password criteria
                if (passwordField.getText().toString().length() < 8) {
                    passwordLayout.setError("Password kurang dari 8 karakter");
                    isPasswordValid = false;
                } else {
                    passwordLayout.setError(null);
                    isPasswordValid = true;
                }
            }
        });

        cPasswordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!(passwordField.getText().toString().equals(cPasswordField.getText().toString()))) {
                    cPasswordLayout.setError("Password tidak cocok");
                    isPasswordCorrect = false;
                } else {
                    cPasswordLayout.setError(null);
                    isPasswordCorrect = true;
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });
    }


    public void submitData() {
        if (isEmailValid && isPasswordValid && isPasswordCorrect) {
            registerButton.setVisibility(View.GONE);
            registerLoadingLayout.setVisibility(View.VISIBLE);
            registerEmailUsedCard.setVisibility(View.GONE);
            ApiRoute submitData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
            Call<RegisterResponse> registerResponseCall = submitData.registerUser(
                    penduduk.getId(),
                    emailField.getText().toString(),
                    cPasswordField.getText().toString()
            );
            registerResponseCall.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    registerButton.setVisibility(View.VISIBLE);
                    registerLoadingLayout.setVisibility(View.GONE);
                    registerEmailUsedCard.setVisibility(View.GONE);
                    if (response.code() == 200 && response.body().getStatusCode() == 200 && response.body().getMessage().equals("register berhasil")) {
                        Intent registerComplete = new Intent(getApplicationContext(), RegisterCompleteActivity.class);
                        startActivity(registerComplete);
                        finish();
                    } else if (response.code() == 200 && response.body().getStatusCode() == 500 && response.body().getMessage().equals("email sudah digunakan")) {
                        registerEmailUsedCard.setVisibility(View.VISIBLE);
                    } else {
                        Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    registerButton.setVisibility(View.VISIBLE);
                    registerLoadingLayout.setVisibility(View.GONE);
                    registerEmailUsedCard.setVisibility(View.GONE);
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
            });
        }

        else{
            Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Terdapat data yang tidak valid, periksa data kembali", Snackbar.LENGTH_SHORT).show();
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}