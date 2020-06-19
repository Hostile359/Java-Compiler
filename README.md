# Java-Compiler
### Build compiler
```
mvn package
```
#### Build without tests
```
mvn package -DskipTests=true
```
#### Run tests without building project
```
mvn test
```
### Run compiler
```
java -jar target/compiler-1.0.jar [Options] <input_file>
```
### Options list
* --dump-tokens — lexer results
* --dump-ast — parser results
* --dump-asm — code generator results

### Run compiled program
```
./bin/prog
```
