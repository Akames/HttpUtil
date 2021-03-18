package com.akame.httputil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.akame.http.AnalyzeNetException
import com.akame.http.ServerImpl
import com.akame.http.requestServer
import com.akame.httputil.net.AppService
import com.akame.httputil.net.HttpRequest
import kotlinx.coroutines.*
import java.lang.Exception

class MainActivity : AppCompatActivity(),CoroutineScope by MainScope(),ServerImpl{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //记得配置HttpConfig
        //创建request
        val retrofit = HttpRequest().getRetrofit(AppService::class.java, "https://gank.io/api/v2/")

        //开启协程请求
        requestServer({
            retrofit.getTest()
        },{
            it?.apply {
                Log.e("net",it[0].image)
            }
        })
    }

    // 实现ServerImpl请求接口 任何类只要实现了这个接口就可以进行网络请求 建议这步操作写在base里面
    override fun launchService(tryBlock: suspend CoroutineScope.() -> Unit, catchBlock: (String) -> Unit, finalBlock: () -> Unit) {
        launch(Dispatchers.Main) {
            try {
                tryBlock()
            }catch (e:Exception){
                catchBlock(AnalyzeNetException.analyze(e))
            }finally {
                finalBlock.invoke()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //记得不要忘记关闭协程
        cancel()
    }
}