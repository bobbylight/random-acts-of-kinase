#!/bin/bash

OUTFILE=/tmp/add-new-kinases.sh

echo -e "#!/bin/bash\n" > $OUTFILE

cat "/users/robert/Downloads/nanosyn kinase list.csv" | tr -d '\r' | while read line ; do

    NANOSYN=`echo ${line} | cut -f1 -d','`
    DISCOVERX=`echo ${line} | cut -f2 -d','`
    ENTREZ=`echo ${line} | cut -f3 -d','`

    if [[ -z ${DISCOVERX} ]] ; then
        echo "insert into kinase (discoverx_gene_symbol, entrez_gene_symbol, nanosyn_gene_symbol, discoverx_url) \
 values (NULL, '$ENTREZ', '$NANOSYN', NULL);" >> $OUTFILE
    else
        echo "update kinase set nanosyn_gene_symbol = '$NANOSYN' \
 where discoverx_gene_symbol = '$DISCOVERX' and entrez_gene_symbol = '$ENTREZ';" >> $OUTFILE
    fi
done


cat $OUTFILE