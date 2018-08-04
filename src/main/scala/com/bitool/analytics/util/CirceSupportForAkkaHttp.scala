package com.bitool.analytics.util

import akka.http.scaladsl.marshalling.{Marshaller, ToEntityMarshaller}
import akka.http.scaladsl.model.{ContentTypes, MediaTypes}
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, Unmarshaller}
import akka.util.ByteString
import cats.syntax.either._
import io.circe.jawn._
import io.circe.syntax._
import io.circe.{Decoder, Encoder, Printer}

/**
  * When mixed in, gives capability to use Circe for automatically encoding/decoding Json using akka http
  * marshallers/unmarshallers
  */
trait CirceSupportForAkkaHttp {

  private final val compactPrinter: Printer = Printer(
    preserveOrder = false,
    dropNullKeys = true,
    indent = ""
  )

  final implicit def jsonUnmarshaller[T: Decoder]: FromEntityUnmarshaller[T] =
    Unmarshaller.byteArrayUnmarshaller
      .forContentTypes(ContentTypes.`application/json`)
      .mapWithCharset {
        case (bytes, charset) =>
          if (bytes.length == 0) throw Unmarshaller.NoContentException
          else decode(new String(bytes, charset.nioCharset.name)).valueOr(throw _)
      }

  final implicit def jsonMarshaller[T: Encoder](
      implicit printer: Printer = compactPrinter): ToEntityMarshaller[T] =
    Marshaller
      .byteStringMarshaller(MediaTypes.`application/json`)
      .compose(obj => ByteString(printer.prettyByteBuffer(obj.asJson)))

}
