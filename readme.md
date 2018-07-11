# Sample Task

### Run tests
    mvn test

### Build project
    mvn clean install

### Create docker image
    docker build -t sampletask .

### Run docker image
    docker run -p 8080:8080 sampletask
    
### Run without docker image
    java -jar target/sampletask-0.0.1-SNAPSHOT.jar