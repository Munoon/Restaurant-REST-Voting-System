# Restaurant-REST-Voting-System

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/caab747fd11d4c0183f60aa7b45e7734)](https://app.codacy.com/app/Munoon/Restaurant-REST-Voting-System?utm_source=github.com&utm_medium=referral&utm_content=Munoon/Restaurant-REST-Voting-System&utm_campaign=Badge_Grade_Dashboard)

[DEVELOPING] REST service to vote for restaurants

This is a [JavaOPS](http://javaops.ru/reg/topjava) graduation project.


## Task
Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) **without frontend**.

The task is:

Build a voting system for deciding where to have lunch.

 * 2 types of users: admin and regular users
 * Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
 * Menu changes each day (admins do the updates)
 * Users can vote on which restaurant they want to have lunch at
 * Only one vote counted per user
 * If user votes again the same day:
    - If it is before 11:00 we asume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides new menu each day.
