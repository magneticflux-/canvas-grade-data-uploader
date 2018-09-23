package com.skaggsm.gradeupload.di

import java.nio.file.Paths

import com.google.inject.Provider
import okhttp3.Cache
import squants.information.Mebibytes

/**
  * Created by Mitchell Skaggs on 9/22/2018.
  */
class OkHttpCacheProvider extends Provider[Cache] {
  override def get(): Cache = {
    new Cache(Paths.get(System.getProperty("java.io.tmpdir")).toFile, Mebibytes(1).toBytes.toLong)
  }
}
