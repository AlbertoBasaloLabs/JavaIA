param(
  [ValidateSet('start', 'stop', 'status')]
  [string]$Action = 'status',
  [int]$Port = 8080
)

$ErrorActionPreference = 'Stop'

function Get-ListeningConnections {
  try {
    return Get-NetTCPConnection -LocalPort $Port -State Listen -ErrorAction SilentlyContinue
  } catch {
    return @()
  }
}

function Get-ListeningPids {
  return (Get-ListeningConnections | Select-Object -ExpandProperty OwningProcess -Unique)
}

switch ($Action) {
  'start' {
    Write-Host "[server] Starting Spring Boot on port $Port..."
    & "$PSScriptRoot\mvnw.cmd" -DskipTests spring-boot:run
    break
  }

  'stop' {
    $pids = Get-ListeningPids
    if (-not $pids -or $pids.Count -eq 0) {
      Write-Host "[server] No LISTENING process found on port $Port."
      break
    }

    foreach ($pid in $pids) {
      try {
        $proc = Get-Process -Id $pid -ErrorAction SilentlyContinue
        if ($null -ne $proc) {
          Write-Host "[server] Stopping PID $pid ($($proc.ProcessName)) on port $Port..."
        } else {
          Write-Host "[server] Stopping PID $pid on port $Port..."
        }
        Stop-Process -Id $pid -Force -ErrorAction SilentlyContinue
      } catch {
        Write-Host "[server] Could not stop PID $pid."
      }
    }

    Write-Host "[server] Stop completed."
    break
  }

  'status' {
    $connections = Get-ListeningConnections
    Write-Host "[server] Port $Port status:"

    if (-not $connections -or $connections.Count -eq 0) {
      Write-Host "  STOPPED (no LISTENING process)"
      break
    }

    foreach ($conn in $connections) {
      $pid = $conn.OwningProcess
      $procName = (Get-Process -Id $pid -ErrorAction SilentlyContinue).ProcessName
      if ([string]::IsNullOrWhiteSpace($procName)) {
        $procName = 'unknown'
      }

      Write-Host ("  LISTENING {0}:{1} PID={2} NAME={3}" -f $conn.LocalAddress, $conn.LocalPort, $pid, $procName)
    }
    break
  }
}
