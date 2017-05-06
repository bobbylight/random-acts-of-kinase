'use strict';

const express = require('express');
//const livereload = require('express-livereload');
const app = express();

const compression = require('compression');
app.use(compression());

const fs = require('fs');
const path = require('path');
const async = require('async');

//import Promise from 'bluebird';
const Promise = require('bluebird');
const db = require('sqlite');

db.open(path.join(__dirname, './kinase.db'), { Promise });

const createCompoundMetadata = async (filter, offset, limit) => {

    // const query = 'select * from data_report where compound_nm like \'%' + filter + '%\';';
    // const [ data ] = await Promise.all([ db.all(query) ]);

    try {
        const query = 'select * from data_report where compound_nm like ? limit ? offset ?';
        const countQuery = 'select count(1) as count from data_report where compound_nm like ?';
        console.log('Query: ' + query);
        filter = '%' + filter + '%';
        const statement = await db.prepare(query);
        const countStatement = await db.prepare(countQuery);
        const [ data, count ] = await Promise.all([
            statement.all(filter, limit, offset),
            countStatement.get(filter)
        ]);
        //statement.finalize();
        return { data, count: count.count };
    } catch (e) {
        console.error('An error occurred fetching data: ' + e);
        return [];
    }
};

app.get('/api/compounds', async (req, res) => {

    const start = req.query.offset || 0;
    const limit = req.query.limit || 20;
    const compoundData = await createCompoundMetadata(req.query.filter, start, limit);

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

app.use(express.static(path.join(__dirname, '../../dist')));
app.use(express.static(path.join(__dirname, '../../semantic')));

// Single-page app; always route to index.html for non-static content URLs
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, '../../dist/index.html'));
});

app.listen(process.env.PORT || 8080);
//livereload(app);
