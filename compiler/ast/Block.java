package ast;
import java.util.List;
import environment.Environment;
import emitter.Emitter;

/**
 * This class represents blocks
 * of statements beginning
 * with BEGIN and ending with END.
 * 
 * @author Linda Zeng
 * @version 3.21.24
 */
public class Block extends Statement
{
    private List<Statement> stmts;

    /**
     * Instantiates 
     * a Block object
     * with a list of statements
     * 
     * @param stmts List of statements
     */
    public Block(List<Statement> stmts)
    {
        this.stmts = stmts;
    }

    /**
     * Executes each statement
     * in the block.
     * 
     * @param env Environment object holding variables
     */
    public void exec(Environment env)
    {
        for (Statement s: stmts)
        {
            s.exec(env);
        }
    }

    /**
     * Emits the sequence
     * of MIPS instructions
     * corresponding to the
     * block using the Emitter
     * 
     * @postcondition the last compiled result is in $v0
     * 
     * @param e the emitter to write the instructions
     */
    public void compile(Emitter e)
    {
        for (Statement s: stmts)
        {
            s.compile(e);
        }
    }
}
