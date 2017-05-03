'use strict';

const express = require('express');
//const livereload = require('express-livereload');
const app = express();

const compression = require('compression');
app.use(compression());

const fs = require('fs');
const path = require('path');
const async = require('async');

// A quick test verifying DB connectivity
const sqlite3 = require('sqlite3').verbose();
const db = new sqlite3.Database(path.join(__dirname, './kinase.db'));
const data = [];
db.serialize(() => {

    db.each('select * from data_report limit 10;', (err, row) => {
        console.log(JSON.stringify(row));
        data.push(row);
    });
});
db.close();

const createDeckMetadata = () => {

    return {
        one: 'one',
        boolean: true,
        number: 42,
        data: data
    };
};

app.get('/api/decks', (req, res) => {
    res.type('application/json');
    res.json(createDeckMetadata());
});

app.use(express.static(path.join(__dirname, '../../dist')));

// Single-page app; always route to index.html for non-static content URLs
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, '../../dist/index.html'));
});

app.listen(process.env.PORT || 8080);
//livereload(app);
