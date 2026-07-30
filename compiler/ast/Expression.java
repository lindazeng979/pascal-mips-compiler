package ast;
import environment.Environment;
import emitter.Emitter;

/**
 * This abstract class represents expression
 * nodes in the parsed file, including numbers, variables,
 * binary operators, and conditions before evaluation.
 * 
 * @author Linda Zeng
 * @version 3.21.24
 */
public abstract class Expression
{
    /**
     * Evaluates the expression
     * for a single int value.
     * 
     * @param env   Environment holding variables
     * @return  the evaluated expression as an int
     */
    public abstract int eval(Environment env);
    
    /**
     * Emits the sequence
     * of MIPS instructions
     * corresponding to the
     * expression using the Emitter
     * 
     * @postcondition the compiled result is in $v0
     * 
     * @param e the emitter to write the instructions
     */
    public void compile(Emitter e)
    {
        throw new RuntimeException("Implement me!!!!");
    }
}
