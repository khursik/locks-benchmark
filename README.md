# Утилита генерации нагрузки

Приложение предназначено для исследования производительности локов (TAS, TTAS, Backoff, CLH, MCS), в зависимости от количества потоков, которые работают с локом (от 1 до 32, логарифмическая шкала).

## Запуск приложения


Параметры запуска:

```shell
Usage: benchmark [options]

  -t, --type <value> type of lock (TAS, TTAS, Backoff, CLH, MCS)
  -thc, --threadsCount <value>
                           threads count
  -gt, --globalTimeoutMs <value>
                           global timeout milliseconds

```

