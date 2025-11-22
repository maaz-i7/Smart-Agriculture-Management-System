# Smart Agriculture Management System

**Comprehensive README — project analysis, OOP concepts, run instructions, class responsibilities, runtime flow, and extension notes**

---

## Table of contents
- Project overview
- Quick start (build & run)
- Files created / logs
- Packages and high-level structure
- Every OOP concept used (with examples)
- Class-by-class responsibilities and key methods
- Runtime flow (step-by-step)
- Threading, synchronization and safety notes
- How to modify / extend
- Troubleshooting
- License

---

## Project overview
This Java project simulates a small Smart Agriculture Management System. It models sensors (meters), actuators (manual and automatic), users (Admin, Agronomist, Farmer) and logging subsystems. The `App.java` file is the entry point. The simulation produces sensor logs and actuator activity logs while allowing user-driven configuration and control.

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

## Quick start — build & run
**Prerequisites**
- Java JDK 8 or later installed
- Command-line (bash / PowerShell) or an IDE (IntelliJ/Eclipse)

**From command-line (Linux / macOS / Git Bash):**
```bash
# from project root (where App.java is located)
mkdir -p out
# compile all .java files (uses bash globbing)
javac -d out App.java $(find src -name "*.java")
# run
java -cp out App
```

**From PowerShell (Windows):**
```powershell
mkdir out
# gather .java files and compile
$files = Get-ChildItem -Path .\src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -d out App.java $files
java -cp out App
```

**Run in an IDE:**
1. Open the project in IntelliJ or Eclipse.
2. Mark the `src` folder as `Sources` (if required).
3. Run `App.java` as a Java application.

**Notes:**
- `App` is in the default package; compiled classes must be on the classpath root `out/` when running.
- The program uses console input heavily; if your IDE doesn't attach a console, run from terminal.

---

## Files produced at runtime (default paths)
By default the simulator creates/updates files under `src/data_logs` (relative to the project root):
- `src/data_logs/actuator_activities/actuator_activities.txt` — actuator activity log (ActuatorLogger)
- `src/data_logs/sensors_data/sensors_data.txt` — plain text sensor log (MeterLogger)
- `src/data_logs/sensors_data/farm_data.csv` — CSV sensor log (MeterLogger)

Make sure these directories exist or the application will create the files — check file permissions.

---

## Packages and high-level structure
- `src.Field_Meters` — simulation engine and common interfaces
  - `FarmSimulator.java` — the simulation loop and environment model
  - `Sensor.java`, `DataLogger.java` — interfaces implemented by meters
  - `Meters/` — `Thermometer`, `Hygrometer`, `Photometer`, `SoilMoistureMeter`

- `src.Actuators` — actuators grouped into `Automatic_Actuators` and `Manual_Actuators`
  - Automatic: `Heater`, `ExhaustFan`, `Humidifier`, `Dehumidifier`
  - Manual: `IrrigationPump`, `DrainPipe`, `Shade`, `SodiumLamp`

- `src.Loggers` — logging utilities
  - `ActuatorLogger.java`, `MeterLogger.java`

- `src.Secure_Authentication` — authentication and security utilities
  - `Authentication` (interface), `Encryptor`, and custom exceptions under `Authentication_Exceptions/`

- `src.Users` — user model
  - `Person.java` (abstract), `Admin.java`, `Agronomist.java`, `Farmer.java`

- `App.java` (root) — entry point, does signup / login and starts simulation

---

## Every OOP concept used (and where)
The project uses many classic OOP concepts. Below is a thorough list with concrete examples and file references.

1. **Abstraction**
   - `Person` is declared `abstract` to provide common attributes and operations for all user types (see `src/Users/Person.java`).
   - Interfaces `Sensor` and `DataLogger` abstract behaviour of sensors and data-loggers (`src/Field_Meters/Sensor.java`, `src/Field_Meters/DataLogger.java`).

2. **Encapsulation**
   - `Person` hides the `hashedPassword` behind getters/setters and exposes `updatePassword()` (`src/Users/Person.java`).
   - Logger classes (`ActuatorLogger`, `MeterLogger`) keep `PrintWriter` instances private and expose `init()`/`log()`/`close()` methods.
   - Note: Several fields are public/static for simplicity (e.g., meter `data` fields). Consider tightening visibility for strict encapsulation.

3. **Inheritance**
   - `Admin`, `Agronomist`, and `Farmer` extend `Person` and inherit common fields and methods (`src/Users/*.java`).

4. **Interfaces & Implementation**
   - `Sensor` and `DataLogger` are implemented by each meter class (e.g., `Thermometer implements Sensor, DataLogger`). This models a meter being both a sensor and a data logger.
   - `Authentication` is declared as an interface but used as a static utility holder; it contains static helper methods for signUp/signIn.

5. **Polymorphism**
   - Methods in meter classes share names that are invoked polymorphically by the simulation loop (e.g., `senseData`, `sendData`, `receiveData`, `logData`). Each meter implements them.

6. **Static members and utility-style classes**
   - Many classes use `static` members and methods (e.g., `ActuatorLogger.log(...)`, `MeterLogger.init(...)`, `Authentication` static methods). These act like global services.

7. **Inner classes (non-static inner class)**
   - `Admin.FieldAdmin` is an inner class inside `Admin` (see `src/Users/Admin.java`). This groups admin-specific field configuration features with `Admin`.

8. **Custom Exceptions**
   - Authentication-related custom exceptions (e.g., `InvalidUsernameException`, `WeakPasswordException`) in `src/Secure_Authentication/Authentication_Exceptions/`.

9. **Threads & Concurrency (multithreading)**
   - `MeterLogger` spawns a thread to receive sensor data (creates `Thread` and `meterLoggerThread.start()`), and `FarmSimulator.start()` loops with sleep intervals. `ActuatorLogger` and `MeterLogger` use synchronization to protect file writes.

10. **Composition**
    - Meters are composed of sensor & data-logger roles; `FarmSimulator` composes meters and actuators to model a field.

11. **Exception handling**
    - The code uses `try/catch` for IO and initialization, and throws/handles custom authentication exceptions.

12. **Factory / Service patterns (informal)**
    - There is no formal factory pattern, but `Authentication.signIn()` and `signUp()` are central service-style methods that create and register `Person` objects.

13. **Single Responsibility Principle (partial)**
    - Classes are organized by responsibility: meters handle sensing, loggers handle logging, users handle user actions, FarmSimulator coordinates simulation. Some utility classes mix responsibilities via static state.

---

## Class-by-class responsibilities (detailed)
> For each class below, I give: *short purpose*, *key methods/fields used at runtime*, and *connections to other classes*.

### App.java
- **Purpose:** Entry point. Handles initial sign-up, user creation, login, invoking admin tasks and finally starting the FarmSimulator.
- **Key flow:** calls `Authentication.signUp()` → prompts to update users → `Authentication.signIn()` for Agronomist, Farmer, Admin → uses `Admin.FieldAdmin` to set thresholds → calls `FarmSimulator.start()`.

### `src/Users/Person.java` (abstract)
- **Purpose:** Base for all user types. Holds shared attributes (name, username, accountType, hashedPassword, phoneNum, salary).
- **Key methods:** `getName()`, `getUsername()`, `updatePassword()`, `updatePhoneNum()`, `getHashedPassword()`.
- **Connections:** Extended by Admin, Agronomist, Farmer. Uses `Encryptor` for password hashing.

### `src/Users/Admin.java`
- **Purpose:** Admin user with abilities to create, delete and manage user accounts.
- **Key methods:** `createUser()`, `deleteUser()`, `updatePasswordOfUser()`, `logAllUsers()`; includes inner class `FieldAdmin` (see below).
- **Inner class `FieldAdmin`:** stores thresholds and billing/cost accumulators used by `FarmSimulator` and actuators (static fields like `MIN_TEMPERATURE_THRESHOLD`, `totalCost`).

### `src/Users/Agronomist.java`
- **Purpose:** Reads and analyzes meter data (static methods that keep last-read values) and may inform farmer decisions.
- **Key methods:** `readAndAnalyzeHumidityData(...)`, `readAndAnalyzeTemperatureData(...)` etc. It receives Meter objects to read `.data`.

### `src/Users/Farmer.java`
- **Purpose:** Contains actions a farmer can take to manually operate actuators (turn pumps/lights on or off).
- **Key methods:** `turnShadeOn/Off()`, `startIrrigation()`, `stopIrrigation()`, `turnSodiumLampOn/Off()` — which call actuator classes' static `turnOn`/`turnOff`.

### `src/Secure_Authentication/Authentication.java` (interface used as utility)
- **Purpose:** Central place for managing user signup and sign-in, user storage (static `Person[] users`) and validation.
- **Key methods:** `signUp()`, `signIn()`, `checkValidUsername()`, `checkValidName()` and helper methods. Uses custom exceptions on invalid input.
- **Connections:** Creates instances of `Admin`, `Agronomist`, `Farmer` and stores them in the `users` array.

### `src/Secure_Authentication/Encryptor.java`
- **Purpose:** Simulated hashing & matching of passwords.
- **Key methods:** `hashPassword(String)`, `isPasswordMatch(Person, String)`.

### `src/Field_Meters/Sensor.java` and `src/Field_Meters/DataLogger.java` (interfaces)
- **Purpose:** Define the methods meters must implement: `senseData`, `sendData`, `receiveData`, `logData`.

### `src/Field_Meters/Meters/*` (Thermometer, Hygrometer, Photometer, SoilMoistureMeter)
- **Purpose:** Implement the `Sensor` and `DataLogger` interfaces. Each meter stores a `data` field and implements the chain: `senseData()` → `sendData()` → `receiveData()` → `logData()`.
- **Connections:** Call `MeterLogger.log(...)` to persist sensed values; Agronomist reads `.data` from these classes.

### `src/Actuators/Automatic_Actuators/*` (Heater, ExhaustFan, Humidifier, Dehumidifier)
- **Purpose:** Automatic actuators that respond to sensor values and `FieldAdmin` thresholds. They expose static fields like `active`, `manualSwitch`, `effect`, and static methods `turnOn()`/`turnOff()` and `tryOperateBasedOn(...)`.
- **Key behavior:** `tryOperateBasedOn(timeStamp, value)` contains logic to decide automatic on/off depending on `manualSwitch` and threshold ranges.
- **Connections:** Called by `FarmSimulator` when sensor values are updated.

### `src/Actuators/Manual_Actuators/*` (IrrigationPump, DrainPipe, Shade, SodiumLamp)
- **Purpose:** Manual actuators controlled by Farmer methods. They also have an `effect`, `operationCharges`, `active` and `manualSwitch`.
- **Connections:** Farmer calls these directly. `FarmSimulator` accounts for their compound effect on simulation variables.

### `src/Loggers/ActuatorLogger.java` and `src/Loggers/MeterLogger.java`
- **Purpose:** Persist actuator activities and meter readings. Provide `init()`, `log()`, and `close()` APIs. `MeterLogger` uses a separate thread to aggregate and/or flush data.
- **Concurrency:** Use `synchronized` blocks and a `lock` object to ensure writes are safe across threads.

### `src/Field_Meters/FarmSimulator.java`
- **Purpose:** The core simulation engine. Models weather types, loops through a 24-hour cycle in 15-minute steps, generates noisy sensor values, applies actuator compound effects and logs sensor & actuator events.
- **Key methods:** `start()`, `updateCompoundEffectIncreaser()`, `updateCompoundEffectDecreaser()` — these compute gradual changes caused by actuators.
- **Connections:** Uses meters to sense & log data, uses actuators' `tryOperateBasedOn()` or `turnOn/turnOff()` methods, updates `Admin.FieldAdmin.totalCost`.

---

## Runtime flow (step-by-step)
1. `App.main()` is executed. The console shows a header and prompts to create the initial Admin user using `Authentication.signUp()`.
2. The program optionally allows admin to update other users (via `Admin` methods invoked in `App`).
3. Users login: Agronomist and Farmer are requested to sign in (`Authentication.signIn()` returns `Agronomist` and `Farmer` objects). Admin logs in and an inner `FieldAdmin` object is used to configure thresholds (temperature, humidity, soil moisture, light intensity) and adjust operation costs.
4. `Authentication.logAllDetailsOfUsers()` writes current users to a `usersData.txt` file.
5. `FarmSimulator.start()` is called — it:
   - Initializes `ActuatorLogger` and `MeterLogger` with file paths.
   - Boots meter instances (`Hygrometer`, `Photometer`, `SoilMoistureMeter`, `Thermometer`).
   - Enters a nested loop for `hour` (0–23) and `min` (0–45 step 15) — 15-minute ticks.
   - On each tick, it computes simulated weather variables (temperature, humidity, light, soil moisture) using base values, time of day, noise and the compound effects of active actuators.
   - For each meter it calls `senseData(timeStamp, value)` which cascades down to `MeterLogger.log(...)`.
   - Automatic actuators evaluate the new sensor values via their `tryOperateBasedOn()` logic and call `turnOn()`/`turnOff()` accordingly; these calls are recorded via `ActuatorLogger`.
   - The simulation updates `Admin.FieldAdmin.totalCost` according to running actuators' hourly operationCharges (pro-rated per 15 minutes).
6. `MeterLogger` may run a dedicated thread to aggregate/flush data and writes sensor readings to both text and CSV logs; `ActuatorLogger` writes actuator events to its file. Both use synchronized logging to be thread-safe.

---

## Threading & synchronization notes
- `MeterLogger` creates and runs a thread to receive data; `ActuatorLogger` provides synchronized `log()` to serialise file writes.
- Because the simulation and logging run on multiple threads, the project uses `synchronized` and private `lock` objects where necessary — but many fields are `static` and `public`. If you introduce concurrency between simulation ticks and user console actions you must carefully consider thread visibility.

---
