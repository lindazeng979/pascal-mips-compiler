package parser;
import scanner.ScanErrorException;
import scanner.Scanner;

import java.io.*;
import java.util.*;

import ast.*;
import environment.Environment;
import emitter.Emitter;

/**
 * This class is a parser for the second step of
 * the syntax analysis in a compiler).
 * It checks a sequence of tokens to see
 * if it follows the grammar for
 * a series of mathematical operations
 * and returns an abstract syntax
 * tree of nodes
 * that represents the hierarchy of
 * expressions
 * and assignments. 
 * 
 * This class includes parsing statements
 * (assignments, blocks, if-else, while, for,
 * readln, writeln)
 * and expressions (numbers, variables, binary
 * operators, conditions), which are
 * parsed by number, term, factor, and expression.
 * 
 * This class also includes a main function to test
 * the scanner + parser.
 * 
 * @author Linda Zeng
 * @version 3.21.2024
 */
public class Parser
{
    
    Scanner scanner;
    String curToken; 

    /**
     * Constructs a parser variable,
     * instantiating the scanner from the input,
     * the current token as the first scanned
     * token if any, and an empty map for variables.
     * 
     * @param scannerParam  the lexical analyzer
     *                      that scans tokens for
     *                      the parser to parse
     * @throws ScanErrorException if scanning has an error
     */
    public Parser(Scanner scannerParam) throws ScanErrorException
    {
        scanner = scannerParam;
        if (scanner.hasNext())
        {
            curToken = scanner.nextToken();
        }
    }

    /**
     * Advances input by
     * eating the current token
     * in the stream if
     * it matches the expected
     * token value.
     * 
     * @param tok    the expected token
     * @throws IllegalArgumentException if the expected token
     *                                  does not match the current token
     * @throws ScanErrorException if there is a scanning error
     */
    private void eat(String tok) throws IllegalArgumentException, ScanErrorException
    {
        if (tok.equals(curToken))
        {
            if (scanner.hasNext())
            {
                curToken = scanner.nextToken();
            }
        }
        else
        {
            throw new IllegalArgumentException("expected token: " + tok +
                                             " is different from current token found: " + curToken);
        }
    }

    /**
     * Parses a program, the highest
     * level of the file, including
     * procedure declarations and one statement
     * at the end.
     * 
     * @return  the program root node
     * @throws ScanErrorException   if there is a scanning error
     */
    public Program parseProgram() throws ScanErrorException
    {
        List<Variable> globVariables = parseVariables();
        List<ProcedureDeclaration> procedureDecs = new ArrayList<ProcedureDeclaration>();
        while (curToken.equals("PROCEDURE"))
        {
            procedureDecs.add(parseProcedureDeclaration());
        }
        Statement finalStatement = parseStatement();
        return new Program(globVariables, procedureDecs, finalStatement);
    }

    /**
     * Parses the variables
     * in the form VAR a, b, c;
     * and returns them in
     * a list.
     * 
     * @return ArrayList of variable objects,
     *          empty if there is no VAR
     * @throws ScanErrorException   if scanning error
     */
    public List<Variable> parseVariables() throws ScanErrorException
    {
        List<Variable> variables = new ArrayList<Variable>();
        while (curToken.equals("VAR"))
        {
            eat("VAR");
            variables.add(new Variable(curToken));
            eat(curToken);
            while (!curToken.equals(";"))
            {
                eat(",");
                variables.add(new Variable(curToken));
                eat(curToken);
            }
            eat(";");
        }
        return variables;
    }

    /**
     * Parses a ProcedureDeclaration
     * and returns it as an object.
     * 
     * @precondition the cur token is "PROCEDURE"
     * @return a Procedure Declaration object
     * @throws ScanErrorException   if scanning error
     */
    public ProcedureDeclaration parseProcedureDeclaration() throws ScanErrorException
    {
        eat("PROCEDURE");
        String name = curToken;
        eat(name);
        eat("(");
        List<Variable> params = new ArrayList<Variable>();
        if (!curToken.equals(")"))
        {
            params.add(new Variable(curToken));
            eat(curToken);
        }
        while (curToken.equals(","))
        {
            eat(",");
            if (!curToken.equals(")"))
            {
                params.add(new Variable(curToken));
                eat(curToken);
            }
        }
        eat(")");
        eat(";");
        List<Variable> variables = parseVariables();
        Statement procedure = parseStatement();
        return new ProcedureDeclaration(name, procedure, params, variables);  
    }

