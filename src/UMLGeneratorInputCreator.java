import java.io.*;
import java.util.*;
import japa.parser.ast.type.ClassOrInterfaceType;
import net.sourceforge.plantuml.SourceStringReader;

public class UMLGeneratorInputCreator {
	public ArrayList<ClassInformation> lstClasses;
	private String UMLGeneratorInp;
	
	public UMLGeneratorInputCreator(){
	}
	
	public void setUMLGeneratorInp(String str){
		this.UMLGeneratorInp=str;
	}
	public String getUMLGeneratorInp(){
		return UMLGeneratorInp;
	}
	
	public void formGeneratorString(Hashtable<String, ClassInformation> mapClassNameToInfo,ArrayList<RelationshipInformation> lstRelDetails) throws IOException{
		this.lstClasses= new ArrayList<ClassInformation> (mapClassNameToInfo.values());
		
		UMLGeneratorInp = "@startuml\n";
		UMLGeneratorInp += "skinparam classAttributeIconSize 0\n";
		
		//ADD RELATIONSHIP DETAILS
		if(lstRelDetails.size()>0){
			for(RelationshipInformation r: lstRelDetails){
				//UMLGeneratorInp +=c.name+" --|> "+cls.getName()+"\n";
				UMLGeneratorInp +=r.getSrcCls()+" "+r.getRel()+" "+r.getDestCls()+"\n";
			}
		}

		
		for(ClassInformation c: this.lstClasses){
			//System.out.println("********in uml generator class info= "+c.name+" is interface= "+c.isInterface + " inherited classes= "+c.lstInheritedClasses+" implemented classes= "+c.lstImplementedClasses);
			//Add interface declarations
			if(c.isInterface)
				UMLGeneratorInp +="interface "+c.name+"\n";
						
			//Class details
			if(c.isInterface)
				UMLGeneratorInp +="interface "+c.name+" {\n";
			else
				UMLGeneratorInp +="class "+c.name+" {\n";
			
			//Attribute Details
			for(AttributeInformation a:  c.mapAttributes.values()){
				//Access specifier
				if(a.getAccessSpecifier() == Constants.privateModifier)
					UMLGeneratorInp +="- ";
				else if(a.getAccessSpecifier() == Constants.publicModifier)
					UMLGeneratorInp +="+ ";
				
				//Attribute Name
				UMLGeneratorInp +=a.getName();
				
				//Attribute data type
				if(a.getType()!=null && a.getType().toString()!=null)
					UMLGeneratorInp +=" : "+a.getType().toString()+"\n";
				else
					UMLGeneratorInp +="\n";
			}
			
			//Method details
			for(MethodInformation m:  c.mapMethods.values()){
				//Set + for public methods
				if(m.getAccessSpecifier() == Constants.publicModifier)
					UMLGeneratorInp +="+ ";
				
				//Method Name
				UMLGeneratorInp +=m.getName();
				
				//Method params
				if(m.getParams().size()>0){
					UMLGeneratorInp +="(";
					for(AttributeInformation p: m.getParams()){
						UMLGeneratorInp +=p.getName()+":"+p.getType().toString()+",";
					}
					//remove last ','
					UMLGeneratorInp = UMLGeneratorInp.substring(0, (UMLGeneratorInp.length()-1));
					UMLGeneratorInp +=")";
				}
				else{
					UMLGeneratorInp +="()";
				}
				
				//Method return type
				if(m.getReturnType()!=null)
					UMLGeneratorInp +=" : "+m.getReturnType()+"\n";
				else
					UMLGeneratorInp +="\n";
			}
			
			UMLGeneratorInp +=" }\n";
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
	
}
