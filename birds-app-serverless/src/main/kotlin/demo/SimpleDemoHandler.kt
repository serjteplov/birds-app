package demo

import yandex.cloud.sdk.functions.Context
import yandex.cloud.sdk.functions.YcFunction


/*
Создание функции в яндекс cloud:
1. Создаем shadow jar
2. Открываем ЯО, создать функцию, способ Object Storage
3. Бакет - test-bucket-teplov-1 (бакет который вы создали)
4. Объект - birds-app-serverless-0.0.3-all.jar (shadow джарник который вы собрали и залили в бакет)
5. Точка входа - demo.SimpleDemoHandler
6. Жмем Создать версию


Вызов функции:
curl --location --request POST 'https://functions.yandexcloud.net/d4eehpo48jmotbie4fdo?integration=raw' \
--header 'Content-Type: application/json' \
--data-raw '1556'
*/

class SimpleDemoHandler : YcFunction<Int, Boolean> {

    override fun handle(number: Int, c: Context): Boolean {
        System.out.println("Number: ${number}")
        System.out.println("Function name: ${c.functionId}")
        System.out.println("Function version: ${c.functionVersion}")
        System.out.println("Service account token: ${c.tokenJson}")
        return (number % 2) == 0
    }
}
