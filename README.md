[![Java CI with Maven](https://github.com/Olegsander48/job4j_grabber/actions/workflows/maven.yml/badge.svg)](https://github.com/Olegsander48/job4j_grabber/actions/workflows/maven.yml)

Проект "Агрегатор вакансий"

Система запускается по расписанию - раз в минуту.  Период запуска указывается в настройках - app.properties. 

Первый сайт будет career.habr.com. Работаем с разделом https://career.habr.com/vacancies/java_developer.  Программа должна считывать все вакансии c первых 5 страниц относящиеся к Java и записывать их в базу.

В проекте используется maven, checkstyle, jacoco, GitHub Actions.

Структура проекта:
1. Класс Post представляет модель данных, описывающую вакансию.
2. Класс Config предназначен для работы с конфигурацией приложения.
3. Интерфейс Store задает контракт для хранилища вакансий.
4. Класс MemStore реализует интерфейс Store и предоставляет хранилище в памяти.
5. Класс JdbcStore реализует интерфейс Store для хранения данных в базе PostgreSQL.
6. Интерфейс Parse описывает операции для парсинга данных с веб-сайтов.
7. Класс HabrCareerParse реализует интерфейс Parse. Используется для парсинга вакансий с платформы career.habr.com.
8. Класс SchedulerManager управляет задачами парсинга через библиотеку Quartz.
9. Класс SuperJobGrab реализует интерфейс Job (из Quartz) и отвечает за выполнение задачи парсинга.
10. Класс Main является точкой входа в приложение.

Архитектурные связи
* Post используется везде как основная модель данных.
* Config загружает параметры для работы приложений.
* Store, MemStore, и JdbcStore обеспечивают хранение данных.
* Parse и HabrCareerParse отвечают за извлечение вакансий.
* Grab и SchedulerManager управляют периодическим запуском.
* Main связывает все компоненты для работы приложения.

![image](https://github.com/user-attachments/assets/d82e82ab-6e04-4d49-b1e7-19f58647451a)
