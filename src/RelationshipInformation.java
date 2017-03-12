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
		
		
		
		return lstRel;
	}
}
