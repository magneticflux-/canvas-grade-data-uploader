package com.skaggsm.gradeupload.cli

import com.google.inject.{Guice, Injector}
import com.skaggsm.gradeupload.di.GradeCommandModule
import picocli.CommandLine.IFactory

/**
  * Created by Mitchell Skaggs on 9/23/2018.
  */
class GuiceFactory extends IFactory {
  val injector: Injector = Guice.createInjector(new GradeCommandModule)

  override def create[K](cls: Class[K]): K = {
    injector.getInstance(cls)
  }
}
