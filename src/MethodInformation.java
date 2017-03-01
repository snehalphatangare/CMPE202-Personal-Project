import japa.parser.ast.body.*;
//import japa.parser.ast.body.MethodDeclaration;
//import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import java.util.*;

public class MethodInformation extends VoidVisitorAdapter<Void> {
	private String name;
	private String accessSpecifier;
	private String returnType;
	private List<AttributeInformation> params;
	private Boolean isConstructor;
	//private Boolean isAbstract;
	
	public String getName(){
		return this.name;
	}
	public String getReturnType(){
		return this.returnType;
	}
	public String getAccessSpecifier(){
		return this.accessSpecifier;
	}
	public Boolean getIsConstructor(){
		return this.isConstructor;
	}
	public List<AttributeInformation> getParams(){
		return this.params;
	}
	
	public MethodInformation(){
		this.params= new ArrayList<AttributeInformation>();
	}
	
	public MethodInformation getMethodInformation (MethodDeclaration methodDec) throws Exception{
		 //System.out.println("****Method name="+methodDec.getName());
		// visit and print the class names
		visit(methodDec, null);
		
		 System.out.println("****Method Info="+this.name+" access specifier="+this.accessSpecifier+" return type="+this.returnType+" method params="+this.params);
		return this;
	}

	
    @Override
    public void visit(MethodDeclaration n, Void arg) {
       
      this.name=n.getName();
      this.accessSpecifier = Utility.getAccessSpecifier(n.getModifiers());
      this.returnType = n.getType().toString();
      
      //Method parameter information
      List<Parameter> params= n.getParameters();
      if(params !=null && params.size()>0){
    	  for(int i=0;i < params.size();i++)
    		  this.params.add(new AttributeInformation((Parameter) params.get(i)));
      }
      
     //Visit inner classes
      super.visit(n, arg);
    }

}
