package com.skaggsm.gradeupload.di

import javax.inject.{Inject, Provider}
import okhttp3._

/**
  * Created by Mitchell Skaggs on 9/22/2018.
  */
class OkHttpProvider @Inject()(val cache: Cache) extends Provider[OkHttpClient] {
  override def get(): OkHttpClient = {
    new OkHttpClient.Builder()
      //.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
      .cache(cache)
      .build()
  }
}
