-- =====================================================================
-- UG Smart Academic & IA Alert Dispatcher
-- schema.sql
-- Author: Glorious (Data & System Architecture team)
-- Target: PostgreSQL (also runs on SQLite 3 with the two notes below)
--
-- SQLite compatibility notes:
--   1. Replace every "SERIAL" with "INTEGER" and add "AUTOINCREMENT"
--      right after "PRIMARY KEY" (SQLite has no SERIAL type).
--   2. SQLite ignores CHECK/FOREIGN KEY enforcement unless you run
--      "PRAGMA foreign_keys = ON;" first -- add that line before
--      running this file in sqlite3.
-- =====================================================================

-- ---------------------------------------------------------------------
-- 1. LOCATIONS
-- One row per campus venue (lecture hall, exam hall, landmark, etc.)
-- This table is the node list for the graph-routing algorithm
-- (Dijkstra / BFS / MST) and is populated from locations.csv.
-- ---------------------------------------------------------------------
CREATE TABLE locations (
    locationId  VARCHAR(10)     PRIMARY KEY,        -- e.g. 'NNB', 'JQB', 'CC'
    name        VARCHAR(100)    NOT NULL,            -- full venue name
    area        VARCHAR(100)    NOT NULL,            -- campus zone / block
    type        VARCHAR(30)     NOT NULL,            -- 'LECTURE_HALL', 'EXAM_HALL',
                                                       -- 'LANDMARK', 'ADMIN', etc.
    latitude    DECIMAL(9,6)    NOT NULL,
    longitude   DECIMAL(9,6)    NOT NULL
);

-- ---------------------------------------------------------------------
-- 2. ROADS
-- One row per walkable connection between two locations. This is the
-- weighted-edge list the routing algorithm uses to compute the
-- shortest / fastest path between an old and a new IA venue.
-- Populated from roads.csv.
-- ---------------------------------------------------------------------
CREATE TABLE roads (
    roadId               SERIAL          PRIMARY KEY,
    fromLocationId        VARCHAR(10)     NOT NULL REFERENCES locations(locationId),
    toLocationId          VARCHAR(10)     NOT NULL REFERENCES locations(locationId),
    distance              DECIMAL(6,2)    NOT NULL,   -- metres
    travelTime            INTEGER         NOT NULL,   -- estimated seconds on foot
    roadConditionWeight   DECIMAL(4,2)    NOT NULL DEFAULT 1.00
        CHECK (roadConditionWeight >= 1.00)           -- 1.00 = clear path,
                                                        -- higher = crowding/obstruction
);

