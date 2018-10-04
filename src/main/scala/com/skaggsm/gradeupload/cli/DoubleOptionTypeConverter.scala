package com.skaggsm.gradeupload.cli

import picocli.CommandLine.ITypeConverter

/**
  * Created by Mitchell Skaggs on 9/22/2018.
  */
class DoubleOptionTypeConverter extends ITypeConverter[Option[Double]] {
  override def convert(value: String): Option[Double] = {
    Option(value).map(_.toDouble)
  }
}
