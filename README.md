# Random Acts of Kinase

[![Build Status](https://travis-ci.org/bobbylight/random-acts-of-kinase.svg?branch=master)](https://travis-ci.org/bobbylight/random-acts-of-kinase)
[![Coverage Status](https://coveralls.io/repos/github/bobbylight/random-acts-of-kinase/badge.svg?branch=master)](https://coveralls.io/github/bobbylight/random-acts-of-kinase?branch=master)

A web frontend for SGC kinase information.

A Spring Boot application exposes the data via a REST API.  The frontend is written in Vue.

Note:  The actual kinase data is not included in this repository, for obvious reasons.
You can still test the application without this data by running it with the `dev` Spring profile.

## Hacking
You can omit the `-xcreateSmileSvgs` options if you actually have openbabel installed
and configured properly in your environment.  If you don't, you need to use this option to
omit that build task to prevent a build failure:

```sh
git clone https://github.com/bobbylight/random-acts-of-kinase.git
./gradlew build -xwebpack --warning-mode all -xcreateSmileSvgs
./gradlew bootRun -xwebpack -xcreateSmileSvgs # Starts application at localhost:8080
./gradlew webpackWatch      # In another window, run webpack watch for UI updates
./gradlew copyStaticResourcesToBuildWatch # Copy webpack build into build/ for hot deploys
```

Unfortunately you need to start three processes to develop - annoying.  I couldn't get
webpack to build directly into `build/` without Spring Boot getting cranky and clearing
out the contents of `build/resources/main/static` on restarts.

*Note:* Ctrl+C may not propagate the SIGKILL to the child npm tasks for `webpackWatch`
and `copyStaticResourcesToBuildWatch`, which can cause race conditions on file copies
and in turn weird behavior (stale static resources).  If you see this happening, you
might consider running the wrapped npm tasks directly.  See
[here](https://github.com/srs/gradle-node-plugin/issues/143) for more information.

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
./gradlew clean build --warning-mode all # Be sure to perform a production build
./gradlew makeAwsArchive
eb deploy --label "something"
```

## Utilities

The `util` folder contains shell scripts to do mundane tasks.  For these scripts
to run you'll need to create a file named `env.sh` from the `env.sh.orig` template
file with connection info for a database containing the SGC data.

### Creating SVG images for compounds from SMILES strings
The SVG images of compounds used by the application are generated from their
SMILES strings.  Generate them by running `./create-smiles-svgs.sh`.  You'll
need to have `psql` on your `PATH` or this script will fail silently.  This
script is run as part of `./gradlew build` so you typically don't need to
run it directly.

### Exporting data from our database into CSV files
To back up the database into CSV files, you can run `./export-to-csv.sh`.


## Updating the AWS database
We won't have to keep doing this once we have the data exported in a format we like...
```sh
psql --host=<host-name> --username=<user> --dbname=<db> -f create-db.ddl
```
