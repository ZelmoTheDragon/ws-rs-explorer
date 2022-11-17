FROM alpine:3.16.2 AS happi-explorer-glassfish

ARG GLASSFISH_VERSION="6.2.5"
ARG PROJECT_VERSION="0.0.10-SNAPSHOT"

COPY target/glassfish-${GLASSFISH_VERSION}.zip /tmp
COPY target/happi-explorer-example-${PROJECT_VERSION}.war /tmp

ENV GF_USER="duke"
ENV GF_HOME="/opt/glassfish6"
ENV PATH="$PATH:$GF_HOME/bin"

# TODO: Optimize Glassifh internal configuration...
RUN \
 apk add openjdk17-jre-headless &&\
 addgroup --system "$GF_USER" &&\
 adduser --system --disabled-password --no-create-home --home="$GF_HOME" --ingroup="$GF_USER" "$GF_USER" &&\
 unzip /tmp/glassfish-6.2.5.zip -d /opt/ &&\
 mv /tmp/happi-explorer-example-${PROJECT_VERSION}.war "$GF_HOME/glassfish/domains/domain1/autodeploy" &&\
 chown -R "$GF_USER":"$GF_USER" "$GF_HOME" &&\
 rm /tmp/glassfish-${GLASSFISH_VERSION}.zip

USER "$GF_USER"
EXPOSE 8080 8181 4848
WORKDIR "$GF_HOME"
CMD ["asadmin", "start-domain", "--verbose"]
