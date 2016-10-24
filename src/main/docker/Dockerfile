FROM registry.aliyuncs.com/leansw/java-base
EXPOSE 9096
VOLUME /tmp
COPY lib /lean/java/lib
ADD entrypoint.sh /entrypoint.sh
ENTRYPOINT sh /entrypoint.sh
ADD gocd-dynamic-agent-manager-*-SNAPSHOT.jar /gocd-dynamic-agent-manager.jar
RUN touch /gocd-dynamic-agent-manager.jar && \
    touch /lean/java/lib/*.jar && \
    chmod +x /entrypoint.sh