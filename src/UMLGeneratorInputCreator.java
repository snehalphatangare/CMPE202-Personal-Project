import java.io.*;
import java.util.*;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.ReferenceType;
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
	
	public void formGeneratorString(Hashtable<String, ClassInformation> mapClassNameToInfo,ArrayList<RelationshipInformation> lstRelDetails,String outputFileName) throws IOException{
		this.lstClasses= new ArrayList<ClassInformation> (mapClassNameToInfo.values());
		
		UMLGeneratorInp = "@startuml\n";
		UMLGeneratorInp += "skinparam classAttributeIconSize 0\n";
		
		//ADD RELATIONSHIP DETAILS
		if(lstRelDetails.size()>0){
			for(RelationshipInformation r:lstRelDetails){
				System.out.println("+++++++++ "+r.getSrcCls()+" "+r.getDestCls()+" "+r.getSrcMultiplicity()+" "+r.getDestMultiplicity());
			}
			for(RelationshipInformation r: lstRelDetails){
				String srcMul=r.getSrcMultiplicity()!= null ? "\""+r.getSrcMultiplicity()+"\""+" ":"";
				String destMul=r.getDestMultiplicity()!= null ? "\""+r.getDestMultiplicity()+"\""+" ":"";
				UMLGeneratorInp +=r.getSrcCls()+" "+ srcMul+
									r.getRel()+" "+ destMul+
									r.getDestCls()+"\n";
			}
			System.out.println("**********UMLGeneratorInp="+UMLGeneratorInp);
		}

		
		for(ClassInformation c: this.lstClasses){
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
				//Only Show Public and Private and Primitive type Attributes 
				if((a.getAccessSpecifier() == Constants.privateModifier || a.getAccessSpecifier()== Constants.publicModifier))
				{
					//if(a.getType()!=null && (a.getType().toString().equalsIgnoreCase("String") || a.getType().toString().contains("[") || !(a.getType() instanceof ReferenceType)))
					if(a.getType()!=null && !Utility.isReferenceTypeVariable(a.getType()))
					{
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
				}
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
		
		//OutputStream png = new FileOutputStream("C:/Users/Snehal Phatangare/Pictures/testImg");
		OutputStream png = new FileOutputStream(outputFileName+".png");
		
		SourceStringReader reader = new SourceStringReader(UMLGeneratorInp);
		// Write the image to "png"
		reader.generateImage(png);
		System.out.println("Done! ");
		
		
	}
	
}
