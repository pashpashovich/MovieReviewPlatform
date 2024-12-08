# Задание
Сервис по оценке фильмов и написанию рецензий 
## Содержание
- [Технологии](#технологии)
- [Предполагаемый функционал](#предполагаемый-функционал)
- [План выполнения](#план-выполнения)

## Технологии
- Java 21
- Servlets
- JSP 
- PostgreSQL
- Hibernate

## Предполагаемый функционал

# Функционал пользователя:
1. Поиск/фильтрация фильмов по названию, жанру, году выпуска, главным актерам.
2. Просмотр информации о фильме (название, постер, описание, год выпуска, жанр, продолжительность, язык оригинала, рецензии к нему, главные актеры, режиссеры, сценаристы, продюсеры). 
3. Просмотр рекомендаций (рекомендации будут формироваться исходя из жанров фильмов, которые пользователь оценивал наиболее высоко).
4. Добавление/удаление из списка "Хочу посмотреть". 
5. Возможность выставить/изменить рейтинг фильму (по шкале от 1 до 10). 
6. Написание и модерация рецензий (могут оставлять, редактировать, удалять рецензии на фильмы).
7. Авторизация, регистрация.
8. Просмотр своего профиля (общая информация, список оцененных фильмов, оставленных рецензий).

### Опционально:
1. Возможность создавать, редактировать, удалять комментарии под рецензиями. 
2. Просмотр карточек фильмов по блокам ("Сейчас в прокате", "Новинки", "Новые рецензии добавлены").

# Функционал администратора:
1. Управление пользователями:
   - Просмотр списка пользователей.
   - Блокировка/разблокировка пользователей за нарушение правил.
   - Изменение роли на админ.
2. История активности пользователя:
   - Отображение списка фильмов, которые пользователь оценил.
   - Список рецензий, оставленных пользователем.
3. Работа с фильмами:
   - Создание, редактирование, удаление добавленных в систему фильмов.
4. Авторизация

### Опционально:
1. Просмотр статистики (количество фильмов, пользователей в системе, количество оставленных рецензий, количество активных пользователей за определенный период, топ фильмов, жанров по рейтингу).
2. Возможность добавлять фильмы в систему, используя Кинопоиск API.

## План выполнения
Сроки выполнения: 28.11-13.12

| Фича | Номер функционала                                           | Описание                                                                                                                                                                                                                                                           | Срок выполнения | Готовность |
|:-----|:------------------------------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------------:|:----------:|
| -    | -                                                           | Проектирование БД, создание сущностей, реализация CRUD операций со сущностями                                                                                                                                                                                      |   01.12.2024    | Выполнено  |
| 1    | Администратор 3                                             | Верстка страницы, где в виде таблицы будут отображены фильмы, добавленные в систему, с возможностью поиска и фильтрации, удаления из системы конкретного фильма, а также с формой для добавления, редактирования фильма. Написание бэкэнд части к данной странице. |   03.12.2024    | Выполнено  |
| 2    | Пользователь 1                                              | Верстка страницы со строкой поиска и полей для фильтрации по добавленным в систему фильмам. Написание бэкэнд части к данной странице.                                                                                                                              |   04.12.2024    | Выполнено  |
| 3    | Пользователь 2                                              | Верстка страницы с информацией о конкретном фильме. Написание бэкэнд части к данной странице.                                                                                                                                                                      |   06.12.2024    | Выполнено  |
| 4    | Пользователь 3                                              | Добавление рекомендаций на страницу поиска. Написание бэкэнд части к данной странице.                                                                                                                                                                              |   07.12.2024    | Выполнено  |
| 5    | Пользователь 4                                              | Верстка страницы с фильмами, добавленными в "Хочу посмотреть". Добавление кнопки "Хочу посмотреть" на страницу конкретного фильма. Написание бэкэнд части к данной странице.                                                                                       |   07.12.2024    | Выполнено  |
| 6    | Пользователь 6                                              | Добавление рецензий на страницу конкретного фильма. Написание бэкэнд части.                                                                                                                                                                                        |   08.12.2024    | Выполнено  |
| 7    | Пользователь 5                                              | Добавление рейтинга на страницу конкретного фильма, а также общую страницу поиска. Написание бэкэнд части.                                                                                                                                                         |   08.12.2024    | Выполнено  |
| 8    | Пользователь 7, администратор 4                             | Верстка страниц с формами для авторизации, регистрации. Написание бэкэнд части к ней.                                                                                                                                                                              |   09.12.2024    | Выполнено  |
| 9    | Администратор 1                                             | Верстка страницы с таблицей для управления пользователями. Написание бэкэнд части к ней.                                                                                                                                                                           |   11.12.2024    |     -      |
| 10   | Администратор 2, пользователь 8                             | Верстка страницы с информацией о конкретном пользователе. Написание бэкэнд части к ней.                                                                                                                                                                            |   12.12.2024    |     -      |
| 11   | Администратор 1-2 опционально, пользователь 1-2 опционально | Добавление опциональной функциональности по возможности. Тестирование приложения, деплой, написание руководства по развертыванию и использованию ПС                                                                                                                |   13.12.2024    |     -      |