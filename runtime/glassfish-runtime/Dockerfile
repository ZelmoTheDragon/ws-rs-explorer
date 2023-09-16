FROM alpine:3.18.3 AS happi-explorer-glassfish

ARG GLASSFISH_SERVER="glassfish-7.0.8.zip"
ARG APPLICATION="glassfish-runtime-0.0.17-SNAPSHOT.war"

COPY "target/$GLASSFISH_SERVER" "/tmp"
COPY "target/$APPLICATION" "/tmp"

ENV GLASSFISH_USER="duke"
ENV GLASSFISH_HOME="/opt/glassfish7"
ENV PATH="$PATH:$GLASSFISH_HOME/bin"

# TODO: Optimize Glassifh internal configuration...
RUN \
 apk update &&\
 apk upgrade &&\
 apk add --no-cache openjdk17-jre-headless &&\
 addgroup --system "$GLASSFISH_USER" &&\
 adduser --system --disabled-password --no-create-home --home="$GLASSFISH_HOME" --ingroup="$GLASSFISH_USER" "$GLASSFISH_USER" &&\
 unzip "/tmp/$GLASSFISH_SERVER" -d "/opt" &&\
 mv "/tmp/$APPLICATION" "$GLASSFISH_HOME/glassfish/domains/domain1/autodeploy" &&\
 chown -R "$GLASSFISH_USER":"$GLASSFISH_USER" "$GLASSFISH_HOME" &&\
 chmod u+x "$GLASSFISH_HOME/bin/asadmin" &&\
 rm "/tmp/$GLASSFISH_SERVER"

WORKDIR "$GLASSFISH_HOME"
USER "$GLASSFISH_USER"
EXPOSE 8080 8181 4848
CMD ["asadmin", "start-domain", "--verbose"]
