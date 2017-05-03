# Random Acts of Kinase

This is very rough at the moment.

## Install

```sh
git clone git@github.com/github.com/bobbylight/rak.git
cd rak
npm install
# Manually copy kinase db into ./src/server/
npm run watch  # Start webpack, builds client stuff into dist/
npm run start  # Starts node server on port 8080
```

The node server serves both static content and a simple REST API to fetch the
kinase data.

The client is all TypeScript, but the server is all vanilla JS.

The application will be hosted at [http://localhost:8080]().

To run tests:
```sh
npm run test     # Tests only
npm run coverage # Generates coverage report
```

Running the tests generates a coverage report:
```sh
open reports/coverage/index.html
```

To do a production (minified) build into `dist/`:
```sh
npm install
npm run build
```

## ...
This app is based off of [ts-webpack-app-template](github.com/bobbylight/ts-webpack-app-template.git).
