import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.Type;
import japa.parser.ast.visitor.VoidVisitorAdapter;

public class AttributeInformation extends VoidVisitorAdapter<Void> {
	private String name;
	private Type type;
	private String accessSpecifier;
	//private Optional<Expression> initialization;
	
	public String getName(){
		return this.name;
	}
	public Type getType(){
		return this.type;
	}
	public String getAccessSpecifier(){
		return this.accessSpecifier;
	}
	public void setAccessSpecifier(String scope){
		this.accessSpecifier=scope;
	}
	
	public AttributeInformation(){
		
	}
	public AttributeInformation(Parameter param){
		this.name=param.getId().toString();
		this.type=param.getType();
		
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
      
    String str= n.getVariables().toString();
    str = str.replaceAll("\\[", "").replaceAll("\\]","");
    if(str.contains("=")){
    	this.name=(str.split("="))[0].trim();
    	//System.out.println("intialized value is "+(str.split("="))[1]);
    }else
    	this.name=str;
    
     this.type=n.getType();
     this.accessSpecifier = Utility.getAccessSpecifier(n.getModifiers());
     
   }
   
   //Checks if attribute is of Reference Data type
  /* public Boolean isReferenceDataType(AttributeInformation a){
	  // Boolean isReferenceDT=false;
	   Type dt=a.getType();
	   //dt.
		return false;	   
   }*/
   
}