-- ---------------------------------------------------------------------
-- 3. SERVICE_REQUESTS
-- Every alert that has to be dispatched to students/lecturers:
-- venue swaps, reschedules, seat-allocation requests, disputes, etc.
-- priorityLevel feeds the priority-queue / deque dispatcher so an
-- urgent last-minute venue change jumps ahead of routine notices.
-- ---------------------------------------------------------------------
CREATE TABLE service_requests (
    requestId       SERIAL          PRIMARY KEY,
    requestType     VARCHAR(30)     NOT NULL,   -- 'VENUE_CHANGE', 'RESCHEDULE',
                                                  -- 'SEAT_ALLOCATION', 'DISPUTE'
    locationId      VARCHAR(10)     REFERENCES locations(locationId),
    priorityLevel   INTEGER         NOT NULL DEFAULT 3
        CHECK (priorityLevel BETWEEN 1 AND 5),   -- 1 = most urgent
    status          VARCHAR(20)     NOT NULL DEFAULT 'PENDING',
                                                  -- 'PENDING','DISPATCHED','RESOLVED'
    description     TEXT,
    createdAt       TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------------------
-- 4. RESOURCES
-- Seat / hall capacity records used by the DP-knapsack and greedy
-- seat-allocation algorithm to fit class streams into available halls.
-- Seeded with the assigned initial capacity of 20 records (see the
-- INSERT block at the bottom of this file).
-- ---------------------------------------------------------------------
CREATE TABLE resources (
    resourceId      SERIAL          PRIMARY KEY,
    locationId      VARCHAR(10)     NOT NULL REFERENCES locations(locationId),
    resourceType    VARCHAR(30)     NOT NULL DEFAULT 'HALL_SEATING',
    capacity        INTEGER         NOT NULL CHECK (capacity >= 0),
    allocatedCount  INTEGER         NOT NULL DEFAULT 0
        CHECK (allocatedCount <= capacity),
    lastUpdated     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------------------
-- 5. ALGORITHM_RUNS
-- A log of every time a core algorithm executes (Dijkstra route
-- calculation, priority-queue dispatch, DP seat allocation, hash-table
-- lookup, etc.) so the team can benchmark and debug the engine.
-- ---------------------------------------------------------------------
CREATE TABLE algorithm_runs (
    runId               SERIAL          PRIMARY KEY,
    algorithmType       VARCHAR(40)     NOT NULL,   -- 'DIJKSTRA','MST','BFS',
                                                      -- 'PRIORITY_QUEUE','DP_KNAPSACK',
                                                      -- 'HASH_LOOKUP','BST_SEARCH'
    triggeredBy         VARCHAR(50),                -- request/user that triggered the run
    inputSummary        TEXT,
    outputSummary       TEXT,
    executionTimeMs     INTEGER,
    runAt               TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------------------
-- 6. AUDIT_EVENTS
-- The stack-based audit / undo log. Every schedule-changing action is
-- "pushed" here (highest eventId = top of stack). To undo, the
-- application reads the most recent un-reverted row for that
-- targetId and restores previousValue, then marks isReverted = TRUE.
-- ---------------------------------------------------------------------
CREATE TABLE audit_events (
    eventId         SERIAL          PRIMARY KEY,
    actionType      VARCHAR(30)     NOT NULL,   -- 'VENUE_CHANGE','RESCHEDULE',
                                                  -- 'SEAT_REALLOCATION'
    targetTable     VARCHAR(30)     NOT NULL,   -- table the change applied to
    targetId        VARCHAR(20)     NOT NULL,   -- primary key value affected
    performedBy     VARCHAR(50)     NOT NULL,
    previousValue   TEXT,                        -- JSON/text snapshot before the change
    newValue        TEXT,                        -- JSON/text snapshot after the change
    isReverted      BOOLEAN         NOT NULL DEFAULT FALSE,
    createdAt       TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================================
-- SEED DATA
-- Resource capacity seed size (per M1 assignment):
--   Sum of last 3 ID digits (2 + 9 + 9) = 20 records.
-- The 20 rows below use locationId values LOC001-LOC020, matching
-- Benedict's locations.csv ID format exactly (3-digit, zero-padded).
-- These rows require locations.csv to be loaded into the locations
-- table first, since locationId is a foreign key.
-- =====================================================================
INSERT INTO resources (locationId, resourceType, capacity, allocatedCount) VALUES
('LOC001', 'HALL_SEATING', 120, 0),
('LOC002', 'HALL_SEATING', 150, 0),
('LOC003', 'HALL_SEATING', 80,  0),
('LOC004', 'HALL_SEATING', 200, 0),
('LOC005', 'HALL_SEATING', 60,  0),
('LOC006', 'HALL_SEATING', 100, 0),
('LOC007', 'HALL_SEATING', 90,  0),
('LOC008', 'HALL_SEATING', 130, 0),
('LOC009', 'HALL_SEATING', 75,  0),
('LOC010', 'HALL_SEATING', 110, 0),
('LOC011', 'HALL_SEATING', 95,  0),
('LOC012', 'HALL_SEATING', 140, 0),
('LOC013', 'HALL_SEATING', 65,  0),
('LOC014', 'HALL_SEATING', 105, 0),
('LOC015', 'HALL_SEATING', 85,  0),
('LOC016', 'HALL_SEATING', 160, 0),
('LOC017', 'HALL_SEATING', 70,  0),
('LOC018', 'HALL_SEATING', 115, 0),
('LOC019', 'HALL_SEATING', 55,  0),
('LOC020', 'HALL_SEATING', 125, 0);

-- ---------------------------------------------------------------------
-- Helpful indexes (supports the BST/hash-table style fast lookups
-- described in the concept: quick search by course/location and
-- sorted retrieval of upcoming requests by time).
-- ---------------------------------------------------------------------
CREATE INDEX idx_roads_from            ON roads(fromLocationId);
CREATE INDEX idx_roads_to              ON roads(toLocationId);
CREATE INDEX idx_service_requests_time ON service_requests(createdAt);
CREATE INDEX idx_service_requests_prio ON service_requests(priorityLevel);
CREATE INDEX idx_audit_events_target   ON audit_events(targetTable, targetId);
