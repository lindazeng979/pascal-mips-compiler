package ast;
import environment.Environment;
import emitter.Emitter;

/**
 * This class represents a condition
 * in IF and WHILE blocks, and consists of
 * of expressions and relational
 * operators.
 * 
 * @author Linda Zeng
 * @version 3.21.24
 */
public class Condition extends Expression
{
    private String relop;
    private Expression exp1;
    private Expression exp2;
    
    /**
     * Instantiates
     * a Condition object
     * with associated parameters.
     * 
     * @param relop String representing relational operator
     * @param exp1  first Expression object
     * @param exp2  second Expression object 
     */
    public Condition(String relop, Expression exp1, Expression exp2)
    {
        this.relop = relop;
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    /**
     * Evaluates the Condition,
     * returning 1 if it's true
     * and 0 if it's false.
     * 
     * @param env Environment object holding variables
     * @return 1 if true, 0 if false
     *         returns 0 if relop invalid
     */
    public int eval(Environment env)
    {
        if (relop.equals("="))
        {
            if (exp1.eval(env) == exp2.eval(env))
            {
                return 1;
            }
            return 0;
        }
        else if (relop.equals("<>"))
        {
            if (exp1.eval(env) != exp2.eval(env))
            {
                System.out.println("true");
                return 1;
            }
            return 0;
        }
        else if (relop.equals("<"))
        {
            if (exp1.eval(env) < exp2.eval(env))
            {
                return 1;
            }
            return 0;
        }
        else if (relop.equals(">"))
        {
            if (exp1.eval(env) > exp2.eval(env))
            {
                return 1;
            }
            return 0;
        }
        else if (relop.equals("<="))
        {
            if (exp1.eval(env) <= exp2.eval(env))
            {
                return 1;
            }
            return 0;
        }
        else if (relop.equals(">="))
        {
            if (exp1.eval(env) >= exp2.eval(env))
            {
                return 1;
            }
            return 0;
        }
        else 
        {
            return 0;
        }
    }

    /**
     * Emits the sequence
     * of MIPS instructions
     * corresponding to the
     * condition.
     * 
     * @postcondition the last compiled result is in $v0
     * 
     * @param e the emitter to write the instructions
     */
    public void compile(Emitter e, String targetLabel)
    {
        exp1.compile(e);
        e.emitPush("$v0");
        exp2.compile(e);
        e.emitPop("$t0");
        if (relop.equals("="))
        {
            e.emit("bne $t0 $v0 " + targetLabel);
        }
        else if (relop.equals("<>"))
        {
            e.emit("beq $t0 $v0 " + targetLabel);
        }
        else if (relop.equals("<"))
        {
            e.emit("bge $t0 $v0 " + targetLabel);
        }
        else if (relop.equals(">"))
        {
            e.emit("ble $t0 $v0 " + targetLabel);
        }
        else if (relop.equals("<="))
        {
            e.emit("bgt $t0 $v0 " + targetLabel);
        }
        else
        {
            e.emit("blt $t0 $v0 " + targetLabel);
        }
    }
}
