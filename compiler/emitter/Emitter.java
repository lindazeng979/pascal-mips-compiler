package emitter;
import java.io.*;
import ast.*;
import java.util.*;

public class Emitter
{
	private PrintWriter out;
	private int ifCounter;
	private ProcedureDeclaration curProc;
	private int excessStackHeight;

	//creates an emitter for writing to a new file with given name
	public Emitter(String outputFileName)
	{
		try
		{
			out = new PrintWriter(new FileWriter(outputFileName), true);
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
		ifCounter = 0;
		curProc = null;
		excessStackHeight = 0;
	}

	//prints one line of code to file (with non-labels indented)
	public void emit(String code)
	{
		if (!code.endsWith(":"))
			code = "\t" + code;
		out.println(code);
	}

	//closes the file.  should be called after all calls to emit.
	public void close()
	{
		out.close();
	}

	/**
	 * Pushes the value
	 * in reg to stack
	 * in memory
	 * 
	 * @param reg	register containing value
	 */
	public void emitPush(String reg)
	{
		emit("subu $sp $sp 4		#push $v0");
		emit("sw " + reg + " ($sp)");
		excessStackHeight = excessStackHeight + 4;
	}

	/**
	 * Pops the top of
	 * the stack into
	 * reg
	 * 
	 * @param reg	register to store value
	 */
	public void emitPop(String reg)
	{
		emit("lw " + reg + " ($sp)		#pop $v0");
		emit("addu $sp $sp 4");
		excessStackHeight = excessStackHeight - 4;
	}

	/**
	 * Counts label
	 * names to branch to for
	 * if statements.
	 * 
	 * @return int 1 the first time it's called,
	 * 			2 the second time, 3 the third, ...
	 */
	public int nextLabelID()
	{
		ifCounter++;
		return ifCounter;
	}

	/**
	 * Sets the current
	 * procedure context
	 * to the current
	 * procedure.
	 * 
	 * @param proc	sets this declaration to curProc
	 */
	public void setProcedureContext(ProcedureDeclaration proc)
	{
		curProc = proc;
		excessStackHeight = 0;
	}

	/**
	 * Clears procedure context
	 * to null.
	 */
	public void clearProcedureContext()
	{
		curProc = null;
		excessStackHeight = 0;
	}

	/**
	 * Checks if the
	 * given variable is
	 * local to the current procedure
	 * 
	 * @param varName var name to be checked
	 * @return	true it is local, false otherwise
	 */
	public boolean isLocalVariable(String varName)
	{
		if (curProc == null)
		{
			return false;
		}
		for(Variable p: curProc.getParameters())
		{
			if (p.getName().equals(varName) || varName.equals(curProc.getName()))
			{
				return true;
			}
		}
		for(Variable p: curProc.getLocalVariables())
		{
			if (p.getName().equals(varName))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines the index of
	 * parameter whose name matches
	 * given localVarName,
	 * and maps that index
	 * to the corresponding offset.
	 * Returns to offset.
	 * 
	 * @precondition:	localVarName
	 * 					is the name of a local variable
	 * 					for the procedure currently compiled
	 * @param localVarName	String which is local variable name
	 * @return	integer of the offset from $sp
	 */
	public int getOffset(String localVarName)
	{
		int largest = 4 * (curProc.getParameters().size() + 1); //curProc.getLocalVariables().size()
		int index = 0;
		boolean local = false;
		for(Variable p: curProc.getLocalVariables())
		{
			if (p.getName().equals(localVarName))
			{
				local = true;
				break;
			}
			index++;
		}
		if (local)
		{
			return (largest + excessStackHeight - (4 * (curProc.getParameters().size() + 2)) - index * 4);
		}
		//NEVER GETS HERE
		else
		{
			index = 0;
			for(Variable p: curProc.getParameters())
			{	
				if (p.getName().equals(localVarName))
				{
					break;
				}
				index++;
			}
			return (largest - index * 4 + excessStackHeight); 
		}
	}
}