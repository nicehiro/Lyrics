package nicehiro.org.lyrics.utils;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import nicehiro.org.lyrics.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {

  private RetrofitUtil(RetrofitUtil retrofitUtil) {
  }

  private static Retrofit retrofit;

  private static Retrofit create() {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    builder.readTimeout(10, TimeUnit.SECONDS);
    builder.connectTimeout(9, TimeUnit.SECONDS);

    if (BuildConfig.DEBUG) {
      HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
      interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
      builder.addInterceptor(interceptor);
    }

    return new Retrofit.Builder().baseUrl(Constants.BASE_URL)
      .client(builder.build())
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .build();
  }

  public static Retrofit getInstance() {
    if (retrofit == null) {
      retrofit = create();
    }
    return retrofit;
  }
}
