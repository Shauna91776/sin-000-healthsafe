# HealthSafe

## Overview

HealthSafe is a small distributed hospital ward management system that handles
ward data, emergency staffing, and critical equipment failure alerts.

Domain entities include wards, wings, and specialist departments.

The project is built as a set of independent Java services and demonstrates a
progression from data cleaning, to synchronous REST communication, to
asynchronous messaging with ActiveMQ:

1. **Stage 1 — Data ingestion:** clean and normalize a messy legacy CSV export.
2. **Stage 2 — REST services:** expose the cleaned ward data and coordinate
   staffing using synchronous HTTP requests.
3. **Stage 3 — Messaging:** publish staffing events through an ActiveMQ topic
   so interested services can receive updates asynchronously.
4. **Stage 4 — Alerting:** publish critical equipment failures to an ActiveMQ
   queue for reliable processing by the equipment alert service.

Every class in this repo lives in the single flat package
`co.wethinkcode.healthsafe`.

The project uses Java 17, Maven, Javalin, Jackson, and ActiveMQ Classic.

| Service | Folder | Port | Role |
|---|---|---:|---|
| IngestionServiceApp | [`ingestion-service/`](ingestion-service) | 7030 | Parses and cleans `wards-outdated.csv` |
| WardServiceApp | [`ward-service/`](ward-service) | 7031 | Provides lists of wards and departments |
| AlertLevelServiceApp | [`alert-level-service/`](alert-level-service) | 7032 | Tracks the hospital Emergency Status (0–8, with 8 representing Code Blue) |
| StaffingServiceApp | [`staffing-service/`](staffing-service) | 7033 | Provides on-call schedules for doctors based on ward and status |
| EquipmentAlertServiceApp | [`equipment-alert-service/`](equipment-alert-service) | 7034 | Consumes critical medical equipment failure alerts from an ActiveMQ queue |

The `common` module contains the shared ActiveMQ broker configuration.
Staffing updates are published as events so that interested services can
receive them without the publisher needing to know about individual consumers.

**Status:** Stages 1–4 are implemented, including CSV ingestion and
normalization, REST service integration, ActiveMQ topic messaging, and
equipment failure queue processing.

## Implementation

The stages below describe the progression used to implement HealthSafe.
Stages 1–2 form the required core, while stages 3–4 demonstrate the use of
asynchronous messaging for service decoupling and reliable alert processing.

### Stage 1 — Ingestion

Implemented in `IngestionServiceApp`:

- Reads and cleans `wards-outdated.csv`.
- Normalizes casing and whitespace.
- Handles duplicate ward IDs.
- Normalizes placeholders and invalid numeric values.
- Exposes cleaned ward records through REST for `ward-service` to consume.

### Stage 2 — REST services

Implemented across `ward-service`, `alert-level-service`, and
`staffing-service`:

- `ward-service` provides ward and department information sourced from
  `ingestion-service`.
- `alert-level-service` tracks Emergency Status values from 0–8.
- `staffing-service` uses synchronous HTTP calls to `ward-service` and
  `alert-level-service` to calculate on-call staffing.
- Services return appropriate HTTP responses for conditions such as unknown
  wards, invalid alert levels, and unavailable downstream services.

### Stage 3 — MQ decoupling (stretch)

Staffing-service publishes staffing events to the ActiveMQ topic
`staffing-events-topic`.

Ward-service subscribes to the topic and processes the staffing updates
asynchronously.

The topic provides a broadcast-style communication channel, allowing
interested services to receive staffing events without the publisher needing
to know which services are consuming them.

The Stage 3 messaging path is implemented alongside the Stage 2 synchronous
REST flow. The Stage 2 HTTP integration remains in place, while the ActiveMQ
topic demonstrates asynchronous event delivery and service decoupling.

### Stage 4 — Equipment failure alerting (stretch)

Ward-service publishes equipment failure events to the ActiveMQ queue
`equipment-failure-queue`.

Equipment-alert-service consumes these messages and processes the failure
alerts.

Equipment failure messages use persistent delivery, and the consumer uses
manual acknowledgement. Messages are acknowledged only after successful
processing.

A queue is appropriate for equipment failures because each alert represents
work that should be handled by a consumer rather than broadcast to every
subscriber.

## Integration contracts

Endpoint shapes below describe the implemented service interactions. Reasonable
field names and HTTP status codes are used where appropriate.

| From | To | Call | Purpose |
|---|---|---|---|
| `ward-service` | `ingestion-service` | `GET /wards` | Retrieve cleaned ward records |
| `staffing-service` | `ward-service` | `GET /wards/{id}` | Validate the ward before scheduling |
| `staffing-service` | `alert-level-service` | `GET /alert-level` | Read the current Emergency Status |
| `staffing-service` | ActiveMQ topic | Publish to `staffing-events-topic` | Publish staffing events |
| `ward-service` | ActiveMQ topic | Subscribe to `staffing-events-topic` | Process staffing events asynchronously |
| `ward-service` | ActiveMQ queue | Publish to `equipment-failure-queue` | Publish equipment failure alerts |
| ActiveMQ queue | `equipment-alert-service` | Consume from `equipment-failure-queue` | Process equipment failure alerts |

## Project structure

```text
healthsafe/
├── README.md
├── .gitignore
├── ingestion-service/          (port 7030)
│   ├── pom.xml
│   ├── README.md
│   └── src/main/
│       ├── java/co/wethinkcode/healthsafe/IngestionServiceApp.java
│       └── resources/wards-outdated.csv
├── ward-service/               (port 7031)
├── alert-level-service/        (port 7032)
├── staffing-service/           (port 7033)
├── equipment-alert-service/    (port 7034)
└── common/
    ├── docker-compose.yml
    └── README.md


## Test

The project includes automated tests for the service and messaging logic.

### Ward service

The Ward Service includes a unit test for processing staffing events received
from the ActiveMQ topic.

Run:

```bash
cd ward-service
mvn clean test