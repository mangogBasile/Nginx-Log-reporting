package utils

import java.sql.Timestamp
import java.text.SimpleDateFormat

import org.apache.spark.sql.Row

object Utilities {


  def getAllBlock(raw: Row): List[String] = {
    //val pattern = "(.*) (\\'.*\\') (\\d* \\d*) (\\'.*\\') (\\'.*\\') (\\'.*\\') (.*)".r
    val pattern = "(.*) (\".*\") (\\d* \\d*) (\".*\") (\".*\") (\".*\") (.*)".r

    raw.toString()match {
      case pattern(a, b, c, d, e, f, g) => List(a, b, c, d, e, f, g)
      case _ => throw new IllegalArgumentException(s"Impossible to retrieve All datablocks from the raw : $raw") //we throw an exception
    }
  }

  def getService(request: String) : String = {
    val pattern2 = "\"GET /service/(\\w*)/.*\"".r

    request match {
      case pattern2(service) => service
      case _ => throw new IllegalArgumentException(s"Impossible to retrieve service from : $request") //we throw an exception
    }
  }

  //on recupere le navigateur
  def getNavigateur(request: String): String = {

    val pattern3 = "\"(\\w*\\/\\d\\.\\d) .*\"".r

    request match {
      case pattern3(navigateur) => navigateur
      case _ => {
        val pattern4 = "\"(python-requests/2.18.1)\"".r
        request match {
          case pattern4(navigateur2) => navigateur2
          case _ => throw new IllegalArgumentException(s"Impossible to retrieve browser from: $request") //we throw an exception
        }
      }
    }
  }

  def getTimestamp(stringTimestamp: String): Timestamp = {
    val pattern5 = "\\d*\\/\\w*\\/\\d*:\\d*:\\d*:\\d* \\+\\d*".r
    val match1 = pattern5.findFirstIn(stringTimestamp)

    match1 match {
      case Some(timestamp) => {
        val originFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z", java.util.Locale.ENGLISH)
        new Timestamp(originFormat.parse(timestamp).getTime)
      }
      case None => throw new IllegalArgumentException(s"Impossible to parse $stringTimestamp to timestamp") //we throw an exception
    }
  }
}
