package cn.naughtychild.rxdemo.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import cn.naughtychild.rxdemo.R
import com.company.prodemo.WeatherEntity
import com.company.prodemo.WeatherService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class Main2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        doRequest()
    }

    fun doRequest() {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://wthrcdn.etouch.cn/")//基础URL 建议以 / 结尾
                .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//RxJava 适配器
                .build();
        val weatherService = retrofit.create(WeatherService::class.java)
        weatherService.getMessage("北京")
                .subscribeOn(Schedulers.io())//IO线程加载数据
                .observeOn(AndroidSchedulers.mainThread())//主线程显示数据
                .subscribe(object : Subscriber<WeatherEntity>() {
                    override fun onNext(weatherEntity: WeatherEntity?) {
                        Log.e("MainActivity", "response == " + weatherEntity?.data );
                    }

                    override fun onCompleted() {
                        Log.d("MainActivity", "onCompleted: ");
                    }

                    override fun onError(e: Throwable?) {
                        Log.d("MainActivity", "onError: ${e!!.printStackTrace()}");
                    }
                });
    }
}
