import java.util.ArrayList;
import java.util.Hashtable;

import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.ReferenceType;

public class RelationshipInformation {
	private String srcCls;
	private String destCls;
	//Relationship between classes defined by symbols 
	private String rel;
	private String mult;
	private String dependency;
		
	public RelationshipInformation(){
		this.srcCls="";
		this.destCls="";
		this.rel="";
		this.dependency="";
	}
	public RelationshipInformation(String src,String dest,String relation,String srcMul,String destMul){
		this.srcCls=src;
		this.destCls=dest;
		this.rel=relation;
		this.dependency=dependency;
		//this.srcMutiplicity=srcMul;
		//this.destMutiplicity=destMul;
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
	public String getSrcMultiplicity(){
		return this.srcMutiplicity;
	}
	public String getDestMultiplicity(){
		return this.destMutiplicity;
	}
	public String getDependency(){
		return this.dependency;
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
					//If type of attribute is same as another class
					if(mapClassNameToInfo.containsKey(attributeDT) && !attributeDT.equalsIgnoreCase(c.name)){
						//Boolean createNewRelation=false;
						Boolean createNewRelation=true;
						
						String secondCls=attributeDT;
						System.out.println("++++++++++relationships till now");
						if(lstRel.size() > 0){
							for(RelationshipInformation r:lstRel){
								System.out.println("+++++++++ "+r.srcCls+" "+r.destCls);
							}
						}
						System.out.println("***********This class && secondCls "+c.name+"  "+secondCls);
						//Before creating relationship check if a relationship between two classes already exists
						RelationshipInformation newRel = new RelationshipInformation();
						if(lstRel.size() > 0){
							System.out.println("***********secondCls "+secondCls);
							for(RelationshipInformation r:lstRel){
								if((r.srcCls.equals(c.name) && r.destCls.equals(secondCls)) || (r.srcCls.equals(secondCls) && r.destCls.equals(c.name))){
									//Relation already exists
									createNewRelation=false;
									System.out.println("***********REL exists");
									break;
								}
							}
							//no existing relationship found, create one
							//createNewRelation=true;
							newRel = new RelationshipInformation(c.name,secondCls,"--",null);
						}
						else{
							System.out.println("**********this is 1st relationship for this useCase");
							//createNewRelation=true;
							newRel = new RelationshipInformation(c.name,secondCls,"--",null);
							System.out.println("***********ASSOCIATION REL created between "+c.name+' '+secondCls);
						}
						
						//Add new relation if was created
						//System.out.println("+++++++");
						if(createNewRelation)
							lstRel.add(newRel);
					}
				}
			}
		}
		
		return lstRel;
	}
}
