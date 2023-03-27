package demo

import yandex.cloud.sdk.functions.Context
import yandex.cloud.sdk.functions.YcFunction


/*

Задача:
Надо создать апи шлюз так чтобы вызывался метод POST а параметр передовался в теле
Решение:
Для начала изменить точку входа - demo.GatewayDemoHandler.
Затем чтобы вызывать функцию через API-gateway нужно чтобы
в коде она принимала на вход Request и
возвращала Response. Request и Response должны иметь определенную структуру.
Спецификация простейшего шлюза выглядит так. Нужно его создать в ЯО
openapi: 3.0.0
info:
  title: Sample API
  version: 1.0.0
paths:
  /:
      post:
        summary: Get ID
        operationId: getID
        tags:
          - example
        x-yc-apigateway-integration:
          type: cloud_functions
          function_id: d4eehpo48jmotbie4fdo
          tag: "$latest"
          service_account_id: ajekkrpv3tfcmva4sltv

Вызов:
curl --location --request POST 'https://d5dnfigrre09gkk5hjpb.apigw.yandexcloud.net' \
--header 'Content-Type: application/json' \
--data-raw 'anything'

*/

class GatewayDemoHandler : YcFunction<Request, Response> {

    override fun handle(request: Request, c: Context): Response {
        System.out.println("Function name: ${c.functionId}")
        System.out.println("Function version: ${c.functionVersion}")
        System.out.println("Service account token: ${c.tokenJson}")
        return Response(
            200,
            false,
            mapOf("Content-Type" to "application/json"),
            request.body!!
        )
    }
}
