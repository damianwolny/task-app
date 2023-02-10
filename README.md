# CDQ Task

## Table of contents 
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Endpoints](#enpoints)

## General info
This project was developed as a solution to a business problem creaded by CDQ.

The main goal is to find the best match of pattern inside input string.

## Technologies
* JDK 17
* Spring Boot 3.0
* Google Guava
* H2

# Setup
To build and run this project on your local please use:

```
$ mvn clean install
$ mvn spring-boot:run
```
Default host: localhost

Default port: 8080

# Endpoints
* POST api/tasks?input=XXXXX&pattern=YYY 
* GET api/tasks/TASK_ID