    /**
     * Parses the entire file,
     * including the WRITELN statement, 
     * blocks of statements between BEGIN
     * and END, IF-DO-THEN statements, FOR statements,
     * WHILE statements, READLN, variable
     * assignments, and any of the above
     * nested in each other.
     * 
     * @precondition: the current token is the first token
     * @postcondition: the entire file is parsed
     * @return a Statement object representing the entire file's
     *         productions
     * @throws ScanErrorException if there is a scanning error
     *                            or if user inputs nothing
     *                            for readln
     */
    public Statement parseStatement() throws ScanErrorException
    {
        if(curToken.equals("WRITELN"))
        {
            eat("WRITELN");
            eat("(");
            Expression exp = parseExpression();
            eat(")");
            eat(";");
            return new Writeln(exp);
        }
        else if (curToken.equals("BEGIN"))
        {
            List<Statement> stmts = new ArrayList<Statement>();
            eat("BEGIN");
            while (!curToken.equals("END"))
            {
                stmts.add(parseStatement());
            }
            eat("END");
            eat(";");
            return new Block(stmts);
        }
        else if (curToken.equals("IF"))
        {
            eat("IF");
            Expression exp1 = parseExpression();
            String relop = curToken;
            eat(relop);
            Expression exp2 = parseExpression();
            Condition cond = new Condition(relop, exp1, exp2);
            eat("THEN");
            Statement then = parseStatement();
            Statement elseStatement = null;
            if (curToken.equals("ELSE"))
            {
                eat("ELSE");
                elseStatement = parseStatement();
            }
            return new IfElse(cond, then, elseStatement);
        }
        else if (curToken.equals("FOR"))
        {
            eat("FOR");
            String var = curToken;
            eat(var);
            eat(":=");
            int lower = Integer.parseInt(curToken);
            eat(curToken);
            eat("TO");
            int upper = Integer.parseInt(curToken);
            eat(curToken);
            eat("DO");
            Statement stmts = parseStatement();
            return new For(var, lower, upper, stmts);
        }
        else if (curToken.equals("WHILE"))
        {
            eat("WHILE");
            Expression exp1 = parseExpression();
            String relop = curToken;
            eat(relop);
            Expression exp2 = parseExpression();
            Condition cond = new Condition(relop, exp1, exp2);
            eat("DO");
            Statement stmt = parseStatement();
            return new While(cond, stmt);
        }
        else if (curToken.equals("READLN"))
        {
            eat("READLN");
            eat("(");
            String var = curToken;
            eat(var);
            eat(")");
            eat(";");
            return new Readln(var);
        }
        else
        {
            String var = curToken;
            eat(var);
            eat(":=");
            Expression exp = parseExpression();
            eat(";");
            return new Assignment(var, exp);
        }
    }

    /**
    * Parses a number.
    *
    * @precondition: current token is an integer
    * @postcondition: number token has been eaten
    * @return an Number object representing the num parsed
    * @throws ScanErrorException if there is a scanning error
    */
    private ast.Number parseNumber() throws ScanErrorException
    {
        int num = Integer.parseInt(curToken);
        eat(curToken);
        return new ast.Number(num);
    }

    /**
     * Parses a factor,
     * including numbers with 
     * negative signs or parentheses,
     * variable or procedure calls.
     * 
     * @precondition: current token is the beginning
     *              of a factor 
     * @postcondition: factor has been eaten, but not ;
     * 
     * @return an Expression object representing
     *         a Number, a Variable, ProcedureCall or
     *         a BinOp for negative values
     * @throws IllegalArgumentException if the expected char is not correct
     *                                  (syntax error)
     * @throws ScanErrorException if there is a scanning error
     */
    private Expression parseFactor() throws IllegalArgumentException,ScanErrorException
    {
        if (curToken.equals("("))
        {
            eat("(");
            Expression factor = parseExpression();
            eat(")");
            return factor;
        }
        else if (curToken.equals("-"))
        {
            eat("-");
            return new BinOp("-",new ast.Number(0),parseFactor());
        }
        else if (Character.isLetter(curToken.charAt(0)))
        {
            String name = curToken;
            eat(curToken);
            List<Expression> params = new ArrayList<Expression>();
            if (curToken.equals("("))
            {
                eat("(");
                if (!curToken.equals(")"))
                {
                    params.add(parseExpression());
                }
                while (curToken.equals(","))
                {
                    eat(",");
                    if (!curToken.equals(")"))
                    {
                        params.add(parseExpression());
                    }
                }
                eat(")");
                return new ProcedureCall(name, params);
            }
            return new Variable(name);
        }
        else
        {
            return parseNumber();
        }
    }

    /**
     * Parses a term
     * including multiplication 
     * and division of factors,
     * or just a factor.
     * stmt → WRITELN ( term ) ;
     * term → term * factor | term / factor | factor
     * factor → ( term ) | - factor | num
     * 
     * @precondition: current token is the beginning
     *               of a term
     * @postcondition: term has been eaten
     * 
     * @return an Expression object representing
     *         an operation with * or / (BinOp)
     *         or a number (Number)
     * @throws IllegalArgumentException if the expected char is not correct
     *                                  (syntax error)
     * @throws ScanErrorException if there is a scanning error
     */
    private Expression parseTerm() throws IllegalArgumentException,ScanErrorException
    {
        Expression res = parseFactor();
        while (curToken.equals("*") || curToken.equals("/"))
        {
            if (curToken.equals("*"))
            {
                eat("*");
                res = new BinOp("*",res,parseFactor());
            }
            if (curToken.equals("/"))
            {
                eat("/");
                res = new BinOp("/",res,parseFactor());
            }
        }
        return res;
    }

    /**
     * Parses an expression
     * including addition 
     * and division of terms
     * according to this production:
     * stmt → WRITELN ( expr ) ;
     * expr → expr + term | expr - term | term
     * term → term * factor | term / factor | factor
     * factor → ( expr ) | - factor | num
     * 
     * 
     * @precondition: current token is the beginning
     *               of an expression
     * @postcondition: expression has been eaten
     * 
     * @return an Expression object representing
     *         an operation with + or -, or a number
     * @throws IllegalArgumentException if the expected char is not correct
     *                                  (syntax error)
     * @throws ScanErrorException if there is a scanning error
     */
    private Expression parseExpression() throws IllegalArgumentException,ScanErrorException
    {
        Expression res = parseTerm();
        while (curToken.equals("+") || curToken.equals("-"))
        {
            if (curToken.equals("-"))
            {
                eat("-");
                res = new BinOp("-",res,parseTerm());
            }
            if (curToken.equals("+"))
            {
                eat("+");
                res = new BinOp("+",res,parseTerm());
            }
        }
        return res;
    }

    /**
     * Locates the bundled default test program, so the compiler
     * can be run with no arguments from either the repository
     * root or from inside the compiler directory.
     *
     * @return the default input file, or null if none was found
     */
    private static File defaultInput()
    {
        String[] candidates = {"compiler/parser/parserTest10.txt",
                               "parser/parserTest10.txt",
                               "parserTest10.txt"};
        for (String c: candidates)
        {
            File f = new File(c);
            if (f.exists())
            {
                return f;
            }
        }
        return null;
    }

    /**
     * Derives an output path from an input path by swapping the
     * extension for .asm, leaving the file in the same directory.
     *
     * @param inputFile the source file being compiled
     * @return the file the generated assembly should be written to
     */
    private static File defaultOutput(File inputFile)
    {
        String name = inputFile.getName();
        int dot = name.lastIndexOf('.');
        if (dot > 0)
        {
            name = name.substring(0, dot);
        }
        return new File(inputFile.getAbsoluteFile().getParentFile(), name + ".asm");
    }

    /**
     * Main method, compiles a Pascal-subset source
     * file into MIPS assembly.
     *
     * Usage: java parser.Parser [input.txt] [output.asm]
     * With no arguments, compiles the bundled parserTest10.txt.
     * With no output argument, writes next to the input as .asm.
     *
     * @param args optional input path, then optional output path
     */
    public static void main(String[] args)
    {
        try
        {
            File inputFile = (args.length > 0) ? new File(args[0]) : defaultInput();
            if (inputFile == null || !inputFile.exists())
            {
                System.out.println("Usage: java parser.Parser <input.txt> [output.asm]");
                return;
            }
            File outputFile = (args.length > 1) ? new File(args[1]) : defaultOutput(inputFile);
            InputStream in = new FileInputStream(inputFile);
            Emitter e = new Emitter(outputFile.getPath());
            Scanner myScanner = new Scanner(in);
            Parser myParser = new Parser(myScanner);
            myParser.parseProgram().compile(outputFile.getPath(), e);
            e.close();
            System.out.println("Compiled " + inputFile.getPath() + " -> " + outputFile.getPath());
        }
        catch (FileNotFoundException e)
        {
            System.out.println("FileNotFoundException: No file found");
        }
        catch (ScanErrorException e)
        {
            System.out.println(e.getMessage());
        }
    }
}