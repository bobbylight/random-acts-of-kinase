#!/bin/bash
#
# Imports data previously exported into CSV files into our database.
# See env.sh for connection info
#

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
OUTDIR="../exported-data" # This can't be an absolute path, at least on Windows

if [ ! -f ${DIR}/env.sh ]; then
    echo "./env.sh not found.  Please create it to continue"
    exit 1
fi
. ${DIR}/env.sh


cd ${DIR}
PGPASSWORD=${PASSWORD} psql -U ${USER} -f ddl/postgres.sql ${DB}
PGPASSWORD=${PASSWORD} psql -U ${USER} -c "\copy compound from '${OUTDIR}/compound.csv' with csv header" ${DB}
PGPASSWORD=${PASSWORD} psql -U ${USER} -c "\copy kinase from '${OUTDIR}/kinase.csv' with csv header" ${DB}
PGPASSWORD=${PASSWORD} psql -U ${USER} -c "\copy kinase_activity_profile from '${OUTDIR}/kinase_activity_profile.csv' with csv header" ${DB}
