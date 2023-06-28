# java-filmorate
Template repository for Filmorate project.

## Проект "Фильмография"
#### Проект умеет:
1. Добавлять пользователя, добавлять их в друзья и отслеживать их лайки
2. Добавлять фильмы, ставить им лайки и сортировать по популярности
### Диаграмма базы данных проекта:
https://dbdiagram.io/d/645580dcdca9fb07c4988478

![This is ER-diagramme](Untitled.png)

### Примеры запросов к базе данных:
Получение списка всех пользователей:

```
SELECT * 
FROM users;
```

Получение пользователей по ID:
``` 
 SELECT * 
 FROM users 
 WHERE user_id = ?; 
 ``` 
Получение списка друзей пользователя:
 ``` 
SELECT * 
FROM users 
WHERE user_id IN (SELECT friend_id FROM friendship WHERE user_id = ?);
 ``` 


Получение списка из наиболее популярных фильмов:
 ``` 
 SELECT id, name, description, release_date, duration, mpa_id 
 FROM films AS f
JOIN likes AS fl ON f.id = fl.film_id
GROUP BY f.id
ORDER BY COUNT(fl.user_id) DESC
LIMIT ? 
 ``` 


