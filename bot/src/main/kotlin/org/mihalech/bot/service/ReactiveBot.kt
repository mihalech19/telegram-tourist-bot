package org.mihalech.bot.service

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import reactor.core.publisher.Flux

interface ReactiveBot {

  fun messageFlux(): Flux<Message>

  fun sendMessage(message: SendMessage): Message
}
