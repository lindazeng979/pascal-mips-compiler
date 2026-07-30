package ast;
import java.util.*;
import environment.Environment;
import emitter.Emitter;

/**
 * This class represents
 *  a procedure
 * declaration which
 * creates a procedure
 * and corresponding statement
 * in the environment.
 * 
 * @author Linda Zeng
 * @version 4.12.2024
 */
public class ProcedureDeclaration {
    
    String procedureName;
    Statement procedure;
    List<Variable> parameters;
    List<Variable> localVar;
    
    /**
     * Instantiates
     * a procedure declaration
     * object.
     * 
     * @param procedureName the name of the procedure
     * @param procedure the statement the procedure represents
     * @param paremeters list of the parameters of the procedure
     */
    public ProcedureDeclaration(String procedureName, Statement procedure, List<Variable> parameters, List<Variable> localVar)
    {
        this.procedureName = procedureName;
        this.procedure = procedure;
        this.parameters = parameters;
        this.localVar = localVar;
    }

    /**
     * Returns the name of the procedure.
     * @return String representing procedure name 
     */
    public String getName()
    {
        return procedureName;
    }

    /**
     * Returns the procedure statement.
     * @return Statement representing procedure name.
     */
    public Statement getProcedure()
    {
        return procedure;
    }

    /**
     * Returns procedure parameters
     * @return List of parameter
     */
    public List<Variable> getParameters()
    {
        return parameters;
    }

    /**
     * Returns local variables
     * found at the first line of procedure
     * declaration
     * @return List of local variables
     */
    public List<Variable> getLocalVariables()
    {
        return localVar;
    }

    /**
     * Set procedure in environemnt
     * 
     * @param env   the environment holding variables and procedures.
     */
    public void exec(Environment env)
    {
        env.setProcedure(procedureName, this);
    }

    /**
     * Emits the sequence
     * of MIPS instructions
     * corresponding to the
     * procedure declaration
     * using the Emitter
     * 
     * @param e the emitter to write the instructions
     */
    public void compile(Emitter e)
    {
        e.emit("proc" + procedureName + ":");
        //pushing extra 0 onto stack for return value
        e.emit("li $v0 0");
        e.emitPush("$v0");
        e.setProcedureContext(this);
        e.emit("subu $sp, $sp, 4    #push return address to stack");
        e.emit("sw $ra, ($sp)");

        e.emit("#pushing local variables to stack");
        for (Variable v: localVar)
        {
            e.emit("li $v0 0");
            e.emitPush("$v0");
        }

        procedure.compile(e);
        e.emit("done"+procedureName + ":");
        e.emit("#popping local variables from stack");
        for (Variable v: localVar)
        {
            e.emitPop("$t0");
        }

        e.emit("lw $ra, ($sp)  #pop return address from stack");
        e.emit("addu $sp, $sp, 4");
        e.emit("jr $ra");
        e.clearProcedureContext();
    }

}
