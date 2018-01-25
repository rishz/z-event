# z-event
An Event Manager Web App

<a href="https://github.com/rishz/z-event/blob/master/LICENSE"><img src="https://img.shields.io/badge/License-MIT-red.svg" alt="license"/></a>

## Description
A web application developed primarily in Java using which a user can create events and
show his interest in the event. It aims at authenticating users and enabling them to create,
edit and delete events and giving the users the option to attend or pass on the event.

## Built With
* spark framework
* freemarker
* postgres
* css 
* bootstrap

## How to Run Locally

1. Open Terminal and enter
```
> psql postgres
```
2. Create User with Password and create the database
```
> CREATE USER eventmanager WITH PASSWORD 'testpass'
```
3. Connect to the database
```
> \c eventmanager
```
4. Create Table Users, Events and Event_Categories
```
> CREATE TABLE users(
> user_uuid UUID PRIMARY KEY NOT NULL,
> username CHAR(255) NOT NULL,
> email CHAR(255) NOT NULL,
> password CHAR(255) NOT NULL
> );
> CREATE TABLE events(
> event_uuid UUID PRIMARY KEY NOT NULL,
> name CHAR(255) NOT NULL,
> date DATE NOT NULL,
> description CHAR(255),
> going TEXT[],
> interested TEXT[],
> categories TEXT[]
> organizer TEXT NOT NULL
> );
> CREATE TABLE event_categories(
> event_uuid UUID PRIMARY KEY NOT NULL,
> category TEXT
> );
```
5. Grant the permission to the tables
```
GRANT ALL PRIVILIGES ON ALL TABLES IN SCHEMA public TO eventmanager;
```
6. Run
```
mvn compile && mvn exec:java -Dexec.args="--db-host localhost --db-port 5432"
```

## Contributing
==========
Any kind of contributions are welcome.

1. **Fork** the repo on GitHub.
2. **Clone** the project to your own machine.
3. **Commit** changes to **development** branch.
4. **Push** your work back up to your fork.
5. Submit a **Pull request** so that I can review your changes

## License

```Groovy
MIT License

Copyright (c) 2018 Rishabh Shukla

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
