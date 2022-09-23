FROM maven:3.8.6-openjdk-11-slim
COPY ./repository /root/.m2/repository
RUN apt-get update && \
    apt-get install -y -qq --no-install-recommends git && \
    apt-get clean -y && \
    apt-get autoremove -y && \
    rm -rfv /var/lib/apt/lists/* /tmp/* /var/tmp/*

