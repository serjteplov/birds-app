package demo

import yandex.cloud.sdk.functions.Context
import yandex.cloud.sdk.functions.YcFunction


/*

Задача:
Надо создать апи шлюз так чтобы вызывался метод POST а параметр передовался в теле
Решение:
Чтобы эта функция работала через API-gateway нужно обязательно чтобы она принимала demo.Request и
возвращала demo.Response. demo.Request и demo.Response должны иметь определенную структуру.
Спецификация шлюза выглядит так:
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
