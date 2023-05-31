FROM alpine:3.17.1 AS happi-explorer-wildfly

ARG WILDFLY_SERVER="wildfly-28.0.1.Final.zip"
ARG APPLICATION="wildfly-runtime-0.0.16-SNAPSHOT.war"

COPY "target/$WILDFLY_SERVER" "/tmp"
COPY "target/$APPLICATION" "/tmp"

ENV WILDFLY_USER="duke"
ENV WILDFLY_HOME="/opt/wildfly-28.0.1.Final"
ENV PATH="$PATH:$WILDFLY_HOME/bin"

# TODO: Optimize Wildfly internal configuration...
RUN \
 apk update &&\
 apk upgrade &&\
 apk add --no-cache openjdk17-jre-headless &&\
 addgroup --system "$WILDFLY_USER" &&\
 adduser --system --disabled-password --no-create-home --home="$WILDFLY_HOME" --ingroup="$WILDFLY_USER" "$WILDFLY_USER" &&\
 unzip "/tmp/$WILDFLY_SERVER" -d "/opt" &&\
 mv "/tmp/$APPLICATION" "$WILDFLY_HOME/standalone/deployments" &&\
 chown -R "$WILDFLY_USER":"$WILDFLY_USER" "$WILDFLY_HOME" &&\
 find "$WILDFLY_HOME/bin/" -name "*.sh" -exec chmod u+x {} + &&\
 rm "/tmp/$WILDFLY_SERVER"

WORKDIR "$WILDFLY_HOME"
USER "$WILDFLY_USER"
EXPOSE 8080 9990
CMD ["standalone.sh"]
