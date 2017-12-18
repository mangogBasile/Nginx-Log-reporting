package utils

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

trait SparkInstrument {

  def jobName: String

  protected val config: Config = ConfigFactory.load("spark")

  private val sparkConfig = {
    val self = new SparkConf().setAppName(jobName)

    val it=config.getConfig("spark").entrySet().iterator()

    while(it.hasNext){
      val next = it.next()
      self.set("spark." + next.getKey, next.getValue.unwrapped().toString)

    }
    self
  }

  protected def builder = SparkSession.builder.config(sparkConfig)

  /**
    * The SparkSession managed by this environment
    */
  lazy val spark: SparkSession = builder.getOrCreate()
}