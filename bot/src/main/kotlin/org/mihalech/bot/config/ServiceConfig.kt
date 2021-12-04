package org.mihalech.bot.config

import org.mihalech.bot.client.DefaultBotApiClient
import org.mihalech.bot.command.DefaultCommandDispatcher
import org.mihalech.bot.command.StartCommand
import org.mihalech.bot.service.DefaultMessagePrinter
import org.mihalech.bot.service.DefaultMessageProcessor
import org.mihalech.bot.service.DefaultReactiveBot
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
open class ServiceConfig {

  @Value("\${bot.api.url}")
  private lateinit var apiUrl: String

  @Bean
  open fun commandDispatcher() =
    DefaultCommandDispatcher(
      mapOf(
        "/start" to StartCommand()
      )
    )

  @Bean
  open fun reactiveBot() = DefaultReactiveBot(
    System.getenv("BOT_NAME"),
    System.getenv("BOT_TOKEN")
  )

  @Bean
  open fun messageProcessor() =
    DefaultMessageProcessor(
      DefaultBotApiClient(webClient()),
      reactiveBot(),
      DefaultMessagePrinter(),
      commandDispatcher()
    )

  @Bean
  open fun webClient(): WebClient = WebClient.create(apiUrl)
}
