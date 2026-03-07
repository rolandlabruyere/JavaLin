USE trafobuilddatabase;

CREATE TABLE IF NOT EXISTS tb900_sessiontracker (
  "AddressId" varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  "timestampStart" varchar(25) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY ("AddressId")
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


INSERT INTO tb900_sessiontracker ("AddressId", "timestampStart") VALUES ("127.0.0.1", "2025-06-30");