# Java Parser UML Generator
There are 2 parts of the project:
1.	Java Parser – The parser reads all the java source code in the provided source path, and creates a grammar language that is interpretable by the UML generator
2.	UML Generator – This part takes input grammar from the JavaParser module and generates a UML diagram

#### Prerequisites
The following must be installed on the system
  •	Java JDK version 1.8
  •	GraphViz

#### Running the Program
Windows/Mac OS:
1.	Unzip the UMLParser.zip file and cd into the folder. 
  i.	cd <pathname> UMLParser
2.	Run the jar file by passing the java source file path and output filename
  i.	java -jar umlParser.jar <javafolderpath> <outputimagename>
  ii.	Sample: java -jar umlParser.jar UMLParser/test1 classDiagram

#### Libraries and tools used
1.	Java Parser: 
For parsing the JAVA code into a usable grammar, I have used the javaparser library: https://github.com/javaparser/javaparser
Javaparser is lightweight and easy to use parser library which parses java code and provides AST (Abstract Syntax Tree).
2.	GraphViz:
GraphViz is open source graph visualization software. GraphViz supports dot(.) notation input for drawing directed graphs. The GraphViz software takes input as simple text file and converts it to diagram.
Limitation: GraphViz application is stand-alone application and can not be integrated in java project without third party library.
Installation instructions: http://plantuml.com/graphviz-dot
3.	PlantUML: 
For generating the class diagram from the parsed code returned by the java parser, plantUML library is used: http://plantuml.com/
PlantUML provides a Java based API for using GraphViz software through your Java application. This library is used to integrate GraphViz directly into our application.

