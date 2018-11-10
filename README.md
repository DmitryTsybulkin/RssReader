# RssReader
Simple multithreading rss/atom tapes reader

Deploy: https://rss-reader-application.herokuapp.com/posts

At the 6 November 2018, develop - is main and stable branch (

REST Controllers:

/tags [GET] - get all tags (return: object {id:Long, name:String});

/tags/new [POST] - create new tag (params (requared): 

/posts [GET] - sse controller, (params (not requared): name - string name of new tag, links - array of strings, where every element is url to rss/atom channel;

No params: get all posts;

Param: Array of strings, where strings are names of tags, example: "/posts?tag=business". Return posts by tags;

Param: Date from which need news, format: "HH:mm:ss dd-MM-yyyy", example: "/posts?from=12:59:59 01-11-2018". Return all posts by date;

Params: Array of strings, where strings are names of tags and from - date from which need news, 
format: "HH:mm:ss dd-MM-yyyy", example: "/posts?tag=business&from=12:59:59 01-11-2018". Return posts by tags and by date;

/posts/{id} [PATCH] - update tag by id (param (requared): name - new name of tag)

/posts/{name} [DELETE] - delete tag by name;
