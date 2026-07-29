# NextClaims

A Minecraft claims plugin for protecting player land.

## Building

### Prerequisites

- **Java 21** or higher
- **Internet connection** (Gradle will download dependencies automatically)

### Quick Start

#### Windows
```batch
build.bat
```

#### Linux / macOS
```bash
./gradlew build
```

### Manual Build

1. **Download Gradle Wrapper** (if not present):
   ```bash
   gradle wrapper --gradle-version 8.9
   ```

2. **Build the project**:
   ```bash
   ./gradlew build
   ```

3. **Output JARs**:
   - `bukkit/build/libs/nextclaims-Bukkit-*.jar` - Bukkit/Spigot version
   - `paper/build/libs/nextclaims-Paper-*.jar` - Paper version
   - `fabric/build/libs/nextclaims-Fabric-*.jar` - Fabric version

### Development Build

To build without running tests:
```bash
./gradlew build -x test
```

### Clean Build

```bash
./gradlew clean build
```

## Project Structure

```
nextclaims/
├── build.gradle          # Root build config
├── settings.gradle       # Multi-module settings
├── bukkit/              # Bukkit platform module
├── paper/               # Paper platform module
├── fabric/              # Fabric platform module
└── common/              # Shared code (auto-downloaded)
```

## Requirements

- Minecraft 1.20.4+
- Java 21+

## License

Apache License 2.0
