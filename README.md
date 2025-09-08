# Spring Boot Jackpot Application

This Spring Boot application manages jackpots and betting functionality, integrated with **Kafka** for messaging and an
**H2 in-memory database** for data persistence.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Project Setup](#project-setup)
    - [Kafka Setup](#kafka-setup)
    - [Database Setup](#database-setup)
    - [Running the Application](#running-the-application)
- [API Usage](#api-usage)
    - [Create a Contributing Bet](#create-a-contributing-bet)
    - [Check for Winning Bet](#check-for-winning-bet)
- [Stopping the Application](#stopping-the-application)
- [Notes](#notes)
- [AI Usage Disclaimer](#ai-usage-disclaimer)

## Prerequisites

- **Java**: JDK 21 or higher
- **Docker**: To run Kafka using Docker Compose
- **IntelliJ IDEA**: Recommended for running the Spring Boot application
- **cURL**: For testing API endpoints

## Project Setup

### Kafka Setup

The application uses Kafka for messaging, configured via a `docker-compose.yml` file.

- **Location**: `kafka/docker-compose.yml`
- **Data Storage**: Kafka data is stored in `kafka/kafka-data`
- **Port**: Kafka runs on port `9092`

To start Kafka:

```bash
docker-compose -f kafka/docker-compose.yml up
```

### Database Setup

The application uses an **H2 in-memory database**.

- **Console Access**: `http://localhost:8080/h2-console`
- **Credentials**:
    - **Username**: `sa`
    - **Password**: `password`
- **Initial Data**:
    - 2 jackpot templates inserted at startup
    - 2 jackpots inserted at startup
    - Uses both `RewardEvaluationTypes` and `ContributionTypes` for entities
    - You can find the start up inserts in `resources/data.sql`

### Running the Application

The application runs on port `8080`.

You can run it directly from IntelliJ(start up file `JackpotContributionApplication` )
Alternatively, you can run `./gradlew bootRun` in the terminal.

## API Usage

### Create a Contributing Bet

To create a new bet contributing to a jackpot:

```bash
curl --location 'http://localhost:8080/api/jackpot-bet' \
--header 'Content-Type: application/json' \
--data '{
  "betId": "b840f64b-f45e-46af-b5cd-43913e7f85f3",
  "userId": "45f06a03-0255-43e7-909d-01a02ae3cb34",
  "jackpotId": "11111111-1111-1111-1111-22222222",
  "betAmount": 50.0
}'
```

### Check for Winning Bet

To evaluate if a bet is a winning bet:

```bash
curl --location --request POST 'http://localhost:8080/api/jackpot-bet/evaluate-for-reward?jackpotContributionId={JackpotContributionId}'
```

**Note**: Replace `{JackpotContributionId}` with the actual JackpotContribution ID from the H2 database. Query it via
the H2 console (`http://localhost:8080/h2-console`).

## Stopping the Application

- **Stop Kafka**:
  ```bash
  docker-compose -f kafka/docker-compose.yml down
  ```
- **Stop Spring Boot**: Stop the application in IntelliJ or terminate the gradlew process.

## Notes

- Ensure **Docker** is running before starting Kafka.
- The H2 database is in-memory; data resets on application restart.
- Verify the `JackpotContributionId` in the H2 console before evaluating bets.

## AI Usage Disclaimer

AI was used throughout the application in a couple of places:

- Unit Test generation
- README.md file generation
- H2 setup
- a little help in some of the functions, like best practices for random number generation