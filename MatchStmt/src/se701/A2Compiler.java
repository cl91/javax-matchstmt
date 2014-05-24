package se701;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.visitor.TranslationVisitor;
import japa.parser.ast.visitor.TypingVisitor;
import japa.parser.ast.visitor.ResolvingVisitor;
import japa.parser.ast.visitor.InheritanceVisitor;
import japa.parser.ast.visitor.DefinitionVisitor;
import symtab.GlobalScope;

public class A2Compiler {
	
	/*
	 * This is the only method you should need to change inside this class. But do not modify the signature of the method! 
	 */
	public static void compile(File file) throws ParseException, FileNotFoundException {

		// parse the input, performs lexical and syntactic analysis
		JavaParser parser = new JavaParser(new FileReader(file));
		CompilationUnit ast = parser.CompilationUnit();
		
		// semantics analysis
		GlobalScope global_scope = new GlobalScope();
		// collecting class and interface info
		TypingVisitor typing = new TypingVisitor(global_scope);
		ast.accept(typing, null);
		// collecting extends and implements info
		InheritanceVisitor inh = new InheritanceVisitor(global_scope);
		ast.accept(inh, null);
		// collecting variable definitions
		DefinitionVisitor def = new DefinitionVisitor(global_scope);
		ast.accept(def, null);
		// resolve variable references
		ResolvingVisitor res = new ResolvingVisitor(global_scope);
		ast.accept(res, null);
		
		// do the javax to java translation
		TranslationVisitor tr = new TranslationVisitor();
		ast.accept(tr, null);
		
		String result = tr.getSource();
		
		// save the result into a *.java file, same level as the original file
		File javaFile = getAsJavaFile(file);
		writeToFile(javaFile, result);
	}
	
	/*
	 * Given a *.javax File, this method returns a *.java File at the same directory location  
	 */
	private static File getAsJavaFile(File javaxFile) {
		String javaxFileName = javaxFile.getName();
		File containingDirectory = javaxFile.getAbsoluteFile().getParentFile();
		String path = containingDirectory.getAbsolutePath()+System.getProperty("file.separator");
		String javaFilePath = path + javaxFileName.substring(0,javaxFileName.lastIndexOf("."))+".java";
		return new File(javaFilePath);
	}
	
	/*
	 * Given the specified file, writes the contents into it.
	 */
	private static void writeToFile(File file, String contents) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(file);
		writer.print(contents);
		writer.close();
	}
}
