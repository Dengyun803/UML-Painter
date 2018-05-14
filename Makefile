

cp=bin:jars/junit-4.12.jar:jars/hamcrest-core-1.3.jar


run:
	javac -classpath $(cp) -d bin src/*.java src/ui/*.java src/model/*.java
	java -classpath $(cp) EREdit


junit:
	javac -classpath $(cp) -d bin src/*.java src/model/*.java src/ui/*.java test/*.java
	java -classpath $(cp) org.junit.runner.JUnitCore ERModelTest

clean:
	rm -rf bin/*.class
	
