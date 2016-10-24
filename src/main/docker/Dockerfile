FROM registry.aliyuncs.com/leansw/java-base
EXPOSE 9096
VOLUME /tmp
COPY lib /lean/java/lib
ADD entrypoint.sh /entrypoint.sh
ENTRYPOINT sh /entrypoint.sh
ADD leansw-gocd-dynamic-agent-manager-*-SNAPSHOT.jar /leansw-gocd-dynamic-agent-manager.jar
RUN touch /leansw-gocd-dynamic-agent-manager.jar && \
    touch /lean/java/lib/*.jar && \
    chmod +x /entrypoint.sh