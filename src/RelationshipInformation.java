import java.util.ArrayList;
import java.util.Hashtable;

import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.ReferenceType;

public class RelationshipInformation {
	private String srcCls;
	private String destCls;
	//Relationship between classes defined by symbols 
	private String rel;
	//Multiplicity between classes defined as 0,*,1
	private String srcMutiplicity; 
	private String destMutiplicity;
	
	public RelationshipInformation(){
		this.srcCls="";
		this.destCls="";
		this.rel="";
		this.srcMutiplicity=null;
		this.destMutiplicity=null;
	}
	public RelationshipInformation(String src,String dest,String relation,String srcMul,String destMul){
		this.srcCls=src;
		this.destCls=dest;
		this.rel=relation;
		this.srcMutiplicity=srcMul;
		this.destMutiplicity=destMul;
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
	
	public ArrayList<RelationshipInformation> createRelationshipDetails(Hashtable<String, ClassInformation> mapClassNameToInfo){
		ArrayList<RelationshipInformation> lstRel = new ArrayList<RelationshipInformation>();
		
		for(ClassInformation c: mapClassNameToInfo.values()){
			//Inheritance Relationships
			if(c.lstInheritedClasses.size()>0){
				for(ClassOrInterfaceType cls:c.lstInheritedClasses){
					RelationshipInformation r = new RelationshipInformation(cls.getName(),c.name,"<|--",null,null);
					lstRel.add(r);
				}
			}
			//Implementation Relationships
			if(c.lstImplementedClasses.size()>0){
				for(ClassOrInterfaceType cls:c.lstImplementedClasses){
					RelationshipInformation r = new RelationshipInformation(cls.getName(),c.name,"<|..",null,null);
					lstRel.add(r);
				}
			}
		}
		
		
		//Association Relationships
		System.out.println("*********Association Relationships");
		for(ClassInformation c: mapClassNameToInfo.values()){
			System.out.println("***********For class "+c.name);
			//Check if current class has an attribute of type another class
			//Iterate on attributes of current class
			for(AttributeInformation a:  c.mapAttributes.values()){
				System.out.println("***********For attribute "+a.getName());
				if(a.getType()!=null && a.getType().toString()!=null){
					
					String attributeDT="";
					String destMultiplicity=null;
					//If attribute is of type reference get the reference Data Type
					if(a.getType() instanceof ReferenceType){
						String strDT=a.getType().toString();
						if(strDT.contains("<") && strDT.contains(">")){
							attributeDT=strDT.substring((strDT.indexOf("<")+1),strDT.indexOf(">"));
							destMultiplicity="*";
						}
						else{
							attributeDT=strDT;
							//destMultiplicity="1";
						}
					}
					//If type of attribute is same as another class
					if(mapClassNameToInfo.containsKey(attributeDT) && !attributeDT.equalsIgnoreCase(c.name)){
						Boolean createNewRelation=true;
						
						String secondCls=attributeDT;
						System.out.println("***********This class && secondCls "+c.name+"  "+secondCls);
						//Before creating relationship check if a relationship between two classes already exists
						RelationshipInformation newRel = new RelationshipInformation();
						if(lstRel.size() > 0){
							System.out.println("***********secondCls "+secondCls);
							for(RelationshipInformation r:lstRel){
								if(((r.srcCls.equals(c.name) && r.destCls.equals(secondCls)) || (r.srcCls.equals(secondCls) && r.destCls.equals(c.name))) && r.rel=="--"){
									//Relation already exists
									createNewRelation=false;
									System.out.println("***********REL exists");
									break;
								}
							}
							//If no existing relationship found, create one
							newRel = new RelationshipInformation(c.name,secondCls,"--",null,destMultiplicity);
							System.out.println("++++++++++++ASSOCIATION REL created between "+c.name+' '+secondCls);
						}
						else{
							newRel = new RelationshipInformation(c.name,secondCls,"--",null,destMultiplicity);
							System.out.println("***********ASSOCIATION REL created between "+c.name+' '+secondCls);
						}
						
						//Add new relation if was created
						if(createNewRelation)
							lstRel.add(newRel);
					}
				}
			}
			
		}
		
		//Dependency Relationships
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
		else{
			System.out.println("**********this is 1st dependency relationship for this useCase");
			createNewRelation=true;
		}
		if(createNewRelation)
			return true;
		else
			return false;
	}
}
