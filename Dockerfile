FROM tomcat:latest

WORKDIR /usr/local/tomcat/webapps/

COPY deploy/MovieReview-8.5.war ROOT.war

EXPOSE 8080