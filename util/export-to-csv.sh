#!/bin/bash
#
# Exports data from our database to CSV
# See env.sh for connection info
#

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
OUTDIR="${DIR}/../exported-data"

if [ ! -f ${DIR}/env.sh ]; then
    echo "./env.sh not found.  Please create it to continue"
    exit 1
fi
. ${DIR}/env.sh

rm -fr ${OUTDIR}
mkdir ${OUTDIR}

psql -U ${USER} -c "\copy (select * from ${SCHEMA}.compound order by compound_nm) to ${OUTDIR}/compound.csv with csv" ${DB}
psql -U ${USER} -c "\copy (select * from ${SCHEMA}.kinase order by discoverx_gene_symbol) to ${OUTDIR}/kinase.csv with csv" ${DB}
psql -U ${USER} -c "\copy (select * from ${SCHEMA}.kinase_activity_profile order by compound_nm, kinase) to ${OUTDIR}/kinase_activity_profile.csv with csv" ${DB}
