# Get the script directory
$SCRIPT_DIR = Split-Path $script:MyInvocation.MyCommand.Path
# Export the rabbitmq sbin path
. (Join-Path -Path $SCRIPT_DIR -ChildPath "export-rabbitmq.ps1")

# Users & Vhosts
$Usernames = @("galSmsService", "galEmailService", "galResourcesService", "galUsersService", "galHomesService", "galTestService")
$Vhosts = @("/galsie-development", "/galsie-testing", "/galsie-production")
$DefaultUserPwd = "galsie123"
$Node = "galsie-node1@localhost"

# get existing users so that we don't recreate
$ExistingUsers = rabbitmqctl -n $Node list_users
$ExistingVhosts = rabbitmqctl -n $Node list_vhosts

Write-Host "=-=-=-=-=-=ADDING USERS=-=-=-=-=-="
foreach ($User in $Usernames) {
   if (!($ExistingUsers -match "^$User\s")) {
      rabbitmqctl -n $Node add_user $User $DefaultUserPwd
      Write-Host "Added the user $User"
   } else {
      Write-Host "User $User already exists, won't add"
   }
}

Write-Host "=-=-=-=-=-=Setting up VHOSTS=-=-=-=-=-="
foreach ($Vhost in $Vhosts) {
    Write-Host "`n=-=-Checking $Vhost=-=-"
    if (!($ExistingVhosts -match "^$Vhost$")) {
        rabbitmqctl -n $Node add_vhost "$Vhost"
        Write-Host "Added vhost $Vhost"
    } else {
        Write-Host "Vhost $Vhost already exists, won't add"
    }

    foreach ($User in $Usernames) {
        rabbitmqctl -n $Node set_permissions -p "$Vhost" "$User" ".*" ".*" ".*"
        Write-Host "`tGranted access to '$User'"
    }
}

Write-Host "Done."
