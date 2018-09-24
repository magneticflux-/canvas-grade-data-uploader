package com.skaggsm.gradeupload.cli

import picocli.CommandLine.ITypeConverter

/**
  * Created by Mitchell Skaggs on 9/22/2018.
  */
class OptionTypeConverter extends ITypeConverter[Option[String]] {
  override def convert(value: String): Option[String] = {
    Option.apply(value)
  }
}
