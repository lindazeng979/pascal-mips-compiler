package ast;
import environment.Environment;
import emitter.Emitter;

/**
 * This class represents a While
 * loop, which can iterately
 * perform statements
 * while a given condition is true.
 * 
 * @author Linda Zeng
 * @version 3.21.24
 */
public class While extends Statement
{
    private Condition condition;
    private Statement stmt;

    /**
     * Instantiates
     * a While object.
     * 
     * @param condition condition to be checked for
     * @param stmt  statements to execute
     */
    public While(Condition condition, Statement stmt)
    {
        this.condition = condition;
        this.stmt = stmt;
    }

    /**
     * Iteratively executes
     * statements in the while
     * loop as long as condition is true.
     * 
     * @param env Environment holding variables
     */
    public void exec(Environment env)
    {
        while (condition.eval(env) == 1)
        {
            stmt.exec(env);
        }
    }
    
    /**
     * Emits the sequence
     * of MIPS instructions
     * corresponding to the
     * while loop
     * 
     * @postcondition the last compiled result is in $v0
     * 
     * @param e the emitter to write the instructions
     */
    public void compile(Emitter e)
    {
        int id = e.nextLabelID();
        String endLoop = "endLoop" + id;
        String loop = "loop" + id;
        e.emit(loop+":");
        condition.compile(e,endLoop);
        stmt.compile(e);
        e.emit("j " + loop);
        e.emit(endLoop+":");
    }
}
