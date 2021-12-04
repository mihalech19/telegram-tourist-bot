package org.mihalech.bot.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class BotApiApp

fun main(args: Array<String>) {
  runApplication<BotApiApp>()
}
