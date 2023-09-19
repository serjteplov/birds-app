## Для начала
Авторизовать github в Intellij
Должен локально проходить gradle clean build

## Запуск через докер
1. (if needed) change version in Configuration.kt
2. (if needed) change version in deploy/docker-compose.yml
3. (if needed) birds-app-api-base-v1: gradle publish (with envs GITHUB_ACTOR=serjteplov GITHUB_TOKEN=сгенерить)
4. (if needed) birds-app: gradle clean build (with envs GITHUB_ACTOR=serjteplov GITHUB_TOKEN=сгенерить)
5. (if needed) birds-app-api-base-v1: gradle publishToMavenLocal
6. (if needed) birds-app-ktor: gradle publishImageToLocalRegistry
7. cd deploy/ && docker compose up
8. Получить токен в keycloak изнутри сети docker compose (т.к. именно в этом случае в токене будет проставлен
корректный issuer, который на самом деле есть адрес keykloak)
   curl --location --request POST 'http://keycloak:8080/auth/realms/otus-marketplace/protocol/openid-connect/token' \
   --header 'Content-Type: application/x-www-form-urlencoded' \
   --data-urlencode 'grant_type=password' \
   --data-urlencode 'username=serg' \
   --data-urlencode 'password=1' \
   --data-urlencode 'client_id=otus-marketplace-service'
9. Сделать запрос в приложение (см. коллекцию постман)
10. Открыть opensearch http://localhost:5601 admin/admin
11. Добавить index pattern: app-logs-*
12. Открыть Discover

## Локальный запуск
1. Добавить в переменные запуска KEYCLOAK_BASE_URL=http://localhost:8081
2. Создать БД tweets
3. Запустить keycloak: cd deploy/ && docker compose up
4. Запустить приложение birds-app-ktor кнопкой в Intellij
5. Получить токен в keycloak через постман
6. Сделать запрос в приложение

## Получить токен keycloak
1. При первом запуске приложения нужно создать пользователя serg в интерфейсе Keycloak
2. http://localhost:8081/
3. Administrative console -> admin\admin
4. Manage -> Groups -> New -> USERS
5. Manage -> Users -> Add User -> serg -> set password -> 1
6. Users -> serg -> Groups -> join USERS -> leave USER
7. Возможно -> Users -> serg -> Role Mappings -> Client Roles -> otus-marketplace-service
8. http://localhost:8081/auth/realms/otus-marketplace/account/#/
9. Login with serg\1
10. Сделать запрос в postman ответом на который будет access_token (см. коллекцию постман)

## Отдельный запуск keycloak (в рамках проекта не требуется)
docker compose -f docker-compose-keycloak.yml up --force-recreate keycloak postgreskey

## Github Actions настроены и работают
GITHUB_TOKEN генерится и добавляется в github actions автоматически
Из скриптов github actions он доступен через ${{ secrets.GITHUB_TOKEN }}
Однако надо не забывать прокидывать его в энвы строчкой env: GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}

## Описание модулей
birds-app-api-base-v1 - модуль с куском OpenApi спеки, общей для всех остальных спек
birds-app-api-v1 - модуль со спекой OpenApi, которая является контрактом взаимодействия
с внешним миром. Также модуль содержит транспортные модели, сгенирированные по этой спеке
birds-app-biz - модуль с бизнес логикой на паттерне chain of responsibility
birds-app-common - модуль с внутренними моделями, которые используются внутри приложения. Эти
модели заполняются на основе транспортных моделей, поступающих извне приложения
birds-app-mappers - модуль мапперов транспортных моделей во внутренние
birds-app-kafka - модуль транспорта на Kafka
birds-app-ktor - модуль транспорта на Ktor
birds-app-serverless - модуль транспорта на Serverless
birds-app-lib-cor - модуль (библиотека) реализующая chain of responsibility паттерн

