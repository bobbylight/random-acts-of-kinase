#!/bin/bash
#
# Imports data previously exported into CSV files into our database.
# See env.sh for connection info
#

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
OUTDIR="../../kinase-data/exported-data" # This can't be an absolute path, at least on Windows

if [ ! -f ${DIR}/env.sh ]; then
    echo "./env.sh not found.  Please create it to continue"
    exit 1
fi
. ${DIR}/env.sh


cd ${DIR}
PGPASSWORD=${PASSWORD} psql -h ${HOST} -p ${PORT} -U ${USER} -v schema=${SCHEMA} -f ddl/postgres.sql ${DB}
PGPASSWORD=${PASSWORD} psql -h ${HOST} -p ${PORT} -U ${USER} -c "\copy ${SCHEMA}.compound from '${OUTDIR}/compound.csv' with csv header" ${DB}
PGPASSWORD=${PASSWORD} psql -h ${HOST} -p ${PORT} -U ${USER} -c "\copy ${SCHEMA}.kinase from '${OUTDIR}/kinase.csv' with csv header" ${DB}
PGPASSWORD=${PASSWORD} psql -h ${HOST} -p ${PORT} -U ${USER} -c "\copy ${SCHEMA}.kinase_activity_profile from '${OUTDIR}/kinase_activity_profile.csv' with csv header" ${DB}
PGPASSWORD=${PASSWORD} psql -h ${HOST} -p ${PORT} -U ${USER} -c "\copy ${SCHEMA}.blog_post from '${OUTDIR}/blog_post.csv' with csv header" ${DB}
PGPASSWORD=${PASSWORD} psql -h ${HOST} -p ${PORT} -U ${USER} -c "\copy ${SCHEMA}.feedback from '${OUTDIR}/feedback.csv' with csv header" ${DB}
PGPASSWORD=${PASSWORD} psql -h ${HOST} -p ${PORT} -U ${USER} -c "\copy ${SCHEMA}.partner from '${OUTDIR}/partner.csv' with csv header" ${DB}
PGPASSWORD=${PASSWORD} psql -h ${HOST} -p ${PORT} -U ${USER} -v schema=${SCHEMA} -f ddl/postgres-fix-sequences.sql ${DB}
