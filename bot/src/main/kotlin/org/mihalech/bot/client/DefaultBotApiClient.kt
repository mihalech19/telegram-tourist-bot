package org.mihalech.bot.client

import org.mihalech.bot.model.City
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

class DefaultBotApiClient(
  private val client: WebClient
) : BotApiClient {
  override fun findByName(name: String): Mono<City> {
    return client.get()
      .uri("/city/$name")
      .retrieve()
      .bodyToMono(City::class.java)
  }
}
