const path = require('path');
//import Promise from 'bluebird';
const Promise = require('bluebird');
const db = require('sqlite');

db.open(path.join(__dirname, '../kinase.db'), { Promise });

/**
 * Returns a list of compound names, with a filter optionally applied.
 *
 * @param filter
 * @param offset
 * @param limit
 * @returns {Promise.<*>}
 */
const getCompoundsByName = async (filter, offset, limit) => {

    let query = 'select distinct compound_nm from data_report';
    let countQuery = 'select count(distinct compound_nm) as count from data_report';

    if (filter) {
        const whereClause = ' where compound_nm like ?';
        query += whereClause;
        countQuery += whereClause;
    }

    query += ' limit ? offset ?';
    console.log('Query params: ' + filter);
    console.log('Query:        ' + query);
    console.log('Count query:  ' + countQuery);

    try {

        let data, count;

        if (filter) {
            filter = '%' + filter + '%';
            const statement = await db.prepare(query);
            const countStatement = await db.prepare(countQuery);
            [ data, count ] = await Promise.all([
                statement.all(filter, limit, offset),
                countStatement.get(filter)
            ]);
        }
        else {
            const statement = await db.prepare(query);
            const countStatement = await db.prepare(countQuery);
            [ data, count ] = await Promise.all([
                statement.all(limit, offset),
                countStatement.get()
            ]);
        }
        //statement.finalize();
        return { data, count: count.count };
    } catch (e) {
        console.error('An error occurred fetching data: ' + e);
        return [];
    }
};

const createLikeParam = (like) => {
    return '%' + like + '%';
};

/**
 * The response format here is that expected by semantic-ui.
 * @returns {Promise.<*>}
 */
const getKinases = async (filter) => {

    try {

        let query = 'select distinct(discoverx_gene_symbol) from data_report ';
        if (filter) {
            query += 'where discoverx_gene_symbol like ?';
        }
        query += 'order by discoverx_gene_symbol';
        const statement = await db.prepare(query);

        const promise = filter ? statement.all(filter + '%') : statement.all();
        const [ data ] = await Promise.all([ promise ]);
        //statement.finalize();

        const response = {
            success: true,
            results: data
        };
        response.results = response.results.map((record) => { return { name: record.discoverx_gene_symbol, value: record.discoverx_gene_symbol }; });
        return response;
    } catch (e) {
        console.error('An error occurred fetching kinase names: ' + e);
        return { success: false };
    }
};

/**
 * Returns records filtered by inhibitor or kinase+activity.  All 3 together is not yet supported.
 * Current bugs:
 *    1. If none of the three are specified, an error occurs
 *    2. This is extra verbose because I can't get Statement.all.apply(this, ...) to work
 *
 * @param queryParams Query parameters, possibly "inhibitor", "kinase" and "activity", or all three.
 * @param compoundNamesOnly Whether to return only distinct compound names, or all data (including
 *        multiple records per compound).
 * @param offset The first record to retrieve.
 * @param limit The maximum number of records to retrieve.
 * @returns {Promise.<*>}
 */
const getCompoundsMatching = async (queryParams, compoundNamesOnly, offset, limit) => {

    const searchFor = compoundNamesOnly ? 'distinct(compound_nm)' : '*';
    let query = `select ${searchFor} from data_report`;
    let countQuery = `select count(${searchFor}) as count from data_report`;

    let whereClauseDefined = false;
    if (queryParams.inhibitor) {
        const whereClausePart = ' where compound_nm like ?';
        query += whereClausePart;
        countQuery += whereClausePart;
        whereClauseDefined = true;
    }
    else if (queryParams.kinase) {

        const whereClausePart = (whereClauseDefined ? ' and' : ' where') + ' discoverx_gene_symbol like ?' +
            ' and percent_control < ?';
        query += whereClausePart;
        countQuery += whereClausePart;
        whereClauseDefined = true;
    }

    query += ' limit ? offset ?';
    console.log('Query params: ' + JSON.stringify(queryParams));
    console.log('Query:        ' + query);
    console.log('Count query:  ' + countQuery);

    try {

        const statement = await db.prepare(query);
        const countStatement = await db.prepare(countQuery);

        const inhibitor = createLikeParam(queryParams.inhibitor);
        const kinase = createLikeParam(queryParams.kinase);
        const activity = queryParams.activity || 20;

        // For now, we support either inhibitor or kianse/activity.  Should support both
        const queries = [];
        if (queryParams.inhibitor) {
            queries.push(statement.all(inhibitor, limit, offset),
                countStatement.get(inhibitor));
        }
        else if (queryParams.kinase) {
            queries.push(statement.all(kinase, activity, limit, offset),
                countStatement.get(kinase, activity));
        }
        else {
            queries.push(statement.all(limit, offset),
                countStatement.get());
        }

        const [ data, count ] = await Promise.all(queries);
        //statement.finalize();
        return { data, count: count.count };
    } catch (e) {
        console.error('An error occurred fetching data: ' + e);
        return [];
    }
};

module.exports = {
    getCompoundsByName: getCompoundsByName,
    getCompoundsMatching: getCompoundsMatching,
    getKinases: getKinases
};
