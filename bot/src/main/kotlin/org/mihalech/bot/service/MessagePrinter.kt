package org.mihalech.bot.service

import org.mihalech.bot.model.City
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

interface MessagePrinter {

  fun print(city: City): SendMessage
}
