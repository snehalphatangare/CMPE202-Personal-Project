import java.util.Stack;


public aspect AspectDefinition {
	pointcut allexecution(Object o) : !within(CustomStack) && !within(Main) && target(o) && execution(public * *.*(..))
	 && !execution(*.new(..));

	pointcut allConstructorCalls() : !within(CustomStack) && !within(Main) && execution(*.new(..));	
	
	public static class AspectDefinitionInnerClass {
		private int methodDepth =0 ;
		private int constructorDepth = 0;
		//Stack of class names i.e participants
		private Stack<String> participantStack = new Stack<String>();
	}
	
	AspectDefinitionInnerClass aspectObj = new AspectDefinitionInnerClass();
	before() : constructorCalls() {
		System.out.println("**********before Constructor ");
	}
	
	after() :  constructorCalls() {
		System.out.println("**********after Constructor ");
		
	}
	
	before(Object o)  : allMethodCalls(o) { 
		System.out.println("**********Before method execution in "+ o.getClass()+" "+thisJoinPoint.toString());
	}
	
	after(Object o) : allMethodCalls(o){
			System.out.println("**********After method execution in "+ o.getClass()+" "+thisJoinPoint.toString());
	}
}