package Amps

import com.crankuptheamps.client.{Client, Command, Message, MessageHandler}

object FigurationSubscriber {
  private var isProcessingTrade = false

  def main(args: Array[String]): Unit = {

    println("=" * 60)
    println("AMEER'S FIGURATION SERVICE WITH SMART SCHEDULER")
    println("=" * 60)

    println("This service:")
    println("1. Listens to AMPS for real-time trades")
    println("2. Scheduler STOPS during publishing")
    println("3. Does NOT touch database")
    println("=" * 60)

    println("\n1. Connecting FigurationPublisher...")
    FigurationPublisher.connect()

    println("\n2. Connecting FigurationSubscriber...")
    val client = new Client("FigurationSubscriber")
    client.connect("tcp://192.168.20.60:9007/amps/json")
    client.logon()
    println("FigurationSubscriber connected!")

    startSmartScheduler()

    subscribeToRealTime(client)

    Thread.sleep(Long.MaxValue)
  }

  def startSmartScheduler(): Unit = {
    new Thread(() => {
      var checkCount = 0
      while (true) {
        if (!isProcessingTrade) {
          checkCount += 1
          println(s"\n[SCHEDULER] Check #$checkCount at ${java.time.LocalDateTime.now()}")
          println("[SCHEDULER] No trades processing...")
          println("[SCHEDULER] Sleeping for 10 seconds...")
          Thread.sleep(10000)
        } else {
          Thread.sleep(100)
        }
      }
    }).start()

    println("\n3. Smart scheduler started!")
    println("Will STOP during trade processing")
    println("Resumes after publishing completes")
  }

  def subscribeToRealTime(client: Client): Unit = {
    val handler = new MessageHandler() {
      override def invoke(msg: Message): Unit = {
        isProcessingTrade = true

        println("\n" + "=" * 50)
        println("REAL-TIME TRADE RECEIVED FROM AMPS:")
        println("Topic: " + msg.getTopic)
        val data = msg.getData

        println("Processing trade...")
        println("SCHEDULER: STOPPED")
        println("-" * 50)

        try {
          println("Calling FigurationPublisher to process and send...")
          FigurationPublisher.publishFiguratedTrade(data)
        } finally {
          // RESTART the scheduler
          isProcessingTrade = false
          println("\nSCHEDULER: RESUMING")
        }

        println("=" * 50 + "\n")
      }
    }

    println("\n4. Subscribing to 'trades.validated' topic...")
    println("Scheduler will STOP during publishing")
    println("Waiting for messages...\n")

    val cmd = new Command("subscribe").setTopic("trades.validated")
    client.executeAsync(cmd, handler)
  }
}