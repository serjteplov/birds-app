package ru.serj.biz.processor

import BirdsContext
import ru.serj.biz.chains.create
import ru.serj.biz.chains.delete
import ru.serj.biz.chains.filter
import ru.serj.biz.chains.search
import ru.serj.biz.repository.*
import ru.serj.biz.stubs.*
import ru.serj.biz.validation.*
import ru.serj.biz.workers.initWorker
import ru.serj.domain.repository.BirdsTweetRepository
import ru.serj.dsl.rootChain

class BirdsMainProcessor(
    private val repository: BirdsTweetRepository
) {

    suspend fun process(ctx: BirdsContext) {
        val rootChain = rootChain<BirdsContext> {
            initWorker("Инициализация запроса")
            create("Обработка запросов CREATE") {
                validation("Блок валидации входящего запроса") {
                    validateTweetSize("Твит не должен превышать 240 символов")
                    validateTweetContent("Твит не должен содержать политические лозунги")
                }
                stubs("Блок обработки стабов") {
                    stubSuccess("Стаб успешной отработки")
                    stubBadId("Стаб, эмулирующий ошибку некорректного Id")
                    stubBadDescription("Стаб, эмулирующий ошибку при проверке текста твита")
                    stubOtherCase("Стаб обработки, если не нашелся подходящий вариант")
                }
                repository("Блок работы с БД") {
                    saveTweetTemporary("Сохранение твита в базу данных", repository)
                }
            }
            filter("Обработка запросов filter") {
                validation("Блок валидации входящего запроса") {
                    validateInterval("Интервал не должен быть пустым")
                    validateDateTimeSequence("Проверка того что дата С меньше чем дата ПО")
                }
                stubs("Блок обработки стабов") {
                    stubSuccessFilter("Стаб успешной отработки")
                    stubNotFoundFilter("Стаб, возвращающий пустой список твитов")
                    stubOtherCase("Стаб обработки, если не нашелся подходящий вариант")
                }
                repository("Блок работы с базой данных") {
                    filterTweetByDate("Фильтр твитов по дате создания", repository)
                }
            }
            delete("Обработка запросов delete") {
                validation("Блок валидации входящего запроса") {
                    validateDeleteIdNotNull("Проверка что во входящем запросе Id не пустой")
                }
                stubs("Блок обработки стабов") {
                    stubSuccess("Стаб успешной отработки")
                    stubDeletionNotFound("Стаб, возвращающий ошибку Не найдено")
                    stubCannotDelete("Стаб, возвращающий ошибку Невозможно удалить твит")
                    stubOtherCase("Стаб обработки, если не нашелся подходящий вариант")
                }
                repository("Блок работы с базой данных") {
                    deleteTweetFromTemporary("Удаление из БД", repository)
                }
            }
            search("Обработка запросов search") {
                validation("Блок валидации входящего запроса") {
                    validateSearchStringNotEmpty("Проверка что поисковая строка не пустая")
                }
                stubs("Блок обработки стабов") {
                    stubSuccessFilter("Стаб успешной отработки")
                    stubBadSearchString("Стаб обработки ситуации невалидной поисковой строки")
                    stubOtherCase("Стаб обработки, если не нашелся подходящий вариант")
                }
                repository("Блок работы с базой данных") {
                    simpleTweetSearch("Простейший поиск по содержимому твита", repository)
                }
            }
        }.build()
        rootChain.exec(ctx)
    }
}
