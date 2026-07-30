package ast;
import environment.Environment;
import emitter.Emitter;

/**
 * This class represents
 * a Writeln statement,
 * which prints an evaluated
 * expression to output terminal
 * 
 * @name Linda Zeng
 * @version 3.18.24
 */
public class Writeln extends Statement
{
    private Expression exp;

    /**
     * Instantiates a Writeln
     * object.
     * 
     * @param exp   Expression to be printed
     */
    public Writeln(Expression exp)
    {
        this.exp = exp;
    }

    /**
     * Prints the evaluated
     * expression, without changing
     * the instance variable's value.
     * 
     * @param env   Environment holding variables
     */
    public void exec(Environment env)
    {
        System.out.println(exp.eval(env));
    }

    /**
     * Emits the sequence
     * of MIPS instructions
     * corresponding to the
     * writeln using the Emitter
     * Emits the writeln expression
     * and a new line.
     * 
     * @postcondition the compiled result is in $v0,
     *                which is 11 to print new line
     * 
     * @param e the emitter to write the instructions
     */
    public void compile(Emitter e)
    {
        exp.compile(e);
        e.emit("#prints value with a new line after");
        e.emit("move $a0 $v0");
        e.emit("li $v0 1");
        e.emit("syscall");
        e.emit("li $v0 11");
        e.emit("li $a0 10");
        e.emit("syscall");
    }
}
