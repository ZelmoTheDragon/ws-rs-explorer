FROM alpine:3.16.2 AS happi-explorer-glassfish

ARG GLASSFISH_SERVER="glassfish-6.2.5.zip"
ARG APPLICATION="glassfish-runtime-0.0.11-SNAPSHOT.war"

COPY "target/$GLASSFISH_SERVER" "/tmp"
COPY "target/$APPLICATION" "/tmp"

ENV GF_USER="duke"
ENV GF_HOME="/opt/glassfish6"
ENV PATH="$PATH:$GF_HOME/bin"

# TODO: Optimize Glassifh internal configuration...
RUN \
 apk update &&\
 apk upgrade &&\
 apk add --no-cache openjdk17-jre-headless &&\
 addgroup --system "$GF_USER" &&\
 adduser --system --disabled-password --no-create-home --home="$GF_HOME" --ingroup="$GF_USER" "$GF_USER" &&\
 unzip "/tmp/$GLASSFISH_SERVER" -d "/opt" &&\
 mv "/tmp/$APPLICATION" "$GF_HOME/glassfish/domains/domain1/autodeploy" &&\
 chown -R "$GF_USER":"$GF_USER" "$GF_HOME" &&\
 chmod u+x "$GF_HOME/bin/asadmin" &&\
 rm "/tmp/$GLASSFISH_SERVER"

WORKDIR "$GF_HOME"
USER "$GF_USER"
EXPOSE 8080 8181 4848
CMD ["asadmin", "start-domain", "--verbose"]
