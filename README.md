# Название проекта 
birds app

## Запуск
1. change version in Configuration.kt
2. birds-app: gradle clean build
3. birds-app-api-base-v1: gradle pablishToMavenLocal
4. birds-app-ktor: gradle publishImageToLocalRegistry
5. change version in docker-compose.yml
6. docker compose up
7. docker compose -f docker-compose-keycloak.yml up --force-recreate keycloak postgreskey

## Логин пользователем
http://localhost:8081/auth/realms/otus-marketplace/account/#/


## Предназначение
социальная сеть обмена короткими сообщениями

## При каждом коммите в github
нужно инкрементить версию приложения в Configuration.kt     
val BIRDS_APP = "0.0.X"

## Цели модулей
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

