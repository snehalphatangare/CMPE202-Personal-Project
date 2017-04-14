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
											
						//Add new relation if was created
						//System.out.println("+++++++");
						if(createNewRelation)
							lstRel.add(newRel);
					}
				}
			}
		}
		
		//Dependency Relationships
		System.out.println("*********Dependency Relationships");
		for(ClassInformation c: mapClassNameToInfo.values()){
			//Check if current class has methods with parameter of type another class OR a method in current class initiates object of type another class
			if(!c.isInterface)
			{
				//Iterate on methods of current class
				for(MethodInformation m:  c.mapMethods.values()){
					if(m.getName().equalsIgnoreCase("main")){
						RelationshipInformation newRel = new RelationshipInformation(c.name,"Component","..>",null,null);
						lstRel.add(newRel);
					}
					
					//Check if method has parameter of type another class
					//Iterate on parameters of current method
					for(AttributeInformation a:  m.getParams()){
						System.out.println("***********For attribute "+a.getName()+" in method "+m.getName()+" in class "+c.name);
						//If attribute is of type reference get the reference Data Type
						if(a.getType()!=null && a.getType().toString()!=null && Utility.isReferenceTypeVariable(a.getType())){
							String attributeDT="";
							String strDT=a.getType().toString();
							if(strDT.contains("<") && strDT.contains(">")){
								attributeDT=strDT.substring((strDT.indexOf("<")+1),strDT.indexOf(">"));
							}
							else
								attributeDT=strDT;
							System.out.println("***********Attribute DT="+attributeDT);
							String secondCls=attributeDT;
							
							//If type of attribute is same as another class and the class it depends on is an Interface
							if(mapClassNameToInfo.containsKey(attributeDT) && !attributeDT.equalsIgnoreCase(c.name) && mapClassNameToInfo.get(attributeDT).isInterface){
								//Create dependency relationship between classes
								if(checkRelationshipAlreadyExist(lstRel,c.name,secondCls,"..>")){
									RelationshipInformation newRel = new RelationshipInformation(c.name,secondCls,"..>",null,null);
									lstRel.add(newRel);
									//Dependency relation created, break out of the loop
									break;
								}
							}
						}
					}
					//Check if method initiates object of type another class
				}
			}//IF condition check for interface ends
		}
		
		return lstRel;
	}
	
	private Boolean checkRelationshipAlreadyExist(ArrayList<RelationshipInformation> lstRel,String firstCls,String secondCls,String rel){
		Boolean createNewRelation=true;
		
		if(lstRel.size() > 0){
			for(RelationshipInformation r:lstRel){
				if(((r.srcCls.equals(firstCls) && r.destCls.equals(secondCls)) || (r.srcCls.equals(secondCls) && r.destCls.equals(firstCls))) && r.rel == rel){
					//Relation already exists
					createNewRelation=false;
					System.out.println("***********REL exists");
					//return false;
				}
			}
		}
		return createNewRelation;
	}
}
