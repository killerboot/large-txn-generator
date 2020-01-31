FROM alfresco/alfresco-content-repository:latest

COPY alfresco-large-txn-generator-1.0-SNAPSHOT.jar /usr/local/tomcat/webapps/alfresco/WEB-INF/lib
RUN chgrp Alfresco /usr/local/tomcat/webapps/alfresco/WEB-INF/lib/alfresco-large-txn-generator-1.0-SNAPSHOT.jar && \
    chmod g+r /usr/local/tomcat/webapps/alfresco/WEB-INF/lib/alfresco-large-txn-generator-1.0-SNAPSHOT.jar
