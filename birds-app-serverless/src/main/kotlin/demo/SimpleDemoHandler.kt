package demo

import yandex.cloud.sdk.functions.Context
import yandex.cloud.sdk.functions.YcFunction


/*
Вызов функции
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
