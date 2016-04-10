CLASS = *.class # the desired file extension
JAVA = *.java

default: 
	javac $(JAVA)

clean:
	/bin/rm -f $(CLASS) 
