javac *.java
jar cvf Racko.jar *.class Images
jarsigner Racko.jar JoshKerbaugh
cp Racko.jar /www/student/jkerb135/CSC421/Racko.jar