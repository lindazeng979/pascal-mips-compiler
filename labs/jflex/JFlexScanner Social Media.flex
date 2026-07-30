package scanner;
/**
* This file defines a simple lexer for the compilers course 2023-2024
* This parses a Twitter corpus and identifies tweet ids, weekday tweet
* was published, hashtags, links, retweets, and mentions in the tweet.
*/
import java.io.*;


%%
/* lexical functions */
/* specify that the class will be called Scanner and the function to get the next
 * token is called nextToken.  
 */
%class Scanner
%unicode
%line
%public
%function nextToken
/*  return String objects - the actual lexemes */
/*  returns the String "EOF: at end of file */
%type String
%eofval{
return "EOF";
%eofval}

/**
 * Pattern definitions
 */
 
Up_Letter = [A-Z]
Hashtag = "#"
Symbol = [-/%=?_()&.] 
Low_Letter = [a-z]
Letter = [a-zA-Z_]
Digit = \d
WhiteSpace = [" "|"\n"|"\t"|"\r"]


%%
/**
 * lexical rules
 */
"tw-"{Digit}{18} {return "<tweet identifier:" + yytext()+">";}

{Up_Letter}{Low_Letter}+"day" {return "<weekday:" + yytext()+">";}

{Hashtag}{Letter}+ {return "<hashtag:" + yytext()+">";}

"RT "?"@"{Letter}*":" {return "<retweet or mention:" + yytext()+">";}

("https"|"http")"://"({Letter}|{Symbol}|{Digit})* {return "<link:" + yytext()+">";}

{WhiteSpace}* { /* do nothing */ }

. { /* do nothing */ }

//need file reader and has next: yyatEOF()
