package Amps

import com.crankuptheamps.client.{Client, Command, Message, MessageHandler}

object FigurationSubscriber {

  @throws[Exception]
  def main(args: Array[String]): Unit = {

    println("=" * 60)
    println("AMEER'S FIGURATION SERVICE")
    println("=" * 60)
    println("1. Connecting FigurationPublisher...")
    FigurationPublisher.connect()

    println("\n2. Connecting FigurationSubscriber...")
    val client = new Client("FigurationSubscriber")
    client.connect("tcp://192.168.20.122:9007/amps/json")
    client.logon()
    println("FigurationSubscriber connected!")

    val handler = new MessageHandler() {
      override def invoke(msg: Message): Unit = {
        println("\n" + "=" * 50)
        println("RECEIVED FROM SRIMANTH:")
        println("Topic: " + msg.getTopic)
        val data = msg.getData
        println("Data: " + data)
        println("-" * 50)

        println("Calling FigurationPublisher to process and send to Harish...")
        FigurationPublisher.publishFiguratedTrade(data)

        println("=" * 50 + "\n")
      }
    }

    println("\nSubscribing to 'validated' topic...")
    println("Will auto-call FigurationPublisher for each trade")
    println("Waiting for messages...\n")

    val cmd = new Command("subscribe").setTopic("validated")
    client.executeAsync(cmd, handler)

    Thread.sleep(300000)

    println("\nStopping...")
    client.close()
    FigurationPublisher.disconnect()
    println("Service stopped.")
  }
}