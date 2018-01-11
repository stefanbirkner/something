# SauceLabs coding challenge

[![Build Status](https://travis-ci.org/stefanbirkner/something.svg?branch=master)](https://travis-ci.org/stefanbirkner/something)


This application is a daemon that monitors a service at HTTP port 12345 on your local machine.

## Usage

You need to have JDK >= 8 installed.
Build the daemon by running

    ./mvnw verify

This will create an executable JAR `target/monitor-0.0.0-SNAPSHOT.jar`. Now you can run
the daemon by

    java -cp "target/monitor-0.0.0-SNAPSHOT.jar:okhttp-3.9.1.jar:okio-1.13.0.jar" Monitor <path/to/log/file>

The log file must already exist. The daemon writes its message to the specified log file. It writes the following events:

- Monitor has started: `DEBUG Monitor started`
- Server is healthy (it returns 200): `INFO Server is healthy`
- Server returns wrong status code: `ERROR Server returned status code 500`
- Cannot connect to server: `ERROR Failed to connect to server`

## Development Guide

The daemon is build with [Maven](http://maven.apache.org/). If you
want to contribute code than

* Please write a test for your change.
* Ensure that you didn't break the build by running `./mvnw verify`.
* Fork the repo and create a pull request. (See [Understanding the GitHub Flow](https://guides.github.com/introduction/flow/index.html))

The basic coding style is described in the
[EditorConfig](http://editorconfig.org/) file `.editorconfig`.

The daemon supports [Travis CI](https://travis-ci.org/) for
continuous integration. Your pull request will be automatically build by Travis
CI.

