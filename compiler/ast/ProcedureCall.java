package ast;
import environment.*;
import java.util.*;
import emitter.Emitter;

/**
 * This class represents
 * a call to a procedure
 * which is an option for
 * factor.
 * It calls and evaluates
 * the procedure.
 * 
 * @author Linda Zeng
 * @version 4.12.2024
 */
public class ProcedureCall extends Expression {
    
    String procedureName;
    List<Expression> params;

    /**
     * Instantiates a ProcedureCall object.
     * 
     * @param procedureName
     * @param params list of expressions representing param values
     */
    public ProcedureCall(String procedureName, List<Expression> params)
    {
        this.procedureName = procedureName;
        this.params = params;
    }

    /**
     * Calls the procedure
     * from the environment
     * and evaluates the
     * statements in the procedure
     * 
     * @precondition number of params declared = number of params in call
     * @param env   the environment holding variables and procedures
     * @return  the value of the return variable
     *          0 if it was not modified
     */
    public int eval(Environment env)
    {
        Environment newEnv = new Environment(env);
        newEnv.declareVariable(procedureName, 0);
        ProcedureDeclaration declaration = env.getProcedure(procedureName);
        List<Variable> paramNames = declaration.getParameters();
        for (int i = 0; i < params.size(); i++)
        {
            newEnv.declareVariable(paramNames.get(i).getName(), params.get(i).eval(env));
        }
        declaration.getProcedure().exec(newEnv);
        return newEnv.getVariable(procedureName);
    }

        /**
     * Emits the sequence
     * of MIPS instructions
     * corresponding to the
     * procedure call
     * using the Emitter
     * 
     * @postcondition the last compiled result 
     *                 (return of the procedure)
     *                  is in $v0
     * 
     * @param e the emitter to write the instructions
     */
    public void compile(Emitter e)
    {
        e.emit("#pushing parameters to memory");
        for (Expression p: params)
        {
            p.compile(e);
            e.emitPush("$v0");
        }
        e.emit("jal proc" + procedureName);
        e.emit("#pop return value");
        e.emitPop("$v0");
        for (Expression p: params)
        {
            e.emitPop("$t0"); //discarded
        }
    }
    
}
