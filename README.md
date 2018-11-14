# RssReader

Simple multithreading rss/atom tapes reader

Deploy: https://rss-reader-application.herokuapp.com

At the 6 November 2018, develop - is main and stable branch for developing and test.

REST Controllers (or go to: "/swagger-ui.html"):

/tags [GET] - get all tags (return: object {id:Long, name:String});

/tags/new [POST] - create new tag (params (requared): name:String);

/tags/{id} [PATCH] - update tag by id (param (requared): name - new name of tag)

/tags/{name} [DELETE] - delete tag by name;

/posts [GET] - sse controller, (params (not requared): name - string name of new tag, links - array of strings, where every element is url to rss/atom channel;

No params: get all posts;

Param: Array of strings, where strings are names of tags, example: "/posts?tag=business". Return posts by tags;

Param: Date from which need news, format: "HH:mm:ss dd-MM-yyyy", example: "/posts?from=12:59:59 01-11-2018". Return all posts by date;

Params: Array of strings, where strings are names of tags and from - date from which need news, 
format: "HH:mm:ss dd-MM-yyyy", example: "/posts?tag=business&from=12:59:59 01-11-2018". Return posts by tags and by date;

----------------------
Простое многопоточное приложение для чтения rss/atom лент.

Деплой: https://rss-reader-application.herokuapp.com

На период 6 ноября 2018, develop - ведущая и стабильная ветка для разработки и тестирования.

REST Контроллеры (или можно посмотреть в запущенном приложении по относительной ссылке: "/swagger-ui.html"):

/tags [GET] - получить все теги (return: object {id:Long, name:String});

/tags/new [POST] - создать новый тег (params (requared): name:String);

/tags/{id} [PATCH] - обновить тег по id (param (requared): name - new name of tag)

/tags/{name} [DELETE] - удалить тег по имени;

/posts [GET] - sse контроллер, (params (not requared): name - название нового тега, links - массив строк, где каждый элемент это ссылка на rss/atom канал;

No params: получить все посты;

Param: массив строк, где кажыдй элемент - название тега, пример запроса: "/posts?tag=business". Возвращает посты по тегам;

Param: Дата с которой требуются новости, формат: "HH:mm:ss dd-MM-yyyy", пример: "/posts?from=12:59:59 01-11-2018". Возвращает все посыт по дате;

Params: Массив строк, где каждый элемент это название тега и from - дата, с которой нужны новости, 
формат: "HH:mm:ss dd-MM-yyyy", пример: "/posts?tag=business&from=12:59:59 01-11-2018". Возвращает посты по тегам и по дате;
