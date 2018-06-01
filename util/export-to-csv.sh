#!/bin/bash
#
# Exports data from our database to CSV
# See env.sh for connection info
#

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
OUTDIR="../exported-data" # This can't be an absolute path, at least on Windows

if [ ! -f ${DIR}/env.sh ]; then
    echo "./env.sh not found.  Please create it to continue"
    exit 1
fi
. ${DIR}/env.sh

rm -fr ${OUTDIR}
mkdir ${OUTDIR}

PGPASSWORD=${PASSWORD} psql -h ${HOST} -U ${USER} -c "\copy (select * from ${SCHEMA}.compound order by compound_nm) to ${OUTDIR}/compound.csv with csv header" ${DB}
PGPASSWORD=${PASSWORD} psql -h ${HOST} -U ${USER} -c "\copy (select * from ${SCHEMA}.kinase order by id) to ${OUTDIR}/kinase.csv with csv header" ${DB}
PGPASSWORD=${PASSWORD} psql -h ${HOST} -U ${USER} -c "\copy (select * from ${SCHEMA}.kinase_activity_profile order by id) to ${OUTDIR}/kinase_activity_profile.csv with csv header" ${DB}
PGPASSWORD=${PASSWORD} psql -h ${HOST} -U ${USER} -c "\copy (select * from ${SCHEMA}.blog_post order by blog_post_id) to ${OUTDIR}/blog_post.csv with csv header" ${DB}
PGPASSWORD=${PASSWORD} psql -h ${HOST} -U ${USER} -c "\copy (select * from ${SCHEMA}.feedback order by feedback_id) to ${OUTDIR}/feedback.csv with csv header" ${DB}
PGPASSWORD=${PASSWORD} psql -h ${HOST} -U ${USER} -c "\copy (select * from ${SCHEMA}.partner order by id) to ${OUTDIR}/partner.csv with csv header" ${DB}
