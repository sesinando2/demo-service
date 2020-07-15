telepresence --swap-deployment $1 --run mvn spring-boot:run -Dspringboot-run.profiles=template -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=*:5005"
