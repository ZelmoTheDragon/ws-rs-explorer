FROM alpine:3.20.0 AS happi-explorer-tomcat

ARG TOMCAT_SERVER="tomcat-10.1.24.zip"
ARG APPLICATION="tomcat-runtime-0.1.0-SNAPSHOT.war"

COPY "target/$TOMCAT_SERVER" "/tmp"
COPY "target/$APPLICATION" "/tmp"

ENV TOMCAT_USER="duke"
ENV TOMCAT_HOME="/opt/apache-tomcat-10.1.24"
ENV PATH="$PATH:$TOMCAT_HOME/bin"

# TODO: Optimize Tomcat internal configuration...
RUN \
 apk update &&\
 apk upgrade &&\
 apk add --no-cache openjdk21-jre-headless &&\
 addgroup --system "$TOMCAT_USER" &&\
 adduser --system --disabled-password --no-create-home --home="$TOMCAT_HOME" --ingroup="$TOMCAT_USER" "$TOMCAT_USER" &&\
 unzip "/tmp/$TOMCAT_SERVER" -d "/opt" &&\
 mv "/tmp/$APPLICATION" "$TOMCAT_HOME/webapps" &&\
 chown -R "$TOMCAT_USER":"$TOMCAT_USER" "$TOMCAT_HOME" &&\
 chmod u+x "$TOMCAT_HOME/bin/catalina.sh" &&\
 rm "/tmp/$TOMCAT_SERVER"

WORKDIR "$TOMCAT_HOME"
USER "$TOMCAT_USER"
EXPOSE 8080
CMD ["catalina.sh", "run"]
