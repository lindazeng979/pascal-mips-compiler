package ast;
import environment.Environment;
import emitter.Emitter;

/**
 * This class represents 
 * a variable name,
 * which has a corresponding value
 * in the environment,
 * and is used when variables
 * are used in expressions or statements.
 * 
 * @author Linda Zeng
 * @version 3.21.24
 */
public class Variable extends Expression
{
    private String name;

    /**
     * Instantiates the variable
     * object with the given name
     * 
     * @param name the name of the variable
     */
    public Variable(String name)
    {
        this.name = name;
    }

    /**
     * Returns the variable name
     * @return String variable name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the value
     * of the variable
     * 
     * @param env the environment holding the variable
     *            and its value
     * @return the int value of the variable
     */
    public int eval(Environment env)
    {
        return env.getVariable(name);
    }

    /**
     * Emits the sequence
     * of MIPS instructions
     * corresponding to the
     * variable declarion using the Emitter,
     * initializing them as "var"+variable name
     * with value 0
     * Loads the variable value into $v0
     * 
     * @postcondition the last compiled result is in $v0
     * 
     * @param e the emitter to write the instructions
     */
    public void compile(Emitter e)
    {
        if (e.isLocalVariable(name))
        {
            int offset = e.getOffset(name);
            e.emit("lw $v0 " + offset + "($sp)"); 
        }
        else
        {
            e.emit("la $t0 var" + name);
            e.emit("lw $v0 ($t0)");
        }
    }

}
