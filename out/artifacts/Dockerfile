FROM tomcat:jdk21-openjdk

COPY RieltorApp_jar /home/app/RieltorApp_jar

WORKDIR /app

EXPOSE 8080

ENTRYPOINT ["java","-jar","/home/app/RieltorApp_jar/RieltorApp.jar"]