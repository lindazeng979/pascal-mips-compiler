package ast;
import environment.Environment;
import emitter.Emitter;

/**
 * This class represents assignments
 * in the form:
 * var := value;
 * and sets variables in the environment.
 * 
 * @author Linda Zeng
 * @version 3.21.24
 */
public class Assignment extends Statement
{
    private String var;
    private Expression exp;

    /**
     * Instantiates Assignment
     * with variable name and
     * expression representing value
     * 
     * @param var   String name of variable
     * @param exp   Expression representing value
     */
    public Assignment(String var, Expression exp)
    {
        this.var = var;
        this.exp = exp;
    }

    /**
     * Executes assignment,
     * placing variable name and
     * evaluated expression value
     * in the Environment.
     * 
     * @param env   Environment storing 
     *              the variable from assignment
     */
    public void exec(Environment env)
    {
        env.setVariable(var, exp.eval(env));
    }

    /**
     * Emits the sequence
     * of MIPS instructions
     * corresponding to the
     * variable assignment
     * by compiling the 
     * value and storing
     * it to the variable.
     * 
     * @postcondition the last compiled result is in $v0
     * 
     * @param e the emitter to write the instructions
     */
    public void compile(Emitter e)
    {
        exp.compile(e);
        if (e.isLocalVariable(var))
        {
            int offset = e.getOffset(var);
            e.emit("sw $v0 " + offset + "($sp)");
        }
        else
        {
            e.emit("sw $v0 var"+ var);
        }
    }
}
