FROM openjdk

WORKDIR /app

COPY target/payroll.jar payroll.jar

ENTRYPOINT ["java", "-jar", "payroll.jar"]

EXPOSE 8080