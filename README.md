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

## Testing

#### Input

Sample test cases can be found in test folders
Output

##### Test Case 1
![testcase1](https://user-images.githubusercontent.com/25491319/31643259-7e1a3fd8-b2a4-11e7-8822-81ae374c8fb5.png)

##### Test Case 2
![testcase2](https://user-images.githubusercontent.com/25491319/31643347-ece31840-b2a4-11e7-9dc4-64bfc2e8bf4f.png)

##### Test Case 3
![testcase3](https://user-images.githubusercontent.com/25491319/31643353-f36734a8-b2a4-11e7-8c15-79c6c2443fdb.png)

##### Test Case 4
![testcase4](https://user-images.githubusercontent.com/25491319/31643358-f8b84b9a-b2a4-11e7-86c6-1a15c7b49e32.png)

##### Test Case 5
![testcase5](https://user-images.githubusercontent.com/25491319/31643367-0134cae6-b2a5-11e7-885f-1c9004ad392a.png)

##### Sequence Diagram
![sequencediagram](https://user-images.githubusercontent.com/25491319/31643532-e626483c-b2a5-11e7-9b66-7b3a4ed86a01.png)
