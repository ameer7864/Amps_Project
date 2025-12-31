package Amps

import com.crankuptheamps.client.Client
import java.time.LocalDateTime

object FigurationPublisher {

  private var ampsClient: Client = _

  def connect(): Unit = {
    ampsClient = new Client("FigurationPublisher")
    ampsClient.connect("tcp://192.168.20.169:9007/amps/json")
    ampsClient.logon()
    println("FigurationPublisher connected to AMPS")
  }

  def publishFiguratedTrade(data: String): Unit = {
    try {
      val tradeId = extractTradeId(data)

      val figuratedData = doFiguration(data)

      println(s"\nPUBLISHING TRADE $tradeId TO HARISH...")
      ampsClient.publish("figurated", figuratedData)
      println("Published to Harish!")

    } catch {
      case e: Exception =>
        println(s"Publishing failed: ${e.getMessage}")
    }
  }

  private def doFiguration(data: String): String = {
    data.replace("\"VALIDATED\"", "\"FIGURATED\"") +
      s""","figuration_time":"${LocalDateTime.now()}","processed_by":"Ameer""""
  }

  private def extractTradeId(data: String): String = {
    val pattern = """"trade_id":(\d+)""".r
    pattern.findFirstMatchIn(data).map(_.group(1)).getOrElse("UNKNOWN")
  }

  def disconnect(): Unit = {
    if (ampsClient != null) {
      ampsClient.close()
      println("FigurationPublisher disconnected")
    }
  }
}