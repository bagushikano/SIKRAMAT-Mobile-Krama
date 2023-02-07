package com.bagushikano.sikedatmobile.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bagushikano.sikedatmobile.R;
import com.bagushikano.sikedatmobile.activity.mutasi.kelahiran.KelahiranAjuanDetailActivity;
import com.bagushikano.sikedatmobile.activity.mutasi.kematian.KematianAjuanDetailActivity;
import com.bagushikano.sikedatmobile.api.ApiRoute;
import com.bagushikano.sikedatmobile.api.RetrofitClient;
import com.bagushikano.sikedatmobile.model.ResponseGeneral;
import com.bagushikano.sikedatmobile.model.notifikasi.FcmMessage;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseMessaging extends FirebaseMessagingService {

    NotificationChannel channel;
    NotificationManagerCompat notificationManagerCompat;
    SharedPreferences loginPreferences;
    FcmMessage fcmMessage;
    Gson gson = new Gson();
    int i = 0;
    private final String KELAHIRAN_DETAIL_KEY = "KELAHIRAN_DETAIL_KEY";
    private final String FLAG_KELAHIRAN_DETAIL_KEY = "KELAHIRAN";

    private final String KEMATIAN_DETAIL_KEY = "KEMATIAN_DETAIL_KEY";
    private final String FLAG_KEMATIAN_DETAIL_KEY = "KEMATIAN";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data = remoteMessage.getData();
        Log.d("fcm", new JSONObject(data).toString());
        fcmMessage = gson.fromJson(new JSONObject(data).toString(), FcmMessage.class);
        Log.d("fcm", "hey ada notif");
        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        if (loginPreferences != null) {
            if (loginPreferences.getInt("login_status", -1) == 1) {
                if (fcmMessage.getType() == 0 && fcmMessage.getUserType() == 0) {
                    // kelahiran
                    Intent kelahiranAjuanIntent = new Intent(this, KelahiranAjuanDetailActivity.class);
                    kelahiranAjuanIntent.putExtra(KELAHIRAN_DETAIL_KEY, fcmMessage.getDataId());
                    kelahiranAjuanIntent.putExtra(FLAG_KELAHIRAN_DETAIL_KEY, 1);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                    stackBuilder.addNextIntentWithParentStack(kelahiranAjuanIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(0,
                                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "kelahiran")
                            .setSmallIcon(R.drawable.ic_notification)
                            .setContentTitle(fcmMessage.getTitle())
                            .setContentText(fcmMessage.getContent())
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(resultPendingIntent)
                            .setAutoCancel(true);
                    notificationManagerCompat = NotificationManagerCompat.from(this);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        channel = new NotificationChannel("kelahiran", "Kelahiran", NotificationManager.IMPORTANCE_DEFAULT);
                        notificationManagerCompat.createNotificationChannel(channel);
                    }
                    notificationManagerCompat.notify(i + 1, notificationBuilder.build());
                } else if (fcmMessage.getType() == 1 && fcmMessage.getUserType() == 0) {
                    // kematian
                    Intent kelahiranAjuanIntent = new Intent(this, KematianAjuanDetailActivity.class);
                    kelahiranAjuanIntent.putExtra(KEMATIAN_DETAIL_KEY, fcmMessage.getDataId());
                    kelahiranAjuanIntent.putExtra(FLAG_KEMATIAN_DETAIL_KEY, 1);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                    stackBuilder.addNextIntentWithParentStack(kelahiranAjuanIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(0,
                                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "kematian")
                            .setSmallIcon(R.drawable.ic_notification)
                            .setContentTitle(fcmMessage.getTitle())
                            .setContentText(fcmMessage.getContent())
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(resultPendingIntent)
                            .setAutoCancel(true);
                    notificationManagerCompat = NotificationManagerCompat.from(this);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        channel = new NotificationChannel("kematian", "Kematian", NotificationManager.IMPORTANCE_DEFAULT);
                        notificationManagerCompat.createNotificationChannel(channel);
                    }
                    notificationManagerCompat.notify(i + 1, notificationBuilder.build());
                }
            }
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        SharedPreferences loginPreferences;
        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        if (loginPreferences != null) {
            if (loginPreferences.getInt("login_status", 0) == 1) {
                ApiRoute storeToken = RetrofitClient.buildRetrofit().create(ApiRoute.class);
                Call<ResponseGeneral> fcmTokenResponseCall = storeToken.sendFcmToken("Bearer " + loginPreferences.getString("token", "empty"), s);
                fcmTokenResponseCall.enqueue(new Callback<ResponseGeneral>() {
                    @Override
                    public void onResponse(Call<ResponseGeneral> call, Response<ResponseGeneral> response) {
                        if (response.code() == 200 && response.body().getMessage().equals("Berhasil memperbarui token")) {
                            Log.d("fcm", "token fcm dikirim");
                            com.google.firebase.messaging.FirebaseMessaging.getInstance().subscribeToTopic("all");
                        }
                        else {
                            Log.d("fcm", "token fcm gagal dikirim");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseGeneral> call, Throwable t) {
                        Log.d("fcm", "token fcm gagal dikirim");
                    }
                });
            }
        }
    }
}
