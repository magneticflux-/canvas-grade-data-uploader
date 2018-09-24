package com.skaggsm.gradeupload.di

import com.google.gson.{FieldNamingPolicy, Gson, GsonBuilder}
import javax.inject.Provider

/**
  * Created by Mitchell Skaggs on 9/23/2018.
  */
class GsonProvider extends Provider[Gson] {
  override def get(): Gson = {
    new GsonBuilder()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
      .create()
  }
}
