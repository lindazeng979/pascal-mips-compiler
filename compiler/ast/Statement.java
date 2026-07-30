package ast;
import environment.Environment;
import emitter.Emitter;

/**
 * This abstract class represents a statement
 * node in the parsed file, inclduing
 * assignments, blocks, if-else, while, for,
 * readln, writeln.
 * 
 * @author Linda Zeng
 * @version 3.21.2024
 */
public abstract class Statement
{
    /**
     * Executes the statement.
     * 
     * @param env   Environment object holding variables
     */
    public abstract void exec(Environment env);

    /**
     * Emits the sequence
     * of MIPS instructions
     * corresponding to the
     * statement using the Emitter
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
