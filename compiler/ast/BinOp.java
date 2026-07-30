package ast;
import environment.Environment;
import emitter.Emitter;
import java.util.*;

/**
 * This class represents binary operations
 * including +, -, *, and /, between
 * various other expressions.
 * 
 * @author Linda Zeng
 * @version 3.21.24
 */
public class BinOp extends Expression
{
    private String op;
    private Expression exp1;
    private Expression exp2;

    /**
     * Instantiates 
     * a binary operation
     * object with nodes
     * to each expression.
     * 
     * @param op    the String representing the operation
     * @param exp1  the first Expression in the operation
     * @param exp2  the second Expression in the operation
     */
    public BinOp(String op, Expression exp1, Expression exp2)
    {
        this.op = op;
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    /**
     * Performs the operation with
     * the evaluated expressions
     * 
     * @precondition expressions are not null
     * @postcondition expressions instance variables are not 
     *                changed (not permanently evaluated)
     * @param env   Environment object representing
     *              the environment holding variables
     * @return  the int result of the operation
     */
    public int eval(Environment env)
    {
        if(op.equals("+"))
        {
            return exp1.eval(env) + exp2.eval(env);
        }
        else if(op.equals("-"))
        {
            return exp1.eval(env) - exp2.eval(env);
        }
        else if(op.equals("*"))
        {
            return exp1.eval(env) * exp2.eval(env);
        }
        else
        {
            return exp1.eval(env) / exp2.eval(env);
        }
    }

     /**
     * Emits the sequence
     * of MIPS instructions
     * corresponding to the
     * binOp using the Emitter
     * 
     * @postcondition the compiled result is in $v0
     * 
     * @param e the emitter to write the instructions
     */
    public void compile(Emitter e)
    {
        exp1.compile(e);
        e.emitPush("$v0");
        exp2.compile(e);    
        e.emitPop("$t0"); //exp1
        Map<String, String> map = new HashMap<String, String>();
        map.put("+", "addu");
        map.put("-", "subu");
        map.put("*", "mul");
        map.put("/", "div");
        e.emit(map.get(op) + " $v0 $t0 $v0"); 
    }
}
