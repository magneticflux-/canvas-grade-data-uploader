package com.skaggsm.gradeupload.canvas

import java.util

import okhttp3.RequestBody
import retrofit2.http._

import scala.concurrent.Future

/**
  * Created by Mitchell Skaggs on 9/22/2018.
  */
trait CanvasService {
  @GET("/api/v1/courses/{courseId}/search_users")
  def searchForUser(@Header("Authorization") auth: String,
                    @Path("courseId") courseId: Int,
                    @Query("search_term") userLoginId: String): Future[Array[User]]

  @GET("/api/v1/courses/{courseId}/assignments/{assignmentId}/submissions/{userId}")
  def getSubmission(@Header("Authorization") auth: String,
                    @Path("courseId") courseId: Int,
                    @Path("assignmentId") assignmentId: Int,
                    @Path("userId") userId: Int,
                    @Query("include[]") includes: Array[String]): Future[Submission]

  @PUT("/api/v1/courses/{courseId}/assignments/{assignmentId}/submissions/{userId}")
  def setGrade(@Header("Authorization") auth: String,
               @Path("courseId") courseId: Int,
               @Path("assignmentId") assignmentId: Int,
               @Path("userId") userId: Int,
               @Query("submission[posted_grade]") postedGrade: String): Future[Submission]

  @FormUrlEncoded
  @POST("/api/v1/courses/{courseId}/assignments/{assignmentId}/submissions/{userId}/comments/files")
  def startFileUpload(@Header("Authorization") auth: String,
                      @Path("courseId") courseId: Int,
                      @Path("assignmentId") assignmentId: Int,
                      @Path("userId") userId: Int,
                      @Field("name") fileName: String): Future[FileUploadPendingState]

  @Multipart
  @POST
  def uploadFileToCanvas(@Url url: String,
                         @PartMap params: util.Map[String, RequestBody],
                         @Part("file") file: RequestBody): Future[FileUploadFinishedState]

  @PUT("/api/v1/courses/{courseId}/assignments/{assignmentId}/submissions/{userId}")
  def addComment(@Header("Authorization") auth: String,
                 @Path("courseId") courseId: Int,
                 @Path("assignmentId") assignmentId: Int,
                 @Path("userId") userId: Int,
                 @Query("comment[file_ids][]") fileIds: Array[Int]): Future[Submission]
}
