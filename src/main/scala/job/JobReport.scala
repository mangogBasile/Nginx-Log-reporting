package job

import utils.SparkInstrument
import utils.Utilities._
import org.apache.spark.sql.{DataFrame, Dataset, Row}
import config.Settings
import model.Model._


object JobReport extends App with SparkInstrument {

  import spark.implicits._
  override  def jobName = "Reporting on the Nginx log"

  val nginxLog: Dataset[NginxLog] =  spark
                                      .read
                                      .text(Settings.config.pathNginxAccessLog)
                                      .filter(x => x.toString().contains("service"))
                                      .map(x => (getAllBlock(x), x.toString()) )
                                      .map(x => NginxLog(getTimestamp(x._1(0)), getService(x._1(1)), getNavigateur(x._1(4)),  x._2))
  nginxLog.show(10)
}
