package org.mihalech.bot.service

import org.apache.logging.log4j.LogManager
import org.mihalech.bot.client.BotApiClient
import org.mihalech.bot.command.CommandDispatcher
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import reactor.core.Disposable
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers

class DefaultMessageProcessor(
  private val client: BotApiClient,
  private val bot: ReactiveBot,
  private val messagePrinter: MessagePrinter,
  private val commandDispatcher: CommandDispatcher
) : MessageProcessor {

  private val defaultMessage = SendMessage().apply {
    text = "Не удалось найти такой город :("
  }

  private var messageSubscribe: Disposable? = null

  @EventListener(ApplicationReadyEvent::class)
  override fun start() {
    bot.messageFlux()
      .filter { message -> message.hasText() }
      .flatMap { message ->
        commandDispatcher.dispatch(message)
          .switchIfEmpty(findCity(message.text))
          .map {
            bot.sendMessage(
              it.apply {
                chatId = message.chatId.toString()
              }
            )
          }
          .doOnNext { log.info("Answer sent to ChatId : ${it.chatId}") }
          .onErrorContinue { e, _ ->
            log.error("An error occurred while processing the message:  \"$message\"", e)
          }
      }
      .doOnError { e ->
        log.error(e)
      }
      .doOnTerminate { log.info("Message processor was terminated") }
      .subscribeOn(Schedulers.boundedElastic())
      .start()
  }

  override fun stop() {
    messageSubscribe?.dispose() ?: throw IllegalStateException("There was no subscription to the processor")
  }

  private fun findCity(cityName: String) =
    client.findByName(cityName)
      .map { city -> messagePrinter.print(city) }
      .defaultIfEmpty(defaultMessage)

  private fun Flux<*>.start() = this.subscribe().also { messageSubscribe = it }

  private companion object {
    private val log = LogManager.getLogger()
  }
}
