import japa.parser.ast.body.*;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import java.util.*;


public class ClassInformation extends VoidVisitorAdapter<Void> {
	public String name;
	public Boolean isInterface;
	public Hashtable<String, AttributeInformation> mapAttributes;
	public Hashtable<String, MethodInformation> mapMethods;
	public List<ClassOrInterfaceType> lstInheritedClasses;
	public List<ClassOrInterfaceType> lstImplementedClasses;
	//public ArrayList<RelationshipInformation> lstRelDetails;
	//public HashMap methodCalls;
	
	public ClassInformation( ) {
		mapAttributes = new Hashtable<String, AttributeInformation>();
		mapMethods= new Hashtable<String, MethodInformation>();
		lstInheritedClasses = new ArrayList<ClassOrInterfaceType>();
		lstImplementedClasses = new ArrayList<ClassOrInterfaceType>();
		//lstRelDetails = new ArrayList<RelationshipInformation>();
	}
	
	public ClassInformation getClassInformation (ClassOrInterfaceDeclaration clsDec) throws Exception{
		// parse file
	//	CompilationUnit cu = JavaParser.parse(in);
		// visit and print the methods names
		// new MethodVisitor().visit(cu, null);    	

		
		visit(clsDec, null);
		
		List<BodyDeclaration> members = clsDec.getMembers();
		//Get details of all variables declared in class
		for (BodyDeclaration member : members) {
            if (member instanceof FieldDeclaration) {
            	FieldDeclaration attributeDec = (FieldDeclaration) member;
            	AttributeInformation a=new AttributeInformation().getAttributeInformation(attributeDec);
            	//Only Show Public and Private Attributes
            	if(a.getAccessSpecifier()!=null)
            		this.mapAttributes.put(a.getName(),a);
            }
		}
		//Get details of all methods in class
		for (BodyDeclaration member : members) {
            if (member instanceof MethodDeclaration) {
            	MethodDeclaration methodDec = (MethodDeclaration) member;
            	MethodInformation m=new MethodInformation().getMethodInformation(methodDec);
            	//Add a method only if it is a public method AND is not a getter/setter method. 
            	if(m.getAccessSpecifier() == Constants.publicModifier && !m.isGetterOrSetterMethod(m,this.mapAttributes))
            		this.mapMethods.put(m.getName(),m);
            		
            }
		}
		
		
		return this;
	}

	
    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void arg) {
       
    //  System.out.println("****Class name="+n.getName());
      this.name=n.getName();
      this.isInterface= n.isInterface();
      if(n.getExtends()!=null)
    	  this.lstInheritedClasses=n.getExtends();
      if(n.getImplements()!=null)
    	  this.lstImplementedClasses=n.getImplements();
     
      
     //Visit inner classes
      super.visit(n, arg);
    }
    
    /*public void setRelationshipInfo(ClassInformation currentCls,Hashtable<String, ClassInformation> mapClassNameToInfo){
    	RelationshipInformation r= new RelationshipInformation();
    	currentCls.lstRelDetails= r.createRelationshipDetails(currentCls, mapClassNameToInfo);
    }*/
    
}
