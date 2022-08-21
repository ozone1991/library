# Library System
This project contains some APIs for a library system. The system is capable of adding/removing the following entities:

* Members
* Authors
* Book Categories
* Books

The system allows members to loan and return books. As part of this task, there were a number of business rules/assumptions applied:

1. We are assuming members are already authenticated
2. Max number of books loaned per member is 3
3. If a member has any outstanding books loaned, they cannot loan any more until all are returned
4. There is only 1 copy of each book
5. Each book can have ore than one category

When a book is loaned, I have set a default "loan" period of 7 days. If the book is not returned in this time and a member then attempts to loan another book, their account will be locked until all books have been returned.

The application is built using Java 17 and Spring Boot. I used mapstruct for handling the mapper classes between entites and DTOs. Using mapstruct helped to reduce a lot of boiler plate code similar to using lombok. The application can be run using the LibraryApplication class. For development, I used a local Postgres instance. I set up a library DB in Postgres and also the libraryuser for this purpose. Connection details for the DB can be found in application.properties and can also be changed to run with other DBs instances here.

I have also created a Postman workspace which contains the various endpoints included within the application. When loaning or returning a book, the member id is passed as a header to the endpoint and the book id as a path variable. The Postman workspace can be found at:

https://www.postman.com/ozone1991/workspace/library
