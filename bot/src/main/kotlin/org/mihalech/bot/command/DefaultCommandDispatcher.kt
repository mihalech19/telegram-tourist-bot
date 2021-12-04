package org.mihalech.bot.command

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import reactor.core.publisher.Mono

class DefaultCommandDispatcher(
  private val commands: Map<String, Command>
) : CommandDispatcher {
  override fun dispatch(message: Message): Mono<SendMessage> {
    return Mono.justOrEmpty(message.text)
      .filter { it.startsWith("/") }
      .flatMap { Mono.justOrEmpty(commands[message.text]) }
      .map { it!!.execute(message) }
  }
}
