package com.skaggsm.gradeupload.cli

import picocli.CommandLine.ITypeConverter

/**
  * Created by Mitchell Skaggs on 9/22/2018.
  */
class StringOptionTypeConverter extends ITypeConverter[Option[String]] {
  override def convert(value: String): Option[String] = {
    Option(value)
  }
}
