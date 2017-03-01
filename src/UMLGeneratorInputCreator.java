import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import japa.parser.ast.type.ClassOrInterfaceType;
import net.sourceforge.plantuml.SourceStringReader;

public class UMLGeneratorInputCreator {
	public ArrayList<ClassInformation> lstClasses;
	private String UMLGeneratorInp;
	
	public UMLGeneratorInputCreator(){
		//this.lstClasses = new ArrayList<ClassInformation>();
	}
	
	public void setUMLGeneratorInp(String str){
		this.UMLGeneratorInp=str;
	}
	public String getUMLGeneratorInp(){
		return UMLGeneratorInp;
	}
	
	public void formGeneratorString(Hashtable<String, ClassInformation> mapClassNameToInfo) throws IOException{
		this.lstClasses= new ArrayList<ClassInformation> (mapClassNameToInfo.values());
		
		UMLGeneratorInp = "@startuml\n";
		UMLGeneratorInp += "skinparam classAttributeIconSize 0\n";
		
		findOtherRelationships(mapClassNameToInfo);
		for(ClassInformation c: this.lstClasses){
			//System.out.println("********in uml generator class info= "+c.name+" is interface= "+c.isInterface + " inherited classes= "+c.lstInheritedClasses+" implemented classes= "+c.lstImplementedClasses);
			//Add interface declarations
			if(c.isInterface)
				UMLGeneratorInp +="interface "+c.name+"\n";
			
			//Inheritance
			if(c.lstInheritedClasses.size()>0){
				for(ClassOrInterfaceType cls:c.lstInheritedClasses){
					UMLGeneratorInp +=c.name+" --|> "+cls.getName()+"\n";
				}
				
			}
			
			//Implementation
			if(c.lstImplementedClasses.size()>0){
				for(ClassOrInterfaceType cls:c.lstImplementedClasses){
					UMLGeneratorInp +=c.name+" ..|> "+cls.getName()+"\n";
				}
			}
			
			
			//Class details
			if(c.isInterface)
				UMLGeneratorInp +="interface "+c.name+" {\n";
			else
				UMLGeneratorInp +="class "+c.name+" {\n";
			
			//Attribute Details
			for(AttributeInformation a:  c.lstAttributes){
				//Access specifier
				if(a.getAccessSpecifier() == Constants.privateModifier)
					UMLGeneratorInp +="- ";
				else if(a.getAccessSpecifier() == Constants.publicModifier)
					UMLGeneratorInp +="+ ";
				
				//Attribute Name
				UMLGeneratorInp +=a.getName();
				
				//Attribute data type
				if(a.getType()!=null)
					UMLGeneratorInp +=" : "+a.getType()+"\n";
				else
					UMLGeneratorInp +="\n";
			}
			
			//Method details
			for(MethodInformation m:  c.lstMethods){
				//Access specifier
				if(m.getAccessSpecifier() == Constants.publicModifier)
					UMLGeneratorInp +="+ ";
				
				//Method Name
				UMLGeneratorInp +=m.getName()+"()";
				
				//Method return type
				if(m.getReturnType()!=null)
					UMLGeneratorInp +=" : "+m.getReturnType()+"\n";
				else
					UMLGeneratorInp +="\n";
			}
			
			UMLGeneratorInp +=" }\n";
			//System.out.println("After class "+c.name+" generator inp= "+UMLGeneratorInp);
		}
		
		UMLGeneratorInp +=" @enduml\n";
		System.out.println("Final generator input= "+UMLGeneratorInp);
		
		OutputStream png = new FileOutputStream("C:/Users/Snehal Phatangare/Pictures/testImg.png");
		/*UMLGeneratorInp = "@startuml\n";
		UMLGeneratorInp +="Class01 <|-- Class02\n" ;
		UMLGeneratorInp +="Class03 <|-- Class04\n" ;
		UMLGeneratorInp +="@enduml\n";*/
		
		SourceStringReader reader = new SourceStringReader(UMLGeneratorInp);
		// Write the first image to "png"
		String desc = reader.generateImage(png);
		System.out.println("Done!");
		
		
	}
	
	private void findOtherRelationships(Hashtable<String, ClassInformation> mapClassNameToInfo){
		
		for(ClassInformation c: this.lstClasses){
			//Check if class has an attribute of type another class
			for(AttributeInformation a:  c.lstAttributes){
				if(a.getType()!=null){
					//check if type of attribute is same as another class
					if(mapClassNameToInfo.containsKey(a.getType()) && a.getType()!=c.name){
						UMLGeneratorInp +=c.name+"--"+a.getType()+"\n";
					}
				}
			}
			
		}
	}
	
}
