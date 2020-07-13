package com.catvinhquang.okhttpcache;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.catvinhquang.okhttpcache.service.ApiService;
import com.catvinhquang.okhttpcache.service.CreateUserRequest;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by QuangCV on 13-Jul-2020
 **/

public class MainActivity extends Activity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ApiService service;

    private StringBuilder stringBuilder = new StringBuilder();
    private TextView tvConsole;
    private int userId = 1;

    private ApiService initService(Context context) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
            Platform.get().log(message, Platform.INFO, null);
            stringBuilder.append(message).append("\n");
            runOnUiThread(() -> tvConsole.setText(stringBuilder));
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new CacheInterceptor(context))
                .addInterceptor(loggingInterceptor)
                .cache(new Cache(context.getCacheDir(), Long.MAX_VALUE))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://reqres.in/api/")
                .build();

        return retrofit.create(ApiService.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = initService(this);
        setContentView(R.layout.main);

        tvConsole = findViewById(R.id.tv_console);
        tvConsole.setMovementMethod(new ScrollingMovementMethod());
        tvConsole.setText("Hello world!");

        findViewById(R.id.btn_get).setOnClickListener(view -> {
            stringBuilder = new StringBuilder();
            Disposable disposable = service.getUser(userId++)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            response -> {
                                // do something...
                            },
                            Throwable::printStackTrace);
            compositeDisposable.add(disposable);

            if (userId > 5) {
                userId = 1;
            }
        });

        findViewById(R.id.btn_post).setOnClickListener(view -> {
            stringBuilder = new StringBuilder();
            stringBuilder.append("OkHttp: Don't cache non-GET responses\n\n");

            /**
             * OkHttp: Don't cache non-GET responses
             * @see okhttp3.Cache#put()
             **/
            CreateUserRequest request = new CreateUserRequest();
            request.name = "Quang";
            request.job = "Software Engineer";

            Disposable disposable = service.createUser(request)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            response -> {
                                // do something...
                            },
                            Throwable::printStackTrace);
            compositeDisposable.add(disposable);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

}