# Speedrun Input Analyzer

A Java desktop application that displays controller inputs on individual emulator frames. The goal is to help speedrunners review frame-perfect techniques and determine whether buttons were pressed, held, or released on the intended frames.

Currently integrates with Mesen through a Lua script to capture NES controller inputs. Tested with Mesen 2; compatibility with other Mesen versions and forks has not been verified.

## Current Features

- Captures live NES controller input from Mesen
- Displays inputs frame by frame
- Starts and stops session recording
- Clears the current session
- Exports recorded sessions as CSV files
- Automatically scrolls as new frames arrive
- Uses an input-source abstraction designed for future emulator support

| Frame | U | D | L | R | A | B | Start | Select |
|------:|:-:|:-:|:-:|:-:|:-:|:-:|:-----:|:------:|
| 100   |   |   |   | R |   | B |       |        |
| 101   |   |   |   | R |   | B |       |        |
| 102   |   |   |   | R | A | B |       |        |
| 103   |   |   |   | R | A | B |       |        |
| 104   |   |   |   | R |   | B |       |        |

This makes it possible to see exactly when a button first appears, how long it remains held, and when it is released.

## Technologies

- Java 21
- JavaFX 21
- Maven
- Lua
- Mesen
- CSV

## How It Works

```text
Controller Input
       ↓
Mesen
       ↓
Lua input callback
       ↓
input_stream.csv
       ↓
Java input source
       ↓
FrameInput objects
       ↓
JavaFX table
```

The Lua script runs inside Mesen and records the controller state whenever the emulator polls input for a frame.

The Java application watches the generated CSV file, converts each new row into a `FrameInput` object, and displays it in the JavaFX interface.

## Project Structure

```text
NESInputReader/
├── pom.xml
├── README.md
├── scripts/
│   └── mesen/
│       └── input_stream.lua
└── src/
    └── main/
        └── java/
            └── com/
                └── speedrunanalyzer/
                    ├── App.java
                    ├── FrameInput.java
                    ├── InputSource.java
                    ├── FakeInputSource.java
                    └── RealInputSource.java
```

## Requirements

Before running the application, install:

- Java Development Kit 21
- Apache Maven
- A version of Mesen that supports the required Lua scripting APIs

Confirm that Java and Maven are using Java 21:

```powershell
java -version
mvn -version
```

## Setup

### 1. Clone the Repository

```powershell
git clone <repository-url>
cd NESInputReader
```

Replace `<repository-url>` with the URL of this GitHub repository.

### 2. Load the Lua Script in Mesen

The Lua script is located at:

```text
scripts/mesen/input_stream.lua
```

In Mesen:

1. Open an NES game.
2. Open the Lua scripting window.
3. Enable access to I/O and operating-system functions.
4. Load `input_stream.lua`.
5. Start the script.

Mesen should display a message similar to:

```text
Writing input stream to:
C:\...\Mesen\LuaScriptData\input_stream\input_stream.csv
```

The exact location depends on the user's Mesen installation and the Lua script's filename.

### 3. Set the Input-File Environment Variable

Create an environment variable named:

```text
MESEN_INPUT_PATH
```

Set its value to the full path printed by the Lua script:

```text
C:\...\Mesen\LuaScriptData\input_stream\input_stream.csv
```

Do not include quotation marks around the path.

In PowerShell, verify that the environment variable is available and points to an existing file:

```powershell
echo $env:MESEN_INPUT_PATH
Test-Path $env:MESEN_INPUT_PATH
```

`Test-Path` should return:

```text
True
```

Restart IntelliJ or your terminal after creating or modifying the environment variable.

### 4. Run the Application

Start Mesen and the Lua script first. Then run:

```powershell
mvn javafx:run
```

## Using the Application

1. Start an NES game in Mesen.
2. Load and run `input_stream.lua`.
3. Launch the Java application.
4. Click **Start Recording**.
5. Play or practice the desired technique.
6. Click **Stop Recording**.
7. Review the input state on each frame.
8. Use **Clear** to begin another attempt.
9. Use **Save Session** to export the recording as a CSV file.

## Exported Session Format

Saved sessions use the following CSV format:

```csv
Frame,Up,Down,Left,Right,A,B,Start,Select
100,0,0,0,1,0,1,0,0
101,0,0,0,1,0,1,0,0
102,0,0,0,1,1,1,0,0
```

A value of `1` means the button was active on that frame. A value of `0` means it was inactive.

## Current Limitations

- Only NES controls are currently supported
- Mesen is currently the only supported emulator family
- Compatibility may vary between Mesen versions depending on Lua API support
- Communication between Mesen and Java currently uses a CSV file
- The user must manually configure `MESEN_INPUT_PATH`
- Saved sessions cannot yet be loaded back into the application
- The interface is still an early version with limited styling

## Planned Features

- Load previously saved sessions
- Select the Mesen input file through the application
- Remember the selected file automatically
- Compare a practice attempt with a reference attempt
- Detect inputs performed early or late
- Add bookmarks for important frames
- Display statistics across repeated attempts
- Improve the JavaFX interface and styling
- Replace CSV polling with local socket communication
- Support additional emulators
- Support SNES, Genesis, Game Boy, and other consoles
- Support analog inputs for systems such as Nintendo 64

## Long-Term Goal

The long-term goal is to create an emulator-independent speedrunning practice tool that can record, visualize, and compare frame-level controller inputs across several consoles and emulators.

## Disclaimer

This project is an independent third-party tool and is not affiliated with
or endorsed by the Mesen project or its contributors. Mesen must be obtained
separately by the user.

Mesen is licensed separately under the GNU General Public License v3.0.
