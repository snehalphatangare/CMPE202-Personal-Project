import japa.parser.ast.body.*;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import java.util.*;


public class ClassInformation extends VoidVisitorAdapter<Void> {
	public String name;
	public Boolean isInterface;
	public ArrayList<AttributeInformation> lstAttributes;
	public ArrayList<MethodInformation> lstMethods;
	public List<ClassOrInterfaceType> lstInheritedClasses;
	public List<ClassOrInterfaceType> lstImplementedClasses;
	public HashMap methodCalls;
	
	public ClassInformation( ) {
		lstAttributes = new ArrayList<AttributeInformation>();
		lstMethods= new ArrayList<MethodInformation>();
		lstInheritedClasses = new ArrayList<ClassOrInterfaceType>();
		lstImplementedClasses = new ArrayList<ClassOrInterfaceType>();
	}
	
	public ClassInformation getClassInformation (ClassOrInterfaceDeclaration clsDec) throws Exception{
		// parse file
	//	CompilationUnit cu = JavaParser.parse(in);
		// visit and print the methods names
		// new MethodVisitor().visit(cu, null);    	

		
		visit(clsDec, null);
		
		List<BodyDeclaration> members = clsDec.getMembers();
		for (BodyDeclaration member : members) {
            if (member instanceof MethodDeclaration) {
            	MethodDeclaration methodDec = (MethodDeclaration) member;
            	MethodInformation m=new MethodInformation().getMethodInformation(methodDec);
				this.lstMethods.add(m);
            }
            if (member instanceof FieldDeclaration) {
            	FieldDeclaration attributeDec = (FieldDeclaration) member;
            	AttributeInformation a=new AttributeInformation().getAttributeInformation(attributeDec);
            	if(a.getAccessSpecifier()!=null)
            		this.lstAttributes.add(a);
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
    
}
