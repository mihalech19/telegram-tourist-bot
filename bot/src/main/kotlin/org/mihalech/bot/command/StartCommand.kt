package org.mihalech.bot.command

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

class StartCommand : Command {
  override fun execute(message: Message): SendMessage {
    return SendMessage().apply {
      text = "Привет. Я бот, который может дать короткую справку по городам мира.\n" +
          "Отправь мне название города и я постараюсь расказать всё что знаю"
    }
  }
}
