![GitHub Workflow Status](https://img.shields.io/github/workflow/status/Olegsander48/job4j_grabber/CI?label=build)

Проект "Агрегатор вакансий"

Система запускается по расписанию - раз в минуту.  Период запуска указывается в настройках - app.properties. 

Первый сайт будет career.habr.com. Работаем с разделом https://career.habr.com/vacancies/java_developer.  Программа должна считывать все вакансии c первых 5 страниц относящиеся к Java и записывать их в базу.

В проекте используется maven, checkstyle, jacoco, GitHub Actions
