package com.skaggsm.gradeupload

import picocli.CommandLine.ITypeConverter

/**
  * Created by Mitchell Skaggs on 9/22/2018.
  */
class OptionTypeConverter extends ITypeConverter[Option[String]] {
  override def convert(value: String): Option[String] = {
    Option.apply(value)
  }
}
