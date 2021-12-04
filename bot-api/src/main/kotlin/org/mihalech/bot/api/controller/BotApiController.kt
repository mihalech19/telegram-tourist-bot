package org.mihalech.bot.api.controller

import com.mongodb.client.result.DeleteResult
import org.mihalech.bot.model.City
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.FindAndReplaceOptions
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.TextCriteria
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
class BotApiController(
  @Autowired private val mongo: ReactiveMongoTemplate
) {

  @GetMapping("/city/{cityName}")
  fun findCityByName(
    @PathVariable("cityName") cityName: String
  ): Mono<City> =
    mongo.findOne(
      Query.query(
        TextCriteria
          .forDefaultLanguage()
          .caseSensitive(false)
          .matching(cityName)
      ),
      City::class.java,
      CITY_COLLECTION_NAME
    )

  @DeleteMapping("/city/{cityName}")
  fun deleteCity(@PathVariable("cityName") cityName: String): Mono<DeleteResult?>? {
    return mongo.remove(
      Query.query(
        TextCriteria
          .forDefaultLanguage()
          .caseSensitive(false)
          .matching(cityName)
      ),
      CITY_COLLECTION_NAME
    )
  }

  @PutMapping(value = ["/city/{cityName}"], consumes = ["application/json"])
  fun updateCity(
    @RequestBody updatedCity: City,
    @PathVariable(value = "cityName") cityName: String
  ): Mono<City> {
    return mongo.findAndReplace(
      Query.query(
        TextCriteria
          .forDefaultLanguage()
          .caseSensitive(false)
          .matching(cityName)
      ),
      updatedCity,
      FindAndReplaceOptions.options().returnNew(),
      City::class.java, CITY_COLLECTION_NAME
    )
  }

  @PostMapping(value = ["/city"], consumes = ["application/json"])
  fun addCity(@RequestBody city: City): Mono<City> =
    mongo.insert(city, CITY_COLLECTION_NAME)

  @GetMapping("/city")
  fun allCities(): Flux<City> =
    mongo.findAll(City::class.java, CITY_COLLECTION_NAME)

  private companion object {
    private const val CITY_COLLECTION_NAME = "city"
  }
}
