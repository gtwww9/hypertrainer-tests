# HyperTrainer Tests

Тесты по охране труда для приложения HyperTrainer

## Структура

```
tests-json/
├── Земляные работы.json
├── Машинист (оператор) крана, управляемого с пола.json
├── Рабочие люльки.json
├── Слесарь ПС ЭНЦ.json
├── Стропальщики.json
└── Электромонтеры.json
```

## Как добавить новый тест

1. Конвертируй .ask файл в JSON:
   ```bash
   node convert-to-json.js tests tests-json
   ```

2. Закоммить и запуш:
   ```bash
   git add tests-json/*.json
   git commit -m "Add new test: <name>"
   git push origin main
   ```

## Формат JSON

```json
{
  "name": "Название теста",
  "questions": [
    {
      "question": "Текст вопроса?",
      "options": [
        {"text": "Ответ 1", "correct": true},
        {"text": "Ответ 2", "correct": false}
      ]
    }
  ]
}
```

## URL для приложения

Тесты загружаются из:
```
https://raw.githubusercontent.com/gtwww9/hypertrainer-tests/main/<filename>.json
```

## Требования к вопросам

- Минимум 2 варианта ответа
- Хотя бы один правильный (correct: true)
- Вопрос должен быть понятным
