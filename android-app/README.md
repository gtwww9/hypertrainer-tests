# HyperTrainer Tests - Android App

Приложение для прохождения тестов по охране труда на основе данных из репозитория [hypertrainer-tests](https://github.com/gtwww9/hypertrainer-tests).

## Функционал

### 1. Выбор теста
- Загрузка списка тестов из `index.json` репозитория
- Отображение названия, описания, категории и сложности каждого теста

### 2. Экзамен
- 10 случайных вопросов из выбранного теста
- Допускается максимум 2 ошибки
- При превышении лимита ошибок экзамен завершается неудачей

### 3. Тренировка
- Все вопросы выбранного теста
- Без ограничений на количество ошибок
- Ошибки сохраняются для последующей работы

### 4. Работа над ошибками
- Вопросы, в которых были допущены ошибки
- Вопрос исчезает после правильного ответа
- Помогает закрепить сложные темы

### 5. Настройки
- Проверка обновлений
- Тёмная тема
- Очистка истории ошибок

### 6. Автообновление
- Сервис проверки обновлений на GitHub
- Уведомления о доступных обновлениях

## Структура проекта

```
android-app/
├── app/
│   ├── src/main/
│   │   ├── java/com/hypertrainer/tests/
│   │   │   ├── data/           # API, Repository, PreferencesManager
│   │   │   ├── model/          # Модели данных (Question, Test, etc.)
│   │   │   ├── ui/             # Activity, Adapter
│   │   │   ├── service/        # UpdateService для уведомлений
│   │   │   ├── util/           # Утилиты (NetworkUtils)
│   │   │   └── App.kt          # Application класс
│   │   ├── res/
│   │   │   ├── layout/         # XML макеты экранов
│   │   │   ├── values/         # Строки, цвета, темы
│   │   │   └── drawable/       # Графические ресурсы
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## Технологии

- **Kotlin** - основной язык разработки
- **AndroidX** - современные библиотеки Android
- **Material Components** - UI компоненты
- **Retrofit** - HTTP клиент для загрузки тестов
- **Gson** - парсинг JSON
- **Coroutines** - асинхронные операции
- **SharedPreferences** - хранение настроек и ошибок

## Сборка и запуск

1. Откройте проект в Android Studio
2. Дождитесь синхронизации Gradle
3. Запустите на эмуляторе или устройстве (Android 7.0+)

## URL источников данных

- Индекс тестов: `https://raw.githubusercontent.com/gtwww9/hypertrainer-tests/main/index.json`
- Файлы тестов: `https://raw.githubusercontent.com/gtwww9/hypertrainer-tests/main/<filename>.json`

## Разрешения

- `INTERNET` - загрузка тестов из репозитория
- `ACCESS_NETWORK_STATE` - проверка подключения к сети
- `POST_NOTIFICATIONS` - уведомления об обновлениях

## Лицензия

Проект создан для использования с тестами HyperTrainer.
