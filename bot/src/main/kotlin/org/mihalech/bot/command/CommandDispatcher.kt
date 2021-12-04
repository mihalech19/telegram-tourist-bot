package org.mihalech.bot.command

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import reactor.core.publisher.Mono

interface CommandDispatcher {

  fun dispatch(message: Message): Mono<SendMessage>
}
