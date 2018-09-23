package com.skaggsm.gradeupload

import retrofit2.http._

import scala.concurrent.Future

/**
  * Created by Mitchell Skaggs on 9/22/2018.
  */
trait CanvasService {
  @GET("/api/v1/courses/29821/search_users")
  def searchForUser(@Header("Authorization") auth: String, @Query("search_term") userLoginId: String): Future[java.util.List[CanvasUser]]
}
