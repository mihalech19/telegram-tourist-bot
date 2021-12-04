package org.mihalech.bot.client

import org.mihalech.bot.model.City
import reactor.core.publisher.Mono

interface BotApiClient {

  fun findByName(name: String): Mono<City>
}
