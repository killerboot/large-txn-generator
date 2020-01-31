FROM alfresco/alfresco-search-services:1.4.1

ARG NUM_SHARDS
ARG SHARD_ID
ARG SHARDING_METHOD
ARG ALFRESCO_HOSTNAME
ARG SOLR_HOSTNAME

ENV NUM_SHARDS $NUM_SHARDS
ENV SHARD_ID $SHARD_ID
ENV SHARDING_METHOD $SHARDING_METHOD
ENV SOLR_HOSTNAME $SOLR_HOSTNAME
ENV ALFRESCO_HOSTNAME $ALFRESCO_HOSTNAME


# Set Port Number and Sharding ID for this Shard Service
RUN sed -i '/^bash.*/i echo "\nshard.instance=${SHARD_ID}" >> ${DIST_DIR}/solrhome/templates/rerank/conf/solrcore.properties\n' \
        ${DIST_DIR}/solr/bin/search_config_setup.sh && \
    sed -i '/^bash.*/i echo "\nshard.count=${NUM_SHARDS}" >> ${DIST_DIR}/solrhome/templates/rerank/conf/solrcore.properties\n' \
        ${DIST_DIR}/solr/bin/search_config_setup.sh && \
    sed -i '/^bash.*/i sed -i "'"s/shard.method=DB_ID/shard.method=${SHARDING_METHOD}/g"'" ${DIST_DIR}/solrhome/templates/rerank/conf/solrcore.properties\n' \
        ${DIST_DIR}/solr/bin/search_config_setup.sh && \
    sed -i '/^bash.*/i sed -i "'"s/alfresco.host=localhost/alfresco.host=${ALFRESCO_HOSTNAME}/g"'" ${DIST_DIR}/solrhome/templates/rerank/conf/solrcore.properties\n' \
        ${DIST_DIR}/solr/bin/search_config_setup.sh && \
    sed -i '/^bash.*/i sed -i "'"s/solr.host=localhost/solr.host=${SOLR_HOSTNAME}/g"'" ${DIST_DIR}/solrhome/conf/shared.properties\n' \
        ${DIST_DIR}/solr/bin/search_config_setup.sh

