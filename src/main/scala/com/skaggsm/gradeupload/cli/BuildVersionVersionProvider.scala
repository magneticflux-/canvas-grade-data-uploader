package com.skaggsm.gradeupload.cli

import build.BuildInfo
import picocli.CommandLine.IVersionProvider

/**
  * Created by Mitchell Skaggs on 10/5/2018.
  */
class BuildVersionVersionProvider extends IVersionProvider {
  override def getVersion: Array[String] = Array(BuildInfo.version)
}
