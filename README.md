# SauceLabs coding challenge

This application is a daemon that monitors a service at HTTP port 12345 on your local machine.

## Usage

You need to have JDK >= 8 installed.
Build the daemon by running

    ./mvnw verify

This will create an executable JAR `target/...`. Now you can run
the daemon by

    `java -jar target/...

The daemon writes its message to a file `daemon.log` at your current directory.

