package com.skaggsm.gradeupload

import com.skaggsm.gradeupload.cli.GuiceFactory
import okhttp3.OkHttpClient
import picocli.CommandLine

/**
  * Created by Mitchell Skaggs on 9/20/2018.
  */
object Main {
  def main(args: Array[String]): Unit = {
    val factory = new GuiceFactory

    CommandLine.run(classOf[GradeCommand], factory, args: _*)

    val client = factory.injector.getInstance(classOf[OkHttpClient])
    client.dispatcher().executorService().shutdown()
  }
}
