package com.bitool.analytics.util

/**
  * To be used for generic REST responses in the 2xx range.
  */
case class SuccessResponse(status: Int, message: String)

/**
  * To be used for generic REST responses in the 4xx or 5xx range
  */
case class ErrorResponse(status: Int, errorMessage: String)
