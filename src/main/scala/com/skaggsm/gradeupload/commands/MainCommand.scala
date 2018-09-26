package com.skaggsm.gradeupload.commands

import java.nio.file.{FileVisitOption, Files, Path}

import com.google.api.client.auth.oauth2.{AuthorizationCodeFlow, BearerToken, ClientParametersAuthentication}
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.inject.Inject
import com.skaggsm.gradeupload.canvas.CanvasService
import com.skaggsm.gradeupload.cli.{DurationTypeConverter, OptionTypeConverter}
import okhttp3.{MultipartBody, RequestBody}
import org.apache.logging.log4j.scala.Logging
import org.apache.pdfbox.pdmodel.PDDocument
import picocli.CommandLine.{Command, Parameters, Option => CommandOption}

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
class MainCommand @Inject()(val service: CanvasService) extends Runnable with Logging {

  @CommandOption(
    names = Array("--timeout"),
    arity = "0..1",
    required = false,
    converter = Array(classOf[DurationTypeConverter]),
    defaultValue = "10s"
  )
  var timeout: Duration = _

  @CommandOption(
    names = Array("-t", "--token"),
    arity = "0..1",
    required = false,
    converter = Array(classOf[OptionTypeConverter])
  )
  var providedOAuth2Token: Option[String] = None

  @CommandOption(
    names = Array("-a", "--assignment"),
    required = true,
    arity = "1"
  )
  var assignmentId: Int = _

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
        new ClientParametersAuthentication("ID", null /*"SECRET"*/),
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
    val tokenHeader = s"Bearer $oAuth2Token"

    logger.debug(s"Token: $oAuth2Token")

    val subdirectories = Files.walk(path, 1, FileVisitOption.FOLLOW_LINKS)
      .iterator().asScala.toSeq
      .drop(1)
      .filter(Files.isDirectory(_))

    for (dir <- subdirectories) {
      val studentName = dir.getName(dir.getNameCount - 1).toString.split("_").head

      logger.info(s"Searching for student id by login id '$studentName'...")

      val user = Await.result(service.searchForUser(tokenHeader, studentName), timeout).head

      logger.info(s"Found id: $user")

      logger.info(s"Loading PDF for $studentName")

      val pdfName = s"${studentName}_submit_submission.pdf"
      val pdfPath = dir.resolve(pdfName)

      val pdf = PDDocument.load(pdfPath.toFile)
      pdf.close()

      val annotations = pdf.getPages.asScala.flatMap(_.getAnnotations.asScala).toSeq

      val commentLines = annotations
        .filter(_.getContents != null)
        .flatMap(_.getContents.replace('\r', '\n').lines)
        .filter(_.nonEmpty)

      if (commentLines.isEmpty) {
        logger.warn("No PDF comments found, assuming ungraded.")
      }
      else {
        val pointModifiers = commentLines
          .flatMap(l => {
            Try(l.toDouble).toOption
          })
          .sum

        val grade = 100 + pointModifiers

        logger.info(s"Determined grade for $studentName is $grade/100")

        val submission = Await.result(service.getSubmission(tokenHeader, assignmentId, user.id, Array("submission_comments")), timeout)

        if (submission.submissionComments.nonEmpty) {
          logger.warn(s"Submission already has at least one Canvas comment, skipping to avoid duplicates.")
        }
        else {
          logger.info(s"Assigning grade of $grade to $studentName")

          val newSubmission = Await.result(service.setGrade(tokenHeader, assignmentId, user.id, grade.toString), timeout)

          logger.info(s"Grade is now ${newSubmission.score}")

          logger.info(s"Uploading PDF with comments for $studentName")

          val fileUploadPendingState = Await.result(service.startFileUpload(tokenHeader, assignmentId, user.id, pdfName), timeout)

          val fileUploadConfirmState = Await.result(
            service.uploadFileToCanvas(
              fileUploadPendingState.uploadUrl,
              fileUploadPendingState.uploadParams
                .asScala
                .mapValues(s => {
                  RequestBody.create(MultipartBody.FORM, s)
                })
                .asJava,
              RequestBody.create(MultipartBody.FORM, pdfPath.toFile)),
            timeout)

          val commentedSubmission = Await.result(service.addComment(tokenHeader, assignmentId, user.id, Array(fileUploadConfirmState.id)), timeout)

          logger.info(s"Submission now has ${commentedSubmission.submissionComments.length} Canvas comment(s)")
        }
      }
    }
  }
}