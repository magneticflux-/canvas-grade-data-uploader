package com.skaggsm.gradeupload.cli

import picocli.CommandLine.ITypeConverter

import scala.concurrent.duration.Duration

/**
  * Created by Mitchell Skaggs on 9/22/2018.
  */
class DurationTypeConverter extends ITypeConverter[Duration] {
  override def convert(value: String): Duration = {
    Duration(value)
  }
}
