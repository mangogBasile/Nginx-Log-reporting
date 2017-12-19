package job

import utils.SparkInstrument
import utils.Utilities._
import org.apache.spark.sql.{Dataset}
import config.Settings
import model.Model._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.expressions.Window


object JobReport extends App with SparkInstrument {

  import spark.implicits._
  override  def jobName = "Reporting on the Nginx log"

  val start = System.currentTimeMillis()

  val nginxLog: Dataset[NginxLog] =  spark
                                      .read
                                      .text(Settings.config.pathNginxAccessLog)
                                      .filter(_.toString().contains("GET /service")==true)
                                      .map(x => (getAllBlock(x), x.toString()) )
                                      .map(y => NginxLog(getTimestamp(y._1(0)), getService(y._1(1)), getNavigateur(y._1(4)),  y._2))


  nginxLog.cache()

  nginxLog.show(10)

  println("********************* Partie 1: Les 10 services les plus souvent sollicités ******************")
  nginxLog.groupBy('service).count().show(10)

  println("********************* Partie 2: Les 10 navigateurs les plus utilisés ******************")
  nginxLog.groupBy('navigateur).count().show(10)

  println("********************* Partie 3: Les 5 services les plus souvent accédés par type de navigateur ******************")
  nginxLog.select( 'navigateur,  count('service)
                                  .over(Window.partitionBy('navigateur)) as "NbrTotalDaccesAuService" )
            .orderBy('NbrTotalDaccesAuService)
            .show(10)

  println("********************* Partie 4: Les jours avec le max de connexions ordonnés ******************")
  nginxLog.groupBy(year('timestamp), dayofyear('timestamp)).count().show(5)


  val end = System.currentTimeMillis()
  println("total compute takes: " + (end-start))
  

}
