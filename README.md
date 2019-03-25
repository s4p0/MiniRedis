# Engineering Redis Challenge
> mini redis implementation challenge

**commands to implement**

- SET​ key value 
- SET​ key value EX seconds ​(need not implement other SET options) 
- GET​ key 
- DEL​ key 
- DBSIZE 
- INCR​ key 
- ZADD​ key score member 
- ZCARD​ key 
- ZRANK​ key member 
- ZRANGE​ key start stop 

## Build and Execute
> this project uses Maven

**to build**

`mvn clean compile assembly:single`


**to execute**

`java -jar target\miniredis-0.0.1-SNAPSHOT-jar-with-dependencies.jar`