import java.util.ArrayList;
import java.util.Hashtable;

import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.ReferenceType;

public class RelationshipInformation {
	private String srcCls;
	private String destCls;
	//Relationship between classes defined by symbols 
	private String rel;
	//Multiplicity between classes defined as 0..*
	private String mutiplicity; 
	
	public RelationshipInformation(){
		this.srcCls="";
		this.destCls="";
		this.rel="";
		this.mutiplicity=null;
	}
	public RelationshipInformation(String src,String dest,String relation,String mul){
		this.srcCls=src;
		this.destCls=dest;
		this.rel=relation;
		this.mutiplicity=mul;
	}
	
	public String getSrcCls(){
		return this.srcCls;
	}
	
	public String getDestCls(){
		return this.destCls;
	}
	public String getRel(){
		return this.rel;
	}
	public String getMultiplicity(){
		return this.mutiplicity;
	}
	
	public ArrayList<RelationshipInformation> createRelationshipDetails(Hashtable<String, ClassInformation> mapClassNameToInfo){
		ArrayList<RelationshipInformation> lstRel = new ArrayList<RelationshipInformation>();
		
		for(ClassInformation c: mapClassNameToInfo.values()){
			//Inheritance Relationships
			if(c.lstInheritedClasses.size()>0){
				for(ClassOrInterfaceType cls:c.lstInheritedClasses){
					RelationshipInformation r = new RelationshipInformation(cls.getName(),c.name,"<|--",null);
					lstRel.add(r);
				}
			}
			//Implementation Relationships
			if(c.lstImplementedClasses.size()>0){
				for(ClassOrInterfaceType cls:c.lstImplementedClasses){
					RelationshipInformation r = new RelationshipInformation(cls.getName(),c.name,"<|..",null);
					lstRel.add(r);
				}
			}
		}
		
		for(ClassInformation c: mapClassNameToInfo.values()){
			System.out.println("***********For class "+c.name);
			//Check if current class has an attribute of type another class
			//Iterate on attributes of current class
			for(AttributeInformation a:  c.mapAttributes.values()){
				System.out.println("***********For attribute "+a.getName());
				if(a.getType()!=null && a.getType().toString()!=null){
					
					String attributeDT="";
					//If attribute is of type reference get the reference Data Type
					if(a.getType() instanceof ReferenceType){
						String strDT=a.getType().toString();
						if(strDT.contains("<") && strDT.contains(">")){
							attributeDT=strDT.substring((strDT.indexOf("<")+1),strDT.indexOf(">"));
						}
						else
							attributeDT=strDT;
						System.out.println("***********Attribute DT="+attributeDT);
					}
				}
			}
}
		
		return lstRel;
	}
}
