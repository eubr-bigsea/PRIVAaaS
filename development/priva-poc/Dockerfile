FROM java:8 

LABEL   maintainer  =   "nmsa@dei.uc.pt" 
LABEL   Description =   "Docker image for the LightPrivacyService"
LABEL   Vendor      =   "UC & UNICAMP" 
LABEL   Version     =   "0.3"

# Install maven
RUN apt-get update              && \
    apt-get install -y maven    && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /PRIVAaaS

# Adding third party dependency
COPY install    /PRIVAaaS/install
RUN cd install && sh install.sh

# Adding source, pom.xml
COPY pom.xml    /PRIVAaaS/pom.xml
COPY src        /PRIVAaaS/src

# Maybe this is not needed...
COPY input      /PRIVAaaS/input

# Prepare by downloading dependencies
# RUN ["mvn", "dependency:resolve"]
# RUN ["mvn", "verify"]


# Compile and package into a fat jar
RUN ["mvn", "package"]

EXPOSE 4567

CMD ["/usr/lib/jvm/java-8-openjdk-amd64/bin/java", "-jar", "target/priva-poc-0.3-standalone.jar"]


