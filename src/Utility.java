import java.lang.reflect.Modifier;

public class Utility {
	public static String getAccessSpecifier(int modifier){
	   	if(modifier == Modifier.PRIVATE)
	   		return Constants.privateModifier;
	   	else if(modifier == Modifier.PUBLIC)
	   		return Constants.publicModifier;
	   		   
	   	return null;
	 }
}
