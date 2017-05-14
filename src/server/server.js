'use strict';

const express = require('express');
//const livereload = require('express-livereload');
const app = express();

const history = require('connect-history-api-fallback');
app.use(history());

const compression = require('compression');
app.use(compression());

const fs = require('fs');
const path = require('path');
const async = require('async');

const dao = require('./dao/dao');

app.get('/api/compounds/names', async(req, res) => {

    const start = req.query.offset || 0;
    const limit = req.query.limit || 20;
    const compoundData = await dao.getCompoundsMatching(req.query, true, start, limit);//getCompoundsByName(req.query.filter, start, limit);

    // Our response is built to the DataTables spec:
    // https://datatables.net/manual/server-side
    const response = {
        draw: req.query.draw || 1,
        recordsTotal: compoundData.count, // This isn't really true, but I don't think we need to do another query
        recordsFiltered: compoundData.count,
        data: compoundData.data
    };

    res.type('application/json');
    console.log('... returning: ' + JSON.stringify(response));
    res.json(response);
});

app.get('/api/compounds', async (req, res) => {

    const start = req.query.offset || 0;
    const limit = req.query.limit || 20;
    const compoundData = await dao.getCompoundsMatching(req.query, false, start, limit);

    // Our response is built to the DataTables spec:
    // https://datatables.net/manual/server-side
    const response = {
        draw: req.query.draw || 1,
        recordsTotal: compoundData.count, // This isn't really true, but I don't think we need to do another query
        recordsFiltered: compoundData.count,
        data: compoundData.data
    };

    res.type('application/json');
    console.log('... returning: ' + JSON.stringify(response));
    res.json(response);
});

app.get('/api/kinases', async (req, res) => {

    const kinaseResponse = await dao.getKinases(req.query.filter);

    res.type('application/json');
    console.log('... returning: ' + JSON.stringify(kinaseResponse));
    res.json(kinaseResponse);
});

app.use(express.static(path.join(__dirname, '../../dist')));
app.use(express.static(path.join(__dirname, '../../semantic')));

// Single-page app; always route to index.html for non-static content URLs
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, '../../dist/index.html'));
});
app.get('/compound/*', (req, res) => {
    res.sendFile(path.join(__dirname, '../../dist/index.html'));
});

app.listen(process.env.PORT || 8080);
//livereload(app);
