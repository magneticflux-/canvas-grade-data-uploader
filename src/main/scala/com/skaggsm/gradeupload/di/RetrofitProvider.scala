package com.skaggsm.gradeupload.di

import com.google.gson.Gson
import javax.inject.{Inject, Provider}
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.scala.ScalaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
  * Created by Mitchell Skaggs on 9/22/2018.
  */
class RetrofitProvider @Inject()(val okHttpClient: OkHttpClient, val gson: Gson) extends Provider[Retrofit] {

  override def get(): Retrofit = {
    new Retrofit.Builder()
      .client(okHttpClient)
      .baseUrl("https://mst.instructure.com")
      .validateEagerly(true)
      .addCallAdapterFactory(ScalaCallAdapterFactory.create())
      .addConverterFactory(GsonConverterFactory.create(gson))
      .build()
  }
}
