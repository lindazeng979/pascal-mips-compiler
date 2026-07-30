package ast;
import environment.*;
import java.util.*;
import emitter.Emitter;

/**
 * This class represents
 *  a program, which
 * is the root of the AST,
 * holding a list of procedures
 * and a final statement.
 * 
 * @author Linda Zeng
 * @version 4.12.2024
 */
public class Program {
    
    List<Variable> globVariables;
    List<ProcedureDeclaration> procedures;
    Statement finalStatement;
    
    /**
     * Instantiates a Program
     * object.
     * 
     * @param procedures    list of procedure declarations
     * @param finalStatement    final statement
     */
    public Program(List<Variable> globVariables, List<ProcedureDeclaration> procedures, Statement finalStatement)
    {
        this.globVariables = globVariables;
        this.procedures = procedures;
        this.finalStatement = finalStatement;
    }

    /**
     * Executes the program
     * by assigning procedure
     * declarations and executing
     * the final statement.
     * 
     * @param env   the environment holding variables and procedures.
     */
    public void exec(Environment env)
    {
        for (ProcedureDeclaration c: procedures)
        {
            c.exec(env);
        }
        finalStatement.exec(env);
    }

    /**
     * Writes the code
     * into the output file,
     * generating MIPS assembly
     * code from the parse tree.
     * 
     * @param outputFile    file to write the program in MIPS
     * @param e     Emitter object used to write to the file
     */
    public void compile(String outputFile, Emitter e)
    {
        e.emit("#This MIPS code is auto-generated from the Java compiler.");
        e.emit("#@author Linda Zeng");
        e.emit("#@version 5.14.24");
        e.emit(".data");
        for (Variable v: globVariables)
        {
            e.emit("var" + v.getName() + ":    .word 0");
        }
        e.emit(".text");
        e.emit(".globl main");
        e.emit("main:");
        finalStatement.compile(e);
        e.emit("li $v0 10");
        e.emit("syscall");
        for (ProcedureDeclaration c: procedures)
        {
            c.compile(e);
        }
    }
}
