package org.mihalech.bot.command

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

interface Command {

  fun execute(message: Message): SendMessage
}
