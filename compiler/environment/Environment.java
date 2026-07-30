package environment;
import java.util.*;
import ast.*;

/**
 * This class 
 * stores variables,
 * including the String name and
 * their corresponding 
 * Integer values, and procedures,
 * including the String name
 * and corresponding ProcedureDeclaration
 * object,
 * for a given file being
 * compiled.
 * 
 * @name Linda Zeng
 * @version 3.21.24
 */
public class Environment
{
    private Map<String,Integer> variables;
    private Map<String, ProcedureDeclaration> procedures;
    private Environment parent;

    /**
     * Instantiates an environment
     * object, instantiating
     * variables 
     * and procedures as empty maps
     * and parent to null
     * if no parameter given
     */
    public Environment()
    {
        variables = new HashMap<String,Integer>();
        procedures = new HashMap<String, ProcedureDeclaration>();
        parent = null;
    }

    /**
     * Instantiates an environment
     * object, instantiating
     * variables 
     * and procedures as empty maps
     * 
     * @param parent the parent Environment
     */
    public Environment(Environment parent)
    {
        variables = new HashMap<String,Integer>();
        procedures = new HashMap<String, ProcedureDeclaration>();
        this.parent = parent;
    }

    /**
     * Associates the given variable name
     * with the given value in the current
     * environment
     * 
     * @param variable  String variable name to be added
     * @param value integer variable value to be added
     */
    public void declareVariable(String variable, int value)
    {
        variables.put(variable, value);
    }

    /**
     * Returns the value associated with
     * the given value in the current variable.
     * If no variable in this environment,
     * searches parent. If never found, declares
     * a new variable with val 0.
     * 
     * @param variable  String name for the value needed
     * @return  integer value corresponding with the name
     *          or 0 if not found
     */
    public int getVariable(String variable)
    {
        //System.out.println("GET: " + variable);
        Environment temp = this;
        while (temp != null)
        {
            if (temp.variables.containsKey(variable))
            {
                return temp.variables.get(variable);
            }
            else
            {
                temp = temp.parent;
            }
        }
        declareVariable(variable, 0);
        return 0;
    }

    /**
     * Sets a variable's values
     * with a new value by finding
     * the variable present in the environment
     * closest to the current one.
     * if no variable found, adds a new variable
     * 
     * @param variable  String variable name to be set
     * @param value integer variable value to be set to
     */
    public void setVariable(String variable, int value)
    {
        //System.out.println("SET: " + variable + " " + value);
        Environment temp = this;
        while (temp != null)
        {
            if (temp.variables.containsKey(variable))
            {
                temp.variables.put(variable, value);
                return;
            }
            else
            {
                temp = temp.parent;
            }
        }
        variables.put(variable, value);
    }


    /**
     * Associates the given procedure name
     * with its procedure declaration object
     * in the global environment
     * 
     * @param name  String procedure name to be added
     * @param declaration ProcedureDeclaration object to be added
     */
    public void setProcedure(String name, ProcedureDeclaration declaration)
    {
        if (parent != null)
        {
            parent.setProcedure(name, declaration);
        }
        procedures.put(name, declaration);
    }

    /**
     * Returns the procedure declaration associated with
     * the given name in the global environment
     * 
     * @param variable  String name for the procedure
     * @return  corresponding procedure declaration object
     */
    public ProcedureDeclaration getProcedure(String name)
    {
        if (parent != null)
        {
            return parent.getProcedure(name);
        }
        return procedures.get(name);
    }
}
