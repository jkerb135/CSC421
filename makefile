JFLAGS = -g
JC = javac
JVM= java 
FILE=

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

	
JAVAFILES := $(wildcard *.java)

MAIN = Racko

default: build

build: $(JAVAFILES:.java=.class)

run: $(MAIN).class
	$(JVM) $(MAIN) $(MAKECMDGOALS)

clean:
	$(RM) *.class