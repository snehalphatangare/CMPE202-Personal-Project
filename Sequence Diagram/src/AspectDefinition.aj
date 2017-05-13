import java.io.*;
import java.util.Stack;

import net.sourceforge.plantuml.SourceStringReader;

public aspect AspectDefinition {
	pointcut allMethodCalls(Object o) : !within(MessageIdStack) && !within(AspectDefinition) && target(o) && execution(public * *.*(..))
	 && !execution(*.new(..));

	pointcut constructorCalls() : !within(MessageIdStack) && !within(AspectDefinition) && execution(*.new(..));
	
	pointcut mainMethod() : execution(public * Main.main(..));
	
	public static class AspectDefinitionInnerClass {
		private int methodDepth =0 ;
		private int constructorDepth = 0;
		//Stack of class names i.e participants
		private Stack<String> participantStack = new Stack<String>();
		//Stack of message numbers
		private MessageIdStack<Integer> messageNumberStack = new MessageIdStack<>();
		private int idVal = 1;
		
		private String UMLGeneratorInp= "@startuml\n";
		
		public void incrementConstructorDepth() {
			constructorDepth++;
		}
		
		public void decrementConstructorDepth() {
			constructorDepth--;
		}
		
		public void methodCompleteExecution() throws IOException {
			if(constructorDepth ==0) {
				updateStackVal();
				//If all methods removed, print sequence diagram
				if(methodDepth == 0) {
					printSequenceDiagram();
				}
			}
		}
		
		private void updateStackVal() {
			methodDepth--;
			if(!participantStack.isEmpty()) {
				UMLGeneratorInp += "deactivate "+participantStack.peek() + "\n";
				participantStack.pop();
			}
			if(!messageNumberStack.isEmpty()) {
				idVal = (Integer)messageNumberStack.pop()+1;
			}
		}

		private void printSequenceDiagram() throws IOException {
			UMLGeneratorInp +=" @enduml\n";
			System.out.println("*****Final generator input= "+UMLGeneratorInp);
			String outputFileName="sequenceDiagram";
			OutputStream png = new FileOutputStream(outputFileName+".png");
			SourceStringReader reader = new SourceStringReader(UMLGeneratorInp);
			// Write the image to "png"
			reader.generateImage(png);
			System.out.println("Done! ");
		}
		
		public void methodCallExecution(String joinPoint,String targetClassName) {
			if(constructorDepth ==0) {
				//System.out.println("before joinpoint "+joinPoint);
				messageNumberStack.push(idVal);
				idVal=1;
				String currentCls=targetClassName;
				String result = getCallingClassName() + " ->" + currentCls + ":" + messageNumberStack.printAll() + " " + getMessageString(joinPoint) + "\n";
				UMLGeneratorInp += getCallingClassName() + " -> " + currentCls + " : " + messageNumberStack.printAll() + " " + getMessageString(joinPoint) + "\n";
				UMLGeneratorInp += "activate "+currentCls + "\n";
				methodDepth += 1;
				participantStack.push(currentCls);
				//System.out.println("****after push participantStack="+participantStack);
			}
		}
		
		public String getCallingClassName() {
			if(participantStack.isEmpty())
				return "Main";
			
			return (String)participantStack.peek();
		}
		
		public static String getMessageString(String joinPoint) {
			//Extract method signature
			//Remove the string "execution" from jointpoint string e.g methodSignatureString = void Pessimist.update()
			String methodSignatureString= joinPoint.substring(joinPoint.indexOf('(')+1,joinPoint.lastIndexOf(')'));
			String components[] = methodSignatureString.split(" ");
			//components[0] = return type of method
			//components[1] contains method name after "."
			return components[1].substring(components[1].indexOf('.')+1) + " : "+ components[0];
		}
		
	}

	
	AspectDefinitionInnerClass aspectObj = new AspectDefinitionInnerClass();

	//Define Advices
	before() : constructorCalls() {
		aspectObj.incrementConstructorDepth();
	}
	
	after() :  constructorCalls() {
		aspectObj.decrementConstructorDepth();
		
	}
	
	before(Object o)  : allMethodCalls(o) { 
		System.out.println("**********Before method execution in "+ o.getClass()+" "+thisJoinPoint.toString());
		aspectObj.methodCallExecution(thisJoinPoint.toString(),o.getClass().getName());
	}
	
	after(Object o) : allMethodCalls(o){
		try {
			//System.out.println("**********After method execution in "+ o.getClass()+" "+thisJoinPoint.toString());
			aspectObj.methodCompleteExecution();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	 before() : mainMethod()
	    {
		 aspectObj.methodCallExecution(thisJoinPoint.toString(),"Main");
	    }
	 
	 after() : mainMethod()
	    {
			try {
				aspectObj.methodCompleteExecution();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
}
