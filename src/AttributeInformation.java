import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.expr.Expression;
//import japa.parser.ast.body.MethodDeclaration;
//import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import java.io.*;
import java.util.List;
import java.util.Optional;

public class AttributeInformation extends VoidVisitorAdapter<Void> {
	private String name;
	private String type;
	private String accessSpecifier;
	//private Optional<Expression> initialization;
	
	public String getName(){
		return this.name;
	}
	public String getType(){
		return this.type;
	}
	public String getAccessSpecifier(){
		return this.accessSpecifier;
	}
	
	public AttributeInformation(){
		
	}
	public AttributeInformation(Parameter param){
		this.name=param.getId().toString();
		this.type=param.getType().toString();
		
	}
	
	public AttributeInformation getAttributeInformation (FieldDeclaration attributeDec) throws Exception{
		 //System.out.println("****Method name="+methodDec.getName());
		// visit and print the class names
		visit(attributeDec, null);
		
		 System.out.println("****Attribue Info="+this.name+' '+this.accessSpecifier+' '+this.type);
		return this;
	}

	
   @Override
   public void visit(FieldDeclaration n, Void arg) {
      
     //this.name=n.toString();
    String str= n.getVariables().toString();
    str = str.replaceAll("\\[", "").replaceAll("\\]","");
    if(str.contains("=")){
    	this.name=(str.split("="))[0].trim();
    	//System.out.println("intialized value is "+(str.split("="))[1]);
    }else
    	this.name=str;
    
     this.type=n.getType().toString();
     this.accessSpecifier = Utility.getAccessSpecifier(n.getModifiers());
     
     
     
   }
   
}
