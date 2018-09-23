package com.skaggsm.gradeupload

import java.nio.file.{FileVisitOption, Files, Path}

import com.google.api.client.auth.oauth2.{AuthorizationCodeFlow, BearerToken, ClientParametersAuthentication}
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import org.apache.pdfbox.pdmodel.PDDocument
import picocli.CommandLine.{Command, Parameters, Option => CommandOption}
import retrofit2.Retrofit
import retrofit2.adapter.scala.ScalaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import scala.collection.JavaConverters._
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Try

/**
  * Created by Mitchell Skaggs on 9/21/2018.
  */
@Command(
  description = Array("Prints directories in the provided path."),
  name = "canvas-grade-data-uploader",
  mixinStandardHelpOptions = true,
  version = Array("0.1.0"),
  showDefaultValues = true,
)
class HelloWorldCommand extends Runnable {

  @CommandOption(
    names = Array("-t", "--token"),
    arity = "0..1",
    converter = Array(classOf[OptionTypeConverter])
  )
  var providedOAuth2Token: Option[String] = None

  @Parameters(
    paramLabel = "PATH",
    arity = "0..1",
    defaultValue = ".",
  )
  var path: Path = _

  override def run(): Unit = {

    val oAuth2Token = providedOAuth2Token.getOrElse({
      //throw new UnsupportedOperationException("Idk how to make OAuth2 work on the command line yet...")
      val flow = new AuthorizationCodeFlow.Builder(
        BearerToken.authorizationHeaderAccessMethod,
        new NetHttpTransport(),
        JacksonFactory.getDefaultInstance,
        new GenericUrl("https://mst.instructure.com/login/oauth2/token"),
        new ClientParametersAuthentication("ID", "SECRET"),
        "ID",
        "https://mst.instructure.com/login/oauth2/auth"
      )
        //.setScopes(List("url:PUT|/api/v1/courses/:course_id/assignments/:assignment_id/submissions/:user_id").asJava)
        .build
      // authorize
      val receiver = new LocalServerReceiver.Builder().setHost("localhost").setPort(8080).build

      val cred = new AuthorizationCodeInstalledApp(flow, receiver).authorize(null)

      cred.getAccessToken
    })

    println(s"Token: $oAuth2Token")

    val subdirectories = Files.walk(path, 1, FileVisitOption.FOLLOW_LINKS)
      .iterator().asScala.toSeq
      .drop(1)
      .filter(Files.isDirectory(_))

    for (dir <- subdirectories) {
      val studentName = dir.getName(dir.getNameCount - 1).toString.split("_").head

      println(s"Searching for student id by login id '$studentName'...")

      // Inject Retrofit and OkHttp properly
      val service = new Retrofit.Builder()
        .baseUrl("https://mst.instructure.com")
        .addCallAdapterFactory(ScalaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(classOf[CanvasService])


      println("Found id: " + Await.result(service.searchForUser(s"Bearer $oAuth2Token", studentName), Duration("10s")))

      val pdfPath = dir.resolve(s"${studentName}_submit_submission.pdf")

      val pdf = PDDocument.load(pdfPath.toFile)
      pdf.close()

      val annotations = pdf.getPages.asScala.flatMap(_.getAnnotations.asScala).toSeq

      val pointModifiers = annotations
        .filter(_.getContents != null)
        .flatMap(_.getContents.replace('\r', '\n').lines)
        .flatMap(l => {
          Try(l.toDouble).toOption
        })
        .sum

      val grade = 100 + pointModifiers

      println(s"Determined grade for $studentName = $grade")
    }
  }
}