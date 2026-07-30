package ast;
import environment.Environment;
import java.util.Scanner;

/**
 * This class represents a
 * READLN(x) statement,
 * taking in a variable x
 * and assigning its value
 * as a number from the user's input.
 * 
 * @author Linda Zeng
 * @version 3.21.24
 */
public class Readln extends Statement
{
    private String varName;

    /**
     * Instantiates the Readln object
     * 
     * @param varName   the name of the variable
     */
    public Readln(String varName)
    {
        this.varName = varName;
    }

    /**
     * Assigns the variable name 
     * to the user input's number
     * and puts them in the environment.
     * 
     * @param env   the environment holding the variable
     */
    public void exec(Environment env) 
    {
        Scanner reader = new Scanner(System.in);  
        System.out.print("Enter a number: ");
        String input;
        if (reader.hasNext())
        {
            input = reader.nextLine();
            env.setVariable(varName, Integer.parseInt(input));
        }
        reader.close();
    }
}
