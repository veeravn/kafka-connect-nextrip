FROM confluentinc/cp-kafka-connect:5.3.1

ENV CONNECT_PLUGIN_PATH="/usr/share/java,/usr/share/confluent-hub-components"

COPY build/distributions/kafka-connect-nextrip.zip /hub-downloads/

RUN \
    mkdir -p /spooldir/input && \
    mkdir -p /spooldir/error && \
    mkdir -p /spooldir/finished

#    rm -fr /usr/share/java/kafka-connect-elasticsearch && \

RUN \
    rm -fr /usr/share/java/kafka-connect-activemq && \
    rm -fr /usr/share/java/kafka-connect-ibmmq && \
    rm -fr /usr/share/java/kafka-connect-jdbc && \
    rm -fr /usr/share/java/kafka-connect-jms && \
    rm -fr /usr/share/java/kafka-connect-s3 && \
    confluent-hub install --no-prompt /hub-downloads/kafka-connect-nextrip.zip

#    confluent-hub install --no-prompt jcustenborder/kafka-connect-spooldir:latest
