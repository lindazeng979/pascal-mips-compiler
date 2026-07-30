package ast;
import environment.Environment;
import emitter.Emitter;

/**
 * This class represents an If
 * statement which can optionally
 * include an else. 
 * 
 * @author Linda Zeng
 * @version 3.21.24
 */
public class IfElse extends Statement
{
    private Condition condition;
    private Statement then;
    private Statement elseStatement;

    /**
     * Instantiates an Ifelse
     * object.
     * @param condition condition for the if statement
     * @param then  statement to be performed if condition is met
     * @param elseStatement statement to be performed
     *                      if condition is not met; null if there
     *                      is no else
     */
    public IfElse(Condition condition, Statement then, Statement elseStatement)
    {
        this.condition = condition;
        this.then = then;
        this.elseStatement = elseStatement;
    }

    /**
     * Executes the if statement,
     * without changing the value of
     * the condition variable.
     *
     * @param env Environment holding variables
     */
    public void exec(Environment env)
    {
        if (condition.eval(env) == 1)
        {
            then.exec(env);
        }
        else if (elseStatement != null)
        {
            elseStatement.exec(env);
        }
    }

    /**
     * Emits the sequence
     * of MIPS instructions
     * corresponding to the
     * if else.
     * 
     * @postcondition the last compiled result is in $v0
     * 
     * @param e the emitter to write the instructions
     */
    public void compile(Emitter e)
    {
        int id = e.nextLabelID();
        String endIf = "endif" + id;
        if (elseStatement != null)
        {
            String elseLabel = "else" + id;
            condition.compile(e,elseLabel);
            then.compile(e);
            e.emit("j " + endIf);
            e.emit(elseLabel+":");
            elseStatement.compile(e);
        }
        else
        {
            condition.compile(e,endIf);
            then.compile(e);
        }
        e.emit(endIf+":");
    }
}
