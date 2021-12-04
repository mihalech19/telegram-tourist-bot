package org.mihalech.bot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class BotApp

fun main(args: Array<String>) {
  runApplication<BotApp>()
}
