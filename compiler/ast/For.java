package ast;
import environment.Environment;

/**
 * This class represents a For
 * loop, which can iterately
 * perform statements
 * according to an iterator
 * variable.
 * 
 * @author Linda Zeng
 * @version 3.21.24
 */
public class For extends Statement
{
    private String iterator;
    private int lower;
    private int upper;
    private Statement stmts;   

    /**
     * Instantiates a For
     * object. 
     * 
     * @param iterator variable determining iteration
     * @param lower int the iterator begins at
     * @param upper int the iterator ends at
     * @param stmts statements to be repeated
     */
    public For(String iterator, int lower, int upper, Statement stmts)
    {
        this.iterator = iterator;
        this.lower = lower;
        this.upper = upper;
        this.stmts = stmts;
    }

    /**
     * Executes the for loop,
     * including each statement
     * in the for loop. The iterator
     * can either increase or decrease
     * depending on lower and upper values
     * of the iterator.
     * 
     * @param env Environment holding variables
     */
    public void exec(Environment env)
    {
        if (upper > lower)
        {
            for (int i = lower; i <= upper; i++)
            {
                env.setVariable(iterator,i);
                stmts.exec(env);
            }
        }
        else
        {
            for (int i = lower; i >= upper; i--)
            {
                env.setVariable(iterator,i);
                stmts.exec(env);
            } 
        }
    }
}
