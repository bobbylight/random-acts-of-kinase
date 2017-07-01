const path = require('path');
//import Promise from 'bluebird';
const Promise = require('bluebird');
const db = require('sqlite');

db.open(path.join(__dirname, '../kinase.db'), { Promise });

const createLikeParam = (like) => {
    return '%' + like + '%';
};

const createSelectClause = (searchFields) => {
    return `select ${searchFields} from data_report d left join kd_report kd on ` +
            'd.compound_nm = kd.compound_nm and d.discoverx_gene_symbol = kd.discoverx_gene_symbol ' +
            'left join s_10 s10 on d.compound_nm = s10.compound_nm'
        ;
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

    const searchFor = compoundNamesOnly ? 'distinct(d.compound_nm), s10.chemotype, s10.s_10' :
        'd.compound_nm, d.discoverx_gene_symbol, d.entrez_gene_symbol, d.percent_control, d.compound_concentration_nm, ' +
        'kd.modifier, kd.kd_nm, ' +
        's10.chemotype, s10.s_10';
    let query = createSelectClause(searchFor);

    let whereClauseDefined = false;
    if (queryParams.inhibitor) {
        const whereClausePart = ' where d.compound_nm like ?';
        query += whereClausePart;
        whereClauseDefined = true;
    }
    else if (queryParams.kinase) {

        const whereClausePart = (whereClauseDefined ? ' and' : ' where') + ' d.discoverx_gene_symbol like ?' +
            ' and d.percent_control < ?';
        query += whereClausePart;
        whereClauseDefined = true;
    }

    const orderBy = queryParams.order || 'compound_nm:asc';
    orderBy.split(',').forEach((orderByPart, index) => {

        if (index === 0) {
            query += ' order by ';
        }
        else {
            query += ', ';
        }
        const [ orderCol, orderDir ] = orderByPart.split(':');

        // TODO: Do this better
        if (orderCol === 'kd_nm') {
            query += 'case when kd.kd_nm is null then 1 else 0 end, ';
        }
        if (orderCol === 'modifier' || orderCol === 'kd_nm') {
            query += 'kd.';
        }
        else {
            query += 'd.';
        }

        query += orderCol + ' ' + orderDir;
    });

    const countQuery = `select count(1) as count from (${query})`;

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
    getCompoundsMatching: getCompoundsMatching,
    getKinases: getKinases
};
