# Nutrition Bot

Nutrition Bot is a Telegram-bot that recommends calorie-optimized meal plans based on user dietary goals. It is
developed using a microservices architecture, which allows for modular components that can be independently scalable and
updated.

In this architecture, each microservice is responsible for a specific functionality. For instance, one microservice
handles the bot's core logic, managing interactions with users and processing their requests. Another microservice is
dedicated to calculating calories based on user data, ensuring that meal plans align with the users' dietary goals. The
third microservice generates menus based on user preferences, creating personalized meal suggestions that fit within
their caloric targets and dietary restrictions.

To facilitate communication between the microservices, a message broker, Kafka, is utilized.

### Content

- [Tech Stack](#tech-stack)
- [Overview](#overview)
- [Services](#services)
- [Services Schema](#services-schema)
- [Telegram Bot States Schema](#telegram-bot-states-schema)

### Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Web
- PostgreSQL
- Kafka
- Maven

### Overview

[![Telegram Bot Overview](https://img.youtube.com/vi/oURoDTx5QqU/0.jpg)](https://www.youtube.com/watch?v=oURoDTx5QqU)

### Services

#### Dispatcher Service

The **Dispatcher Service** handles the core logic of the Telegram-bot. It manages interactions with users, processes
their
requests, and ensures smooth communication between the user and other microservices. When a user sends a request (e.g.,
asking for a meal plan or providing their dietary preferences), the Dispatcher forwards this information to the relevant
services, collects the responses, and sends the final recommendation back to the user.

#### Count Calories Service

The **Count Calories Service** is responsible for calculating the user's daily caloric intake based on their specific
data,
such as age, weight, height, activity level, and dietary goals (e.g., weight loss or maintenance). If a user does not
know how many calories they should consume, this service calculates the optimal daily calorie target that aligns with
their goals.

#### Meal Generator Service

The **Meal Generator Service** creates personalized meal plans based on the user's dietary preferences and caloric
needs. It
retrieves meals from the database and generates menus that fit within the user's calorie targets and adhere to any
dietary restrictions they may have (e.g., vegetarian, paleo, ketogenic). It ensures the balance of macronutrients and
variety in the suggested meals.

#### Parsing Service

The **Parsing Service** scrapes nutritional information and meal data from various websites. This service gathers details
such as meal descriptions, caloric values, and macronutrient composition. The extracted data is then sent
to the database, keeping the meal options updated and expanding the variety of available dishes for the meal plan
generator.

### Services Schema

![Services Schema](/img/services_schema.png)

### Telegram Bot States Schema

![TG Bot Schema](/img/TG_Bot_Schema.jpg)
