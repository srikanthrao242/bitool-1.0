package com.bitool.analytics.util

import java.time.Instant

import cats.syntax.either._
import io.circe.{Decoder, Encoder, HCursor}

/**
  * Circe encoders/decoders for commonly used types
  *
  * @author kcpaul
  */
object CirceUtil {

 /* /**
    * Circe decoder for java.time.Instant from ISO-8601 string representation
    */
  val decodeInstant: Decoder[Instant] = Decoder.decodeString.emap { str =>
    Either.catchNonFatal(Instant.parse(str)).leftMap(t => "Instant")
  }

  /**
    * Circe encoder for java.time.Instant to ISO-8601 string representation
    */
  val encodeInstant: Encoder[Instant] = Encoder.encodeString.contramap[Instant](_.toString)

  /**
    * Circe decoder for java.lang.Boolean from decoded scala.Boolean
    */
  val decodeJavaBoolean: Decoder[java.lang.Boolean] =
    (hCursor: HCursor) => Decoder.decodeBoolean.map(b => new java.lang.Boolean(b)).apply(hCursor)

  /**
    * Circe encoder for java.lang.Boolean from encoded scala.Boolean
    */
  val encodeJavaBoolean: Encoder[java.lang.Boolean] = (a: java.lang.Boolean) =>
    Encoder.encodeBoolean.apply(a)

  /**
    * Circe decoder for scala.Double from string representation like "Infinity", "-Infinity" & "NaN"
    */
  val customDoubleDecoder: Decoder[Double] = Decoder.decodeDouble.or(
    Decoder.decodeString.emap {
      case "Infinity" => Right(Double.PositiveInfinity)
      case "-Infinity" => Right(Double.NegativeInfinity)
      case "NaN" => Right(Double.NaN)
      case _ => Left("Double")
    }
  )*/

}
