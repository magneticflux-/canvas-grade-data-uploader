package com.skaggsm.gradeupload

import picocli.CommandLine.{Command, Option}

/**
  * Created by Mitchell Skaggs on 9/21/2018.
  */
@Command(
  description = Array("Says \"hello\" to someone."),
  name = "canvas-grade-data-uploader",
  mixinStandardHelpOptions = true,
  version = Array("0.1.0"),
  showDefaultValues = true
)
class HelloWorldCommand extends Runnable {

  @Option(names = Array("-n", "--name"))
  var name: String = "World"

  override def run(): Unit = {
    println(s"Hello $name!")
  }
}