package Amps

import com.crankuptheamps.client.Client
import play.api.libs.json._

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
      val trade = Json.parse(data).as[Trade]
      val tradeId = trade.trade_id

      val updatedTrade = trade.copy(status = "FIGURATED")

      val figuratedData = Json.toJson(updatedTrade).toString()

      println(s"\nPUBLISHING TRADE $tradeId TO HARISH...")
      ampsClient.publish("figurated", figuratedData)
      println("Published to Harish!")

    } catch {
      case e: Exception =>
        println(s"Publishing failed: ${e.getMessage}")
    }
  }

  def disconnect(): Unit = {
    if (ampsClient != null) {
      ampsClient.close()
      println("FigurationPublisher disconnected")
    }
  }
}