# Restful-Booker API Automation Framework

API test automation framework for the [Restful-Booker](https://restful-booker.herokuapp.com) demo API, built with **REST Assured**, **TestNG**, and **Allure Reports**.

## Tech Stack

| Tool | Version | Purpose |
|------|---------|---------|
| Java | 11 | Programming language |
| Maven | - | Build & dependency management |
| REST Assured | 5.4.0 | API request/response handling |
| TestNG | 7.9.0 | Test execution framework |
| Allure | 2.25.0 | Test reporting |
| Jackson | 2.16.1 | JSON serialization |

## Project Structure

```
src/test/java/api/
├── endpoints/
│   ├── Routes.java              # API endpoint constants
│   └── BookingEndpoints.java    # HTTP request methods (no assertions)
├── payload/
│   ├── Booking.java             # Request/response POJO
│   └── BookingDates.java        # Nested object POJO
├── tests/
│   ├── BaseTest.java            # Shared test setup
│   ├── PositiveTests.java       # TC01–TC08
│   └── NegativeTests.java       # NT01–NT09
└── utils/
    ├── ConfigManager.java       # Reads config.properties
    └── RestAssuredSetup.java    # Request/response spec configuration

src/test/resources/
├── config.properties            # Base URL, credentials, test data
└── testng.xml                   # TestNG suite definition
```

## Architecture

The framework follows a **layered architecture** for maintainability:

- **utils** — shared configuration and setup
- **payload** — data models (request/response bodies)
- **endpoints** — HTTP calls only, zero assertions
- **tests** — test logic and assertions

Each layer only depends on the layer below it, keeping responsibilities cleanly separated.

## Test Coverage

**17 test cases** covering authentication, booking CRUD operations, and health checks:

- **Positive scenarios (TC01–TC08):** health check, create/get/update/delete booking, partial update, verify deletion
- **Negative scenarios (NT01–NT09):** invalid credentials, missing fields, invalid date format, non-existent IDs, missing/invalid tokens, no-match filters

## Prerequisites

- Java 11 or higher
- Maven 3.6+

## Setup

```bash
git clone <your-repo-url>
cd restful-booker-improved
```

Update `src/test/resources/config.properties` if needed (base URL, credentials, test data).

## Running the Tests

Run the full suite:

```bash
mvn test
```

Run a specific test class:

```bash
mvn test -Dtest=PositiveTests
```

## Generating the Allure Report

Generate and open the report in one step:

```bash
mvn allure:serve
```

Or generate the static report separately:

```bash
mvn allure:report
```

The report will be available at `target/site/allure-maven-plugin/index.html`.

## Known Issues

- **NT04** — The Restful-Booker API does not perform server-side validation on date fields (`checkin`/`checkout`). Invalid date strings are silently accepted and stored as `"0NaN-aN-aN"`. This is documented as a known API limitation, not a framework defect.

## Author

**Amgad Atef**
Software Tester — ITI (Information Technology Institute)
