package ast;
import environment.Environment;
import emitter.Emitter;

/**
 * This class represents 
 * any integer number.
 * 
 * @author Linda Zeng
 * @version 3.21.24
 */
public class Number extends Expression
{
    private int value;

    /**
     * Instantiates a Number
     * object with the given value.
     * 
     * @param value the integer the Number
     *              represents
     */
    public Number(int value)
    {
        this.value = value;
    }

    /**
     * Returns the value.
     * 
     * @param env Environment holding variables
     * @return the integer value
     *          of the number
     */
    public int eval(Environment env)
    {
        return value;
    }

    /**
     * Emits the sequence
     * of MIPS instructions
     * corresponding to the
     * number using the Emitter
     * 
     * @postcondition the compiled result is in $v0
     * 
     * @param e the emitter to write the instructions
     */
    public void compile(Emitter e)
    {
        e.emit("li $v0 "+ value);
    }

}
