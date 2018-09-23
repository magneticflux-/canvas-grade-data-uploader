package com.skaggsm.gradeupload.canvas

import retrofit2.http.{GET, Header, Path, Query}

import scala.concurrent.Future

/**
  * Created by Mitchell Skaggs on 9/22/2018.
  */
trait CanvasService {
  @GET("/api/v1/courses/29821/search_users")
  def searchForUser(@Header("Authorization") auth: String, @Query("search_term") userLoginId: String): Future[Array[User]]

  @GET("/api/v1/courses/29821/assignments/{assignmentId}/submissions/{userId}")
  def getSubmission(@Header("Authorization") auth: String, @Path("assignmentId") assignmentId: Int, @Path("userId") userId: Int, @Query("include[]") includes: Array[String]): Future[Submission]
}
