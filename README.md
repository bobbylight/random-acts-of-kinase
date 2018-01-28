# Random Acts of Kinase
[![Build Status](https://travis-ci.org/bobbylight/rak.svg?branch=master)](https://travis-ci.org/bobbylight/rak)
[![Coverage Status](https://coveralls.io/repos/github/bobbylight/rak/badge.svg?branch=master)](https://coveralls.io/github/bobbylight/rak?branch=master)

A web frontend for SGC kinase information.

A Spring Boot application exposes the data via a REST API.  The frontend is written in Vue.

Note:  The actual kinase data is not included in this repository, for obvious reasons.
You can still test the application without this data by running it with the `dev` Spring profile.

## Install

```sh
git clone git@github.com/github.com/bobbylight/rak.git
cd rak/src/main/frontend
# "npm install" runs semantic-ui stuff that's buggy, be prepared
export CLI_WIDTH=80 # Work around bug in a dependency of semantic-ui's build, should be exactly right
npm install # Select 'Skip install', then *use arrow keys at least once* to select Yes or No
npm run build-semantic-ui  # Create our custom semantic-ui build
cd ../../..
./gradlew build -xwebpack
./gradlew bootRun         # Starts application at localhost:8080
./gradlew webpackWatch    # In another window, run webpack watch for UI updates
./gradlew copyStaticResourcesToBuildWatch # Copy webpack build into build/ for hot deploys
```

Unfortunately you need to start three processes to develop - annoying.  I couldn't get
webpack to build directly into `build/` without Spring Boot getting cranky and clearing
out the contents of `build/resources/main/static` on restarts.

*Note:* Ctrl+C may not propagate the SIGKILL to the child npm tasks for
`webpackWatch` and `copyStaticResourcesToBuildWatch`, which can cause race conditions on
file copies and in turn weird behavior (stale static resources).  If you see this happening, you
might consider running the wrapped npm tasks directly.

If you have data to develop against in a local postgres instance, you can run against
via `./gradlew bootRun -Dspring.profiles.active=dev-postgres`.

The server is a simple Spring Boot application exposing a REST API and a little
static content.  The client is Vue/JS/TypeScript.

The application will be hosted at [http://localhost:8080]().

To run tests:
```sh
./gradlew test -xwebpack # Service tests
npm test                 # Client tests
npm run coverage         # Client tests + coverage report
```

The coverage report for the client-side tests lives here:
```sh
open reports/coverage/index.html
```

To deploy to AWS (we build a zip containing just the jar and a Procfile to launch
it, as configured in `.elasticbeanstalk/config.yml`):
```sh
./gradlew makeAwsArchive
eb deploy [--staged] --label "something"
```

## Updating the AWS database
We won't have to keep doing this once we have the data exported in a format we like...
```sh
psql --host=<host-name> --username=<user> --dbname=<db> -f create-db.ddl
```
