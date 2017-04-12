import java.io.*;

import java.util.*;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.TypeDeclaration;

public class UMLParserMain {
	private static String srcFolderName;
	private static String outputFileName;
	public static Hashtable<String, ClassInformation> mapClassNameToInfo;
	public static ArrayList<ClassInformation> lstClasses;
	public static ArrayList<RelationshipInformation> lstRelDetails;

	public static void main(String[] args) throws Exception {
		mapClassNameToInfo = new Hashtable<String, ClassInformation>();
		lstClasses = new ArrayList<ClassInformation>();
		lstRelDetails = new ArrayList<RelationshipInformation>();
		
		/*System.out.println("Program Arguments:");
		if(args.length > 0){
			if(args[0]!=null && args[0]!=""){
				srcFolderName=args[0];
			}else{
				System.out.println("Please provide Source folder argument");
				System.exit(1);
			}
			System.out.println("Source Folder " + srcFolderName);
			
			if(args[1]!=null && args[1]!=""){
				outputFileName=args[1];
			}else{
				System.out.println("Please provide Output file name as argument");
				System.exit(1);
			}
			System.out.println("Output File argument " + outputFileName);
		}*/
		
		
		//HARDCODED ARGUMENTS
		srcFolderName="C:/Semester I/CMPE202/cmpe202-master/umlparser/uml-parser-test-5";
		
		//Process all .java files in srcFolder
		File folder = new File(srcFolderName);
		for(File fileObj:folder.listFiles()){
			if(fileObj.getName().endsWith(".java")){
				System.out.println("Processing file="+fileObj.getName());
				// creates an input stream for the file to be parsed
				FileInputStream in = new FileInputStream(fileObj);
				// parse file
				CompilationUnit cu = JavaParser.parse(in);
				
				// visit and print the class names
				List<TypeDeclaration> types =cu.getTypes();
				for (TypeDeclaration type : types) {
					if (type instanceof ClassOrInterfaceDeclaration) {
						ClassOrInterfaceDeclaration classDec = (ClassOrInterfaceDeclaration) type;
						ClassInformation c=new ClassInformation().getClassInformation(classDec);
						lstClasses.add(c);
						mapClassNameToInfo.put(c.name, c);
				    	//currentCls.lstRelDetails= r.createRelationshipDetails(currentCls, mapClassNameToInfo);
						System.out.println("********class info= "+c.name+" is interface= "+c.isInterface + " inherited classes= "+c.lstInheritedClasses+" implemented classes= "+c.lstImplementedClasses);
					}
				}
			}
		}
		//Update lstRelDetails
		lstRelDetails= new RelationshipInformation().createRelationshipDetails(mapClassNameToInfo);
		
		//Remove implemented methods from classes
		ClassInformation.removeImplementedMethods(mapClassNameToInfo);
		
		//Form Plant UML generator Input
		UMLGeneratorInputCreator u=new UMLGeneratorInputCreator();
		u.formGeneratorString(mapClassNameToInfo,lstRelDetails);
		
		//Call to PlantUML

	}

}
