FROM mherwig/alpine-java-mongo

WORKDIR /src

ADD target/sampletask-*.jar /src/sampletask.jar

EXPOSE 8080

CMD ["/bin/bash", "-c" ,"mongod --fork --logpath /var/log/mongod.log && java -jar /src/sampletask.jar"]