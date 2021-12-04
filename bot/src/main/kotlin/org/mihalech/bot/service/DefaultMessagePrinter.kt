package org.mihalech.bot.service

import org.mihalech.bot.model.City
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class DefaultMessagePrinter : MessagePrinter {
  override fun print(city: City): SendMessage {
    return SendMessage().apply {
      text = city.info.entries
        .joinToString(separator = "\n") { entry -> "${entry.key}: ${entry.value}" }
    }
  }
}
