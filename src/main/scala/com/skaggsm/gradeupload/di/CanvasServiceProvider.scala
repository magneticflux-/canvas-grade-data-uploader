package com.skaggsm.gradeupload.di

import com.skaggsm.gradeupload.canvas.CanvasService
import javax.inject.{Inject, Provider}
import retrofit2.Retrofit

/**
  * Created by Mitchell Skaggs on 9/22/2018.
  */
class CanvasServiceProvider @Inject()(val retrofit: Retrofit) extends Provider[CanvasService] {

  override def get(): CanvasService = {
    retrofit.create(classOf[CanvasService])
  }
}
