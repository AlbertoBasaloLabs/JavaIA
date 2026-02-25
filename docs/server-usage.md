# Server Scripts Usage

This project includes two helper scripts to run and manage the Spring Boot server on port `8080`:

- `server.bat` (Command Prompt / Git Bash calling CMD)
- `server.ps1` (PowerShell)

## Prerequisites

- Java and Maven Wrapper files available in project root (`mvnw.cmd`)
- Run commands from the project root folder
- Default port used by scripts: `8080`

---

## Using `server.bat`

### Start server (foreground)

```bat
server.bat start
```

### Stop server (kill process listening on 8080)

```bat
server.bat stop
```

### Check server status

```bat
server.bat status
```

---

## Using `server.ps1`

### Start server (foreground)

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\server.ps1 -Action start
```

### Stop server

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\server.ps1 -Action stop
```

### Check status

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\server.ps1 -Action status
```

### Optional: custom port

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\server.ps1 -Action status -Port 8081
```

---

## Troubleshooting

- If startup fails with port conflict, run `stop` first and retry `start`.
- `TIME_WAIT` entries on port `8080` are normal and do not mean a process is actively listening.
- If PowerShell blocks script execution, keep using `-ExecutionPolicy Bypass` as shown above.

---

## Recommended flow

1. Check current status
2. Stop if needed
3. Start server
4. Test endpoints from REST Client (`e2e/astro_biblia_1-basic.http`)
