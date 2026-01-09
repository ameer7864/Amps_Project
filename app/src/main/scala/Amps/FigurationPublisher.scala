package Amps

import com.crankuptheamps.client.Client
import play.api.libs.json._

object FigurationPublisher {

  private var ampsClient: Client = _

  def connect(): Unit = {
    ampsClient = new Client("FigurationPublisher")
    ampsClient.connect("tcp://192.168.20.60:9007/amps/json")
    ampsClient.logon()
    println("FigurationPublisher connected to AMPS")
  }

  def publishFiguratedTrade(data: String): Unit = {
    try {
      val trade = Json.parse(data).as[Trade]
      val tradeId = trade.trade_id
      val symbol = trade.symbol

      println(s"\nFIGURATING TRADE:")
      println(s"  Trade ID: $tradeId")
      println(s"  Symbol: $symbol")
      println(s"  Current Status: ${trade.status}")
      println(s"  Scheduler Status: STOPPED")  // ← Added this

      val isValid = trade.status == "VALIDATED" || trade.status.contains("VALID")

      if (!isValid) {
        println(s"  Skipping - Trade is not valid")
        val failedTrade = trade.copy(status = "FIGURATION_SKIPPED")
        val failedJson = Json.toJson(failedTrade).toString()
        ampsClient.publish("trades.figurated", failedJson)
        return
      }

      val figuratedTrade = trade.copy(status = "FIGURATED")

      println(s"  New Status: FIGURATED")
      println(s"  Forwarding to Settlement...")

      val figuratedData = Json.toJson(figuratedTrade).toString()

      ampsClient.publish("trades.figurated", figuratedData)

      println("  Published to Settlement Service!")

    } catch {
      case e: Exception =>
        println(s"Publishing failed: ${e.getMessage}")
        try {
          val trade = Json.parse(data).as[Trade]
          val errorTrade = trade.copy(status = "FIGURATION_FAILED")
          val errorJson = Json.toJson(errorTrade).toString()
          ampsClient.publish("trades.figurated", errorJson)
        } catch {
          case _: Exception =>
        }
    }
  }

  def disconnect(): Unit = {
    if (ampsClient != null) {
      ampsClient.close()
      println("FigurationPublisher disconnected")
    }
  }
}