package com.skaggsm.gradeupload

import picocli.CommandLine

/**
  * Created by Mitchell Skaggs on 9/20/2018.
  */
object Main {
  def main(args: Array[String]): Unit = {
    CommandLine.run(new HelloWorldCommand, args: _*)
  }
}
