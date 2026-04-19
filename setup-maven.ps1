# Setup Maven environment variables

# 1. Set MAVEN_HOME
[Environment]::SetEnvironmentVariable('MAVEN_HOME', 'D:\Developer\node_modules\apache-maven-3.9.9', 'User')
Write-Host "Set MAVEN_HOME = D:\Developer\node_modules\apache-maven-3.9.9"

# 2. Add to PATH
$oldPath = [Environment]::GetEnvironmentVariable('Path', 'User')
$mavenBin = 'D:\Developer\node_modules\apache-maven-3.9.9\bin'

if ($oldPath -notlike "*$mavenBin*") {
    $newPath = "$oldPath;$mavenBin"
    [Environment]::SetEnvironmentVariable('Path', $newPath, 'User')
    Write-Host "Added Maven to PATH"
} else {
    Write-Host "Maven already in PATH"
}

Write-Host ""
Write-Host "Done! Please close all terminal windows and reopen to apply changes."
Write-Host "After reopening, run 'mvn -v' to verify."
