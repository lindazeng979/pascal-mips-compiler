package scanner;
import java.io.*;


/**
 * Scanner reads in the input string, determines
 * individual lexemes according to a given
 * set of rules, and produces a string of
 * tokens. Each token is simply the actual lexeme.
 * In addition to performing lexical analysis,
 * it eliminates comments.
 * 
 * @author Linda Zeng
 * @version 1.26.2024
 */
public class Scanner
{
    private BufferedReader in;
    private char currentChar;
    private boolean eof;

    /**
     * Scanner constructor for construction of a scanner that 
     * uses an InputStream object for input.  
     * Usage: 
     * FileInputStream inStream = new FileInputStream(new File(<file name>);
     * Scanner lex = new Scanner(inStream);
     * @param inStream the input stream to use
     */
    public Scanner(InputStream inStream)
    {
        in = new BufferedReader(new InputStreamReader(inStream));
        eof = false;
        getNextChar();
    }
    /**
     * Scanner constructor for constructing a scanner that 
     * scans a given input string.  It sets the end-of-file flag an then reads
     * the first character of the input string into the instance field currentChar.
     * Usage: Scanner lex = new Scanner(input_string);
     * @param inString the string to scan
     */
    public Scanner(String inString)
    {
        in = new BufferedReader(new StringReader(inString));
        eof = false;
        getNextChar();
    }
    /**
     * Gets the next character from the input stream and
     * sets it as currentChar. Assigns eof to be true if 
     * at the end of file. Exits the program if the file 
     * contains no input
     */
    private void getNextChar()
    {
        try
        {
            int c = in.read();
            if (c == -1)
            {
                eof = true;
            }
            else
            {
                currentChar = (char)c;
            }
        }
        catch (IOException e)
        {
            System.out.println("File has no input.");
            System.exit(-1);
        }
    }
    /**
     * Advances to the next character
     * in the input stream as long as the
     * expected character is the current character.
     * 
     * @param expected character representing the
     *                  expected value of current char
     * @throws ScanErrorException if the expected value !=
     *                              current character
     */
    private void eat(char expected) throws ScanErrorException
    {
        if (expected == currentChar)
        {
            getNextChar();
        }
        else
        {
            throw new ScanErrorException("Illegal character - expected " +
                                        currentChar + " and found " + expected);
        }
    }

    /**
     * Checks if the input character
     * is a digit between 0 to 9.
     *  
     * @param c character to be checked
     * @return true if the character is a digit; false if not
     */
    public static boolean isDigit(char c)
    {
        return (c >= '0' && c <= '9');
    }

