Simple JMX client

To build project run ./gradlew shadowJar

Start with params host:port (where the client will be listening for connections)

Ex. 
```
java -jar jmxclient-1.0-SNAPSHOT-all.jar localhost 9000
```

Open telnet to port (ex. 9000)

Send jmx host and port followed by \n 
Ex. 
```
localhost:9010 \n
```
Send objectname@attribute\n 
Ex.
```
 java.lang:type=OperatingSystem@Arch\n
```