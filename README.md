# Smart Agriculture Management System — README

Java-based Smart Agriculture Monitoring System using Object Oriented Programming approach that helps farmers remotely monitor and control different parameters of their agricultural fields such as soil moisture, temperature, humidity, irrigation, and crop health using a simulated sensor network.

> **Comprehensive interactive guide** for the Java project contained in `Smart_Agriculture_System.zip`.
> This README documents the entire codebase, how to build/run it, how modules interact, data formats, extension points, security notes, and suggested improvements.

---

## Table of contents
1. [Project summary & purpose](#project-summary--purpose)
2. [Quick start (run in 2 minutes)](#quick-start-run-in-2-minutes)
3. [Build & run (detailed)](#build--run-detailed)
4. [Project layout (files & folders)](#project-layout-files--folders)
5. [Key components and class map](#key-components-and-class-map)
6. [Data files & formats](#data-files--formats)
7. [How the simulation works — runtime flow](#how-the-simulation-works---runtime-flow)
8. [Authentication & encryption details](#authentication--encryption-details)
9. [Logging, persistence, and data sinks](#logging-persistence-and-data-sinks)
10. [Extending the system (adding sensors / actuators)](#extending-the-system-adding-sensors--actuators)
11. [Runtime examples & walkthroughs (menus, sample run)](#runtime-examples--walkthroughs-menus-sample-run)
12. [Testing & debugging tips](#testing--debugging-tips)
13. [Known issues, limitations & security concerns](#known-issues-limitations--security-concerns)
14. [Suggested improvements & roadmap](#suggested-improvements--roadmap)
15. [Contribution, license, authorship](#contribution-license-authorship)

---

## Project summary & purpose
This project is a *Smart Agriculture Management System* written in plain Java (no Maven/Gradle wrapper). It simulates a farm environment (temperature, humidity, light, soil moisture) via `Field_Meters` modules, records sensor data, and provides both **manual** and **automatic** actuators to respond to conditions. It includes a local file-based logging and a simple user system with roles: **Admin**, **Agronomist**, and **Farmer**. It is entirely based on Object Oriented Programming Principles.

Primary goals of the project:
- Reflect the significance and role of Object Oriented Programming Principles.
- Simulate sensor & actuator behaviors in a greenhouse/farm environment.
- Provide role-based interactive CLI for administration, agronomy decisions, and farmer controls.
- Demonstrate file-based data logging and rudimentary authentication.

This repository contains ~29 Java source files and multiple data log files used by the simulator.

---

## Quick start (run in 2 minutes)
**Assumptions**: You have Java JDK (11+ recommended) installed and are running from the project root directory where `Smart_Agriculture_System` folder is present.

From project root (Unix/macOS):
```bash
# compile everything into `bin`
find Smart_Agriculture_System -name "*.java" > sources.txt
javac -d bin @sources.txt
# run the App entry point (App.java lives at project root level inside the folder)
java -cp bin App
```

Windows (PowerShell / cmd):
```powershell
rem Compile (Windows)
for /R Smart_Agriculture_System %f in (*.java) do @echo %f >> sources.txt
javac -d bin @sources.txt
java -cp bin App
```

**IDE**: Import the folder as a Java project (create a new Java project and add `Smart_Agriculture_System` as source root). Set working directory to project root so relative data paths like `src\data_logs\...` resolve correctly.

---

## Build & run (detailed)
**Prereqs**
- Java JDK 8+ (11 recommended)
- ~200KB disk space for logs

**Compile steps**
1. From repo root create `bin` directory.
2. Use `javac -d bin <all .java files>` (see quick start commands to generate `sources.txt`).
3. Run `java -cp bin App` (the `App` class is the entrypoint and is *not* packaged under `src.*`, so run it by simple name).

**Notes on working directory**
- The code reads/writes *relative file paths* like `src\data_logs\sensors_data\sensors_data.txt`. Run the program with the current working directory being the repository root so those paths map correctly.
- On Linux/macOS the backslashes still work with Java `File` APIs but prefer using the exact project layout.

**IDE run configuration**
- Set the main class to `App`.
- Set working directory to the repository root (so `src/data_logs/...` is reachable).
- Add `bin` as the output folder when compiling within the IDE.

---

## Project layout (files & folders)
Below is a human-friendly tree of the main files (trimmed to important ones):

```
Smart_Agriculture_System/
├─ App.java                     # Main entrypoint (CLI)
├─ src/
│  ├─ Field_Meters/
│  │  ├─ FarmSimulator.java     # Sensor simulator & data feeder
│  │  ├─ Sensor.java            # Sensor interface + start logic
│  │  ├─ Meters/                # Individual meter implementations
│  │  │  ├─ Hygrometer.java
│  │  │  ├─ Photometer.java
│  │  │  ├─ SoilMoistureMeter.java
│  │  │  └─ Thermometer.java
│  ├─ Actuators/
│  │  ├─ Automatic_Actuators/   # Automatic actuators triggered by simulation
│  │  │  ├─ Dehumidifier.java
│  │  │  ├─ ExhaustFan.java
│  │  │  ├─ Heater.java
│  │  │  └─ Humidifier.java
│  │  └─ Manual_Actuators/      # Manual actuators controlled by users
│  │     ├─ DrainPipe.java
│  │     ├─ IrrigationPump.java
│  │     ├─ Shade.java
│  │     └─ SodiumLamp.java
│  ├─ Data_Logger/
│  │  └─ ActuatorLogger.java
│  ├─ Secure_Authentication/
│  │  ├─ Authentication.java
│  │  ├─ Encryptor.java         # Simulated hashing mechanism
│  │  └─ Authentication_Exceptions/ (various exceptions)
│  ├─ Users/
│  │  ├─ Person.java
│  │  ├─ Admin.java
│  │  ├─ Agronomist.java
│  │  └─ Farmer.java
│  └─ data_logs/                # example logs and datasets
│     ├─ actuator_activities/actuator_activities.txt
│     └─ sensors_data/
│        ├─ farm_data.csv
│        └─ sensors_data.txt
```

**Files created/used at runtime**
- `src/data_logs/sensors_data/sensors_data.txt` — main sensor data feed used by the simulator.
- `src/data_logs/sensors_data/farm_data.csv` — a historical CSV with sample farm readings.
- `src/data_logs/actuator_activities/actuator_activities.txt` — log of actuator on/off and changes.
- `src/data_logs/users_data/users_data.txt` — persisted users info (username, encrypted password, role) — *the program uses simple text-based persistence.*

---

## Key components and class map
This section explains the responsibilities of core packages and classes.

### App.java (Entrypoint)
- Presents a CLI to create an initial **Admin** user, then allows login with roles.
- On login, launches role-specific menus (Admin, Agronomist, Farmer).
- Starts the `FarmSimulator` background process which feeds sensor data.

### Field_Meters (Sensors & Simulator)
- `Sensor.java` — interface & orchestrator; it defines how sensors should send/receive data and exposes `startSensingData()` which bootstraps the simulation.
- `FarmSimulator.java` — central simulation loop. It reads `sensors_data.txt` continuously and uses `Random` + actuator compound effects to mutate environmental variables (soil moisture, humidity, temperature, light). It triggers automatic actuators when thresholds are breached.
- Meters under `Meters/` (Photometer, Thermometer, Hygrometer, SoilMoistureMeter) simulate granular readings and feed them to the logger.

### Actuators
- **Automatic_Actuators**: React to sensor thresholds automatically. E.g. `Humidifier`, `Dehumidifier`, `ExhaustFan`, `Heater`.
- **Manual_Actuators**: Manually controlled components used by Farmer/Admin via CLI: `IrrigationPump`, `DrainPipe`, `Shade`, `SodiumLamp`.
- `ActuatorLogger.java` logs actuator activities to `actuator_activities.txt`.

### Secure_Authentication
- `Authentication.java` — interface defining user creation, validation, and helper validators (username/name/password checks). Also stores an in-memory `Person[] users` array (size 100) and `usersTypes` list.
- `Encryptor.java` — a simulated hashing/encryption class (non-standard, reversible-like algorithm used to obfuscate passwords in the `users_data.txt`). See **Security** section for caveats.
- Several custom exceptions in `Authentication_Exceptions` for invalid inputs.

### Users
- `Person.java` — common fields & methods for users (username, name, userType, password hash handling, etc.).
- `Admin`, `Agronomist`, `Farmer` — role-specific UI and behavior. `Admin` keeps track of `currentUsersCount` and can change global thresholds (max temperature, soil moisture, etc.).

---

## Data files & formats
**1) sensors_data.txt** (primary feed)
- Format: newline-delimited time-stamped sensor tuples used by the simulator.
- The simulator reads lines such as (example from included CSV):

```
00:00,20.60,81.02,10000,47.68
00:15,16.80,79.06,0,48.86
```

Interpretation (fields):
- Timestamp (HH:mm)
- Temperature (°C)
- Humidity (%)
- Light (a number; code treats `10000` as light-on/full and `0` as night)
- Soil moisture (%)

**2) farm_data.csv**
- Historical CSV (headers included) with the same columns as above. Use as a dataset for testing or to seed simulation.

**3) actuator_activities.txt**
- Appends human-readable logs when actuators change state, including automatic triggers and manual operations.

**4) users_data.txt**
- Stores user entries in a textual format. The passwords are stored after running through `Encryptor.hashPassword` — this is an obfuscation but not a secure hashing scheme.

---

## How the simulation works — runtime flow
1. `App` starts and ensures at least one Admin account exists.
2. `App` calls `Sensor.startSensingData()` which starts `FarmSimulator`.
3. `FarmSimulator` continuously reads `sensors_data.txt` (it sleeps between reads to simulate passage of time).
4. For each time step the simulator:
   - Parses line to derive temperature, humidity, light, soilMoisture.
   - Applies random variation and **compound effects** from manual actuators (e.g., irrigation increases soil moisture over several ticks).
   - Checks thresholds; if an automatic actuator should turn on/off, it invokes that actuator and logs the activity.
   - Sensors report to data loggers which append records to logs (CSV/text).
5. The CLI (App) concurrently allows logged-in users to manually operate actuators, query sensor values, modify thresholds (Admin), or view logs.

Sequence diagram (simplified):
```
App --> FarmSimulator: startSensingData()
FarmSimulator --> sensors_data.txt: read next line
FarmSimulator --> Meters: update readings
FarmSimulator --> ActuatorLogger: log events
User (Admin) --> App: change thresholds
App --> Actuators: manual control
```

---

## Authentication & encryption details
- The system uses a custom-developed `Encryptor.hashPassword(String)` method to store password-like data. The algorithm: reverse the password, perform index-based integer transformations, and store integers as obfuscated values.

**Security notes**
- The `Encryptor` is *not* a standard cryptographic hash (e.g., SHA-256) and appears to be reversible or vulnerable to brute-force. **Do not** consider it secure for real deployments.
- Passwords and users are stored in plaintext-like files (even if obfuscated). For production, replace `Encryptor` with a salted cryptographic hash (PBKDF2, bcrypt, scrypt, or Argon2) and use proper secure storage (database or OS-protected files).

---

## Logging, persistence, and data sinks
- All logs are file-based under `src/data_logs/`.
- Sensor readings are appended to `sensors_data.txt` and `farm_data.csv` (the simulator may open these files for read/write depending on code path).
- Actuator actions are appended to `actuator_activities.txt` by `ActuatorLogger`.
- Users are persisted to `users_data.txt` on account creation.

**Note**: File writes are not synchronized across threads in a heavy way; concurrent access may lead to race conditions if the code is modified to be multi-threaded.

---

## Extending the system (adding sensors / actuators)
**To add a new sensor**
1. Create a new class in `src/Field_Meters/Meters/` implementing the same interface/shape as existing meters (`DataLogger` methods and `senseData/sendData`).
2. Update `Sensor.startSensingData()` / `FarmSimulator` to instantiate and use the new meter.
3. Add CSV column handling in data files if you need to store the new metric.

**To add an actuator**
1. Decide if the actuator is `Automatic` or `Manual`.
2. Create the class under `Automatic_Actuators` or `Manual_Actuators`. Follow existing classes: provide `start()`, `stop()`, and `applyEffect()` methods consistent with how `FarmSimulator` looks up compound effects.
3. Add logging calls to `ActuatorLogger` when the actuator changes state.
4. If automatic, update threshold logic in `FarmSimulator`.

**Design tip**: Use a single `Actuator` interface (if you refactor) and a central registry so FarmSimulator can query all actuators polymorphically.

---

## Runtime examples & walkthroughs (menus, sample run)
When you run the `App` the CLI prompts are interactive. Typical flow:
1. Create Admin (enter username, name, password — validation enforces basic rules)
2. Login as Admin
3. Admin menu: manage users, set thresholds (e.g., `setMaxTemperatureThreshold`), view logs
4. Start/stop simulator or view sensor readings in real-time
5. Farmer/Agronomist menus: manually trigger actuators (Irrigation pump, Shade, SodiumLamp) and view sensor history

**Sample sensor data line** (from `farm_data.csv`):
```
00:00,20.60,81.02,10000,47.68
```
Which maps to `time, temp, humidity, light, soilMoisture`.

---

## Testing & debugging tips
- Use a small `sensors_data.txt` for quick iteration (10–20 lines) and watch the simulator loop over it.
- If `FarmSimulator` seems to hang, check the file paths (`src/data_logs/...`) and working directory.
- Add `System.out.println(...)` debug lines in `FarmSimulator` to trace threshold checks and actuator triggers.
- To verify authentication, inspect `src/data_logs/users_data/users_data.txt` after creating users.

---

## Known issues, limitations & security concerns
1. **Weak encryption**: `Encryptor` is not secure — replace with standard crypto.
2. **File-based persistence**: single-process, not concurrency-safe; consider migrating to a lightweight DB (SQLite, H2) or MongoDB.
3. **Hard-coded paths**: Paths use `src\\data_logs\\...` which is brittle; use `Paths.get(...)` and configurable properties.
4. **No structured configuration**: thresholds are hard-coded or set at runtime; add `config.properties` for environment configuration.
5. **No unit tests**: Add JUnit tests for core logic (threshold triggering, Encryptor correctness, parser functions).
6. **Threading & resource management**: `FarmSimulator` uses blocking IO loops — consider using scheduled executors for controlled timing.

---

## Suggested improvements & roadmap
Short-term (code-level):
- Replace `Encryptor` with PBKDF2/bcrypt; store salts per-user.
- Move data logs into `logs/` and make paths configurable.
- Replace ad-hoc `Person[]` with a `List<Person>` and persist as JSON (Gson/Jackson) for easier parsing.
- Add a CLI argument parser to control run-mode (simulate-only, headless, debug).

Medium-term (features):
- HTTP REST API + lightweight UI dashboard to visualize live sensor values.
- Persistence layer (H2/SQLite) and REST endpoints for logs and actuator controls.
- Add unit and integration tests with continuous integration.

Long-term (production):
- Role-based access control (RBAC) using a secure token mechanism.
- Replace file-based sensor feed with MQTT or socket-based feed for real sensors.

---

## Contribution, license, authorship
- This README was autogenerated after static analysis of the provided source tree.
- No explicit LICENSE file was found; add an appropriate license (`MIT`, `Apache-2.0`, etc.) if you plan to publish.

---

## Appendix A — Full file index (sources)
Below is a condensed index of Java sources discovered (29 files):

```
(App.java)
src/Field_Meters/*
src/Field_Meters/Meters/*
src/Actuators/Automatic_Actuators/*
src/Actuators/Manual_Actuators/*
src/Data_Logger/ActuatorLogger.java
src/Secure_Authentication/*
src/Secure_Authentication/Authentication_Exceptions/*
src/Users/*.java
src/data_logs/*
```

(If you'd like a literal file-by-file list, I can paste the entire tree into a separate file.)

---

## Appendix B — Quick reference commands
**Compile all Java sources**
```bash
find Smart_Agriculture_System -name "*.java" > sources.txt
javac -d bin @sources.txt
```
**Run**
```bash
java -cp bin App
```
**Open logs**
```bash
less src/data_logs/sensors_data/sensors_data.txt
less src/data_logs/actuator_activities/actuator_activities.txt
```

---
