# Java-Compiler
### Build compiler
#### First time
```
mvn compile
```
#### Second time
```
mvn package
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
