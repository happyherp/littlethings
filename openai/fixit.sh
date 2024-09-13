#!/bin/bash


last_command=$(history | tail -n 2 | head -n 1 | sed 's/^[ ]*[0-9]*[ ]*//')
# Gather information from the system
error_message=$(dmesg | tail -n 1)
operating_system=$(uname -a)
context_details=$(df -h)

#command="ls -l /nonexistent"
command=$last_command

# Run the command and capture stdout and stderr separately
stdout_file=$(mktemp)
stderr_file=$(mktemp)

$command >"$stdout_file" 2>"$stderr_file"
command_output=$(cat "$stdout_file")
command_error=$(cat "$stderr_file")

# Clean up temporary files
rm "$stdout_file" "$stderr_file"

combined="Failed Command: $last_command

Standard Output: 
$command_output

Standard Error: 
$command_error

Error Message: $error_message
Operating System: $operating_system
Additional Context: $context_details"


# Display gathered information
echo "combined: $combined"

add_to_history() {
    local cmd="$1"
    history -s "$cmd"
}