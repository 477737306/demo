FROM testsource
# FROM fabric8/java-jboss-openjdk8-jdk 建议生产使用，ref: http://blog.tenxcloud.com/?p=1894
# FROM iron/java:1.8

ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8

# Prepare by downloading dependencies
COPY ./target/testing.jar /home/

# VOLUME ["/home/logs"]


# EXPOSE 8088 20880

CMD ["java", "-Djava.security.egd=file:/dev/./urandom","-Duser.timezone=GMT+08", "-Xdebug","-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5580", "-jar", "/home/testing.jar"]
