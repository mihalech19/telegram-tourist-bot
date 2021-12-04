package org.mihalech.bot.service

import org.apache.logging.log4j.LogManager
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks

class DefaultReactiveBot(
  private val botUserName: String,
  private val botToken: String
) : TelegramLongPollingBot(), ReactiveBot {

  init {
    startReceiving()
  }

  private val messageSink = Sinks.many().unicast().onBackpressureBuffer<Message>()

  override fun onUpdateReceived(update: Update) {
    if (update.hasMessage()) {
      log.info(
        "An update ${update.updateId} was received from ChatId: ${update.message.chatId}"
      )
      messageSink.tryEmitNext(update.message)
    }
  }

  override fun messageFlux(): Flux<Message> = messageSink.asFlux()

  override fun sendMessage(message: SendMessage): Message = execute(message)

  private fun startReceiving() {
    val botsApi = TelegramBotsApi(DefaultBotSession::class.java)
    try {
      botsApi.registerBot(this)
    } catch (e: TelegramApiException) {
      log.error("Failed to start bot", e)
    }
  }

  override fun getBotToken(): String = botToken

  override fun getBotUsername(): String = botUserName

  private companion object {
    private val log = LogManager.getLogger()
  }
}
