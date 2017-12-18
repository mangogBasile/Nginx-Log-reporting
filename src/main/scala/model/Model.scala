package model
import java.sql.Timestamp

object Model {

  case class NginxLog(timestamp:java.sql.Timestamp, service:String, navigateur:String, raw:String)
}

