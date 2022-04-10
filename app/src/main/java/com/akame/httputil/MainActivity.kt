package com.akame.httputil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.akame.httputil.repository.MainRepository
import com.akame.httputil.viewmodel.MainViewModel
import com.akame.httputil.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private val mainViewModel by lazy {
        MainViewModelFactory(MainRepository()).create(MainViewModel::class.java)
    }

    private val downLoadPath =
        "https://imtt.dd.qq.com/sjy.10001/16891/apk/871CF4013280C18F69D61F44DABDFD56.apk?fsname=com.tplink.cloudrouter_5.6.26_156.apk&csr=3554"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getData()
        mainViewModel.testDownLoadFile(downLoadPath, getSaveFilePath())
    }

    private fun getData() {
        mainViewModel.getBanner().observe(this) {
            Log.e("tag", "---requestResult--->>  ${it.data?.toString() ?: ""}")
        }
    }

    private fun getSaveFilePath(): String {
        return externalCacheDir?.let {
            it.absolutePath + "/呵呵.apk"
        } ?: ""
    }
}