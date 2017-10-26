# Random Acts of Kinase

[![Build Status](https://travis-ci.org/bobbylight/rak.svg?branch=master)](https://travis-ci.org/bobbylight/rak)

This is very rough at the moment.

## Install

```sh
git clone git@github.com/github.com/bobbylight/rak.git
cd rak/src/main/frontend
# "npm install" runs semantic-ui stuff that's buggy, be prepared
export CLI_WIDTH=80 # Work around bug in a dependency of semantic-ui's build, should be exactly right
npm install # Select 'Skip install', then *use arrow keys at least once* to select Yes or No
npm run build-semantic-ui  # Create our custom semantic-ui build
cd ../../..
./gradlew build
./gradlew bootRun      # Starts application at localhost:8080
./gradlew webpackWatch # In another window, run webpack watch for UI updates
./gradlew copyStaticResourcesToBuildWatch # Copy webpack build into build/ for hot deploys
```

Unfortunately you need to start three processes to develop - annoying.  I couldn't get
webpack to build directly into `build/` without Spring Boot getting cranky and clearing
out the contents of `build/resources/main/static` on restarts.

The server is a simple Spring Boot application exposing a REST API and a little
static content.  The client is Vue/JS/TypeScript.

The application will be hosted at [http://localhost:8080]().

To run tests:
```sh
./gradlew test   # Service tests
npm run test     # Client tests
npm run coverage # Client tests + coverage report
```

The coverage report for the client-side tests lives here:
```sh
open reports/coverage/index.html
```

To deploy to AWS (we build a zip containing just the jar and a Procfile to launch
it, as configured in `.elasticbeanstalk/config.yml`):
```sh
./gradlew makeAwsArchive
eb deploy [--staged]
```

## Updating the AWS database
We won't have to keep doing this once we have the data exported in a format we like...
```sh
psql --host=<host-name> --username=<user> --dbname=<db> -f create-db.ddl
```
