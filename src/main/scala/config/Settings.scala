package config

import com.typesafe.config.{Config, ConfigFactory}
import configs.{ConfigError, Configs}
import org.slf4j.{Logger, LoggerFactory}

final case class servConf(pathNginxAccessLog: String,parquetPath:String)

object Settings {

  val log: Logger = LoggerFactory.getLogger(getClass)

  val conf: Config = ConfigFactory.load() //Ici on charge les configurations contenues par défaut dans le fichier reference.conf

  val config = Configs[servConf].get(conf, "serv").valueOrThrow(configErrorsToException) //ici on recupere la configuration qui nous intéresse

  def configErrorsToException(err: ConfigError) =
    new IllegalStateException(err.entries.map(_.messageWithPath).mkString(","))
}
