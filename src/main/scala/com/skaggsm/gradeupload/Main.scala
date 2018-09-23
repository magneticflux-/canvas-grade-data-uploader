package com.skaggsm.gradeupload

import com.skaggsm.gradeupload.cli.GuiceFactory
import com.skaggsm.gradeupload.commands.MainCommand
import okhttp3.OkHttpClient
import picocli.CommandLine

/**
  * Created by Mitchell Skaggs on 9/20/2018.
  */
object Main {
  def main(args: Array[String]): Unit = {
    val factory = new GuiceFactory

    CommandLine.run(classOf[MainCommand], factory, args: _*)

    // Required because of https://github.com/square/okhttp/issues/4029
    val client = factory.injector.getInstance(classOf[OkHttpClient])
    client.dispatcher().executorService().shutdown()
  }
}
