package com.skaggsm.gradeupload.di

import com.google.gson.Gson
import com.google.inject.AbstractModule
import com.skaggsm.gradeupload.canvas.CanvasService
import javax.inject.Singleton
import net.codingwell.scalaguice.ScalaModule
import okhttp3.{Cache, OkHttpClient}
import retrofit2.Retrofit

/**
  * Created by Mitchell Skaggs on 9/22/2018.
  */
class MainCommandModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[Cache].toProvider[OkHttpCacheProvider]

    bind[OkHttpClient].toProvider[OkHttpProvider].in[Singleton]
    bind[Gson].toProvider[GsonProvider].in[Singleton]
    bind[Retrofit].toProvider[RetrofitProvider].in[Singleton]
    bind[CanvasService].toProvider[CanvasServiceProvider].in[Singleton]
  }
}