    /**
     * Checks if the input char 
     * is a letter (a-z or A-Z).
     * 
     * @param c character to be checked
     * @return true if the char is a letter; false if not
     */
    public static boolean isLetter(char c)
    {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    /**
     * Checks if the input char is
     * white space
     * (space, tab, new line, carriage return)
     * 
     * @param c character to be checked
     * @return true if the char is white space; false if not
     */
    public static boolean isWhiteSpace(char c)
    {
        return(c==' ' || c == '\t' || c =='\r' || c == '\n');
    }
    
    /**
     * Scans the
     * input and returns the
     * string of digits representing
     * the lexeme (number) found.
     * 
     * @precondition currentChar is a digit
     * @postcondition scanner has passed over
     *                all of the number, currentChar is
     *                the char after the number
     * 
     * @throws ScanErrorException if no lexeme is recognized
     */
    private String scanNumber() throws ScanErrorException
    {
        String token = "";
        while (isDigit(currentChar))
        {
            token += String.valueOf(currentChar);
            eat(currentChar);
        }
        return token;
    }

    /**
     * Scans the
     * input stream until there
     * is no more letters or
     *  digits in a row.
     * Returns the letters and/or digits
     * as the lexeme (identifier) found.
     * 
     * @precondition currentChar is a letter
     * @postcondition scanner has passed over
     *                all of the identifier, currentChar is
     *                the char after the identifier
     * 
     * @throws ScanErrorException if no lexeme is recognized
     */
    private String scanIdentifier() throws ScanErrorException
    {
        String token = "";
        while (isLetter(currentChar) || isDigit(currentChar))
        {
            token += String.valueOf(currentChar);
            eat(currentChar);
        }
        return token;
    }

    /**
     * Checks if it
     * belongs to a two character
     * operand, and returns
     * the one or two character
     * symbols as the lexeme (operand) found.
     * 
     * @precondition currentChar is an operand,
     *               or the beginning of a double operand
     * @postcondition scanner has passed over
     *                all of the operand, currentChar is
     *                the char after the operand
     * 
     * @throws ScanErrorException if current lexeme is unexpected
     *                            or unrecognized
     */
    private String scanOperand() throws ScanErrorException
    {
        String singleOperands = "= + - * / % ( ) ; ,";
        String doubleOperands = ": < >";
        if (singleOperands.indexOf(currentChar) != -1)
        {
            char ret = currentChar;
            eat(currentChar);
            return String.valueOf(ret);
        }
        String token = String.valueOf(currentChar);
        eat(currentChar);
        if ((doubleOperands.indexOf(token) != -1) && currentChar == '=')
        {
            token += String.valueOf(currentChar); 
            eat(currentChar);
        }
        else if (token.equals("<") && currentChar == '>')
        {
            token += String.valueOf(currentChar);
            eat(currentChar);
        }
        else if (token.equals("<") || token.equals(">"))
        {
            return token;
        }
        else
        {
            throw new ScanErrorException("Unrecognized character: " + token);
        }
        return token;
    }

    /**
     * Checks if the scanner
     * is at the end of the file.
     * 
     * @return true if at end of file;
     *         false otherwise
     */
    public boolean hasNext()
    {
        return (!eof);
    }

    /**
     * Skips any leading white space
     * and single-line, multi-line
     * comments, then examines
     * the value of currentChar, calling
     * appropiate methods to scan the next lexeme
     * in the input stream.
     * Note: requires no consecutive
     * lines of single-line comments,
     * instead combine them into multi
     * line comments.
     * 
     * @return String representing the lexeme found;
     *         "EOF" if end of file.
     * 
     * @throws ScanErrorException
     */
    public String nextToken() throws ScanErrorException
    {
        try
        {
            String operands = "= + - * / % ( ) : < > ; ,";
            while (hasNext() && currentChar != '.' && isWhiteSpace(currentChar))
            {
                eat(currentChar);
            }
            if (!hasNext() || currentChar == '.')
            {
                eof = true;
                return "EOF";
            }
            else
            {
                if (currentChar == '/')
                {
                    eat(currentChar);
                    if (currentChar == '/') //single line comments
                    {
                        while (hasNext())
                        {
                            eat(currentChar);
                        }
                        eat(currentChar);
                        //return "";
                    }
                    else if (currentChar == '*') //block comments
                    {
                        eat(currentChar);
                        //System.out.println("eaten, current char: " + currentChar);
                        char temp = currentChar;
                        eat(currentChar);
                        //System.out.println("temp: " + temp);
                        while (hasNext() && !(temp == '*' && currentChar == '/'))
                        {
                            temp = currentChar;
                            //System.out.println("temp: " + currentChar);
                            eat(currentChar);
                            //System.out.println("eaten, next char: " + currentChar);
                        }
                        //System.out.println("current char: " + currentChar);
                        eat(currentChar);
                        //return "";
                        //System.out.println("eaten, next char: " + currentChar);
                    }
                    else
                    {
                        return "/"; //division
                    }
                }
                while (hasNext() && currentChar != '.' && isWhiteSpace(currentChar))
                {
                    eat(currentChar);
                }
                if (!hasNext() || currentChar == '.')
                {
                    return "EOF";
                }
                else if (isDigit(currentChar))
                {
                    return scanNumber();
                }
                else if (isLetter(currentChar))
                {
                    return scanIdentifier();
                }
                else if (operands.indexOf(currentChar) != -1)
                {
                    return scanOperand();
                }
                else
                {
                    throw new ScanErrorException("Unrecognized character " + currentChar);
                }
            }
        }
        catch(ScanErrorException e)
        {
            eat(currentChar);
            return e.getMessage();
        }
    } 
    
    /**
     * Locates the bundled default test program, so the scanner
     * can be run with no arguments from either the repository
     * root or from inside the compiler directory.
     *
     * @return the default input file, or null if none was found
     */
    private static File defaultInput()
    {
        String[] candidates = {"compiler/scanner/scannerTestAdvanced.txt",
                               "scanner/scannerTestAdvanced.txt",
                               "scannerTestAdvanced.txt"};
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
     * Main method used to run the scanner test docs.
     * Reads in the file, creates a scanner, and
     * prints the stream of tokens generated.
     *
     * @param args optional path to the file to tokenize
     */
    public static void main(String[] args)
    {
        try
        {
            File inputFile = (args.length > 0) ? new File(args[0]) : defaultInput();
            if (inputFile == null || !inputFile.exists())
            {
                System.out.println("Usage: java scanner.Scanner <input.txt>");
                return;
            }
            InputStream in = new FileInputStream(inputFile);
            Scanner myScanner = new Scanner(in);

            while(myScanner.hasNext())
            {
                System.out.println(myScanner.nextToken());
            }
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
