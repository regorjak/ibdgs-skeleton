# Intelligent Bus Driver Guidance System (IBDGS)

Assignment 4 — Software Engineering Fundamentals (for IT) — RMIT University

## Overview

A Java backend for managing drivers and buses in a public-transport bus driver guidance system. Data is persisted to plain-text or JSON files; no database management system is used.

## Prerequisites

- Java 17 (Temurin or equivalent)
- Maven 3.6+

## Running tests locally

```
mvn clean test
```

Tests also run automatically on every push and pull request via GitHub Actions.

## Project structure

```
ibdgs/
├── .github/workflows/ci.yml          CI pipeline
├── data/                             TXT/JSON data files
├── src/main/java/com/ibdgs/          Source code
│   ├── Driver.java
│   ├── DriverRepository.java
│   ├── Bus.java
│   └── BusRepository.java
├── src/test/java/com/ibdgs/          Tests
│   ├── DriverUnitTest.java
│   ├── DriverIntegrationTest.java
│   ├── BusUnitTest.java
│   └── BusIntegrationTest.java
├── pom.xml
└── README.md
```

## Team

- Member 1 — Maven setup, GitHub Actions, user stories
- Member 2 — Driver implementation and tests
- Member 3 — Bus implementation and tests
- Member 4 — Report compilation, video, submission

Video demonstration push.
