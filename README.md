# Pascal-to-MIPS Compiler

A compiler for a Pascal-like language, built from scratch in Java. It parses source code and generates MIPS assembly compatible with MARS and SPIM (without relying on parser generators, compiler frameworks, or external dependencies).

The same abstract syntax tree drives two backends: a tree-walking interpreter that
executes the program directly, and a code generator that emits MIPS (compiler).

Built for *Honors Advanced Topics in Computer Science: Compilers & Interpreters* (The Harker
School, 2023–24), across a sequence of labs each adding a stage to the pipeline.

**Author:** Linda Zeng

---

## Quick start

Requires a JDK (developed and tested on Java 18). No build tool needed.

```bash
# from the repository root
javac -d out $(find compiler -name "*.java")
```

**Compile a program to MIPS:**

```bash
java -cp out parser.Parser <input.txt> [output.asm]
```

```bash
# explicit input and output
java -cp out parser.Parser compiler/parser/parserTest9.txt out/test9.asm

# output path defaults to the input path with a .asm extension
java -cp out parser.Parser compiler/parser/parserTest12.txt

# no arguments compiles the bundled sample (parserTest10.txt)
java -cp out parser.Parser
```

**Dump the token stream** (scanner only, no parsing):

```bash
java -cp out scanner.Scanner compiler/scanner/scannerTestAdvanced.txt
```

**Run the generated assembly:** load the `.asm` file into
[MARS](http://courses.missouristate.edu/kenvollmar/mars/) or SPIM and assemble/run it.
`WRITELN` compiles to syscall 1 (print int) followed by syscall 11 (print newline).

---

## Entry points

| Entry point | Class | What it does |
|---|---|---|
| **Compiler** (main) | `parser.Parser` | source `.txt` → MIPS `.asm` |
| Scanner only | `scanner.Scanner` | source `.txt` → token stream on stdout |
| Interpreter | `ast.Program.exec(Environment)` | executes the AST directly; no `main`, call from your own driver |

The interpreter has no command-line entry point of its own — it predates the code generator
in the lab sequence. To use it, parse a program and call `exec`:

```java
Parser p = new Parser(new Scanner(new FileInputStream("program.txt")));
p.parseProgram().exec(new Environment());
```

---

## How it works

```
source.txt
    │
    ▼
scanner/Scanner.java ──────── hand-written DFA lexer. Character-by-character,
    │                         no regex and no generator. Strips // line comments
    │  tokens                 and /* block */ comments.
    ▼
parser/Parser.java ────────── recursive descent. Operator precedence falls out of
    │                         the expr → term → factor call chain.
    │  AST
    ▼
ast/*.java ────────────────── 16 node types. Each implements exec (interpret)
    │                         and/or compile (emit MIPS).
    ├──────────────► exec(Environment)   tree-walking interpreter
    │                environment/Environment.java — scope chain with parent links
    │
    └──────────────► compile(Emitter)    MIPS code generator
                     emitter/Emitter.java — stack discipline, $sp offsets, labels
```

**`scanner/Scanner.java`** — a hand-rolled deterministic finite automaton. `nextToken()`
dispatches on the current character into `scanNumber`, `scanIdentifier`, or `scanOperand`,
handling multi-character operators (`<=`, `>=`, `<>`, `:=`) and both comment forms.

**`parser/Parser.java`** — recursive descent with a one-token lookahead and an `eat(String)`
that advances or throws. Arithmetic precedence is encoded structurally: `parseExpression`
handles `+ -`, calls `parseTerm` for `* /`, which calls `parseFactor` for literals,
parenthesized subexpressions, unary minus, variable references, and procedure calls.

**`ast/`** — the node hierarchy. `Program` is the root, holding global variables, procedure
declarations, and the main statement.

**`environment/Environment.java`** — the interpreter's symbol table. Each environment holds
variable and procedure maps plus a parent pointer, so a procedure body gets a child scope
that falls back to global on a miss. Procedures always register in the global scope.

**`emitter/Emitter.java`** — the code generator's bookkeeping. Tracks `excessStackHeight` so
local variables and parameters resolve to the correct `$sp` offset as the stack grows mid-
expression, generates unique labels for control flow, and knows which procedure is currently
being compiled so it can tell locals from globals.

---

## Language supported

```
program        → varDecl* procedureDecl* statement .
varDecl        → VAR id (, id)* ;
procedureDecl  → PROCEDURE id ( params? ) ; varDecl* statement
statement      → WRITELN ( expr ) ;
               | READLN ( id ) ;
               | BEGIN statement* END ;
               | IF expr relop expr THEN statement [ ELSE statement ]
               | WHILE expr relop expr DO statement
               | FOR id := num TO num DO statement
               | id := expr ;
expr           → expr (+ | -) term | term
term           → term (* | /) factor | factor
factor         → ( expr ) | - factor | id | id ( args? ) | num
relop          → = | <> | < | > | <= | >=
```

- **Types:** integers only.
- **Procedures:** parameters, local variables (`VAR` inside a declaration), and **recursion**.
  A procedure returns a value by assigning to its own name (`square := x * x`), the Pascal convention.
- **Comments:** `//` to end of line, and `/* ... */` blocks.
- **Termination:** a `.` ends the program; the scanner treats it as end-of-input.

### Calling convention (generated code)

Arguments are pushed onto the stack by the caller. The callee pushes a zero-initialized
return slot, saves `$ra`, pushes locals, then runs. On return it restores `$ra` and jumps
back; the caller pops the return value off the stack into `$v0`. This is what makes
recursion work.

---

## Example

Input:

```pascal
VAR n, ignore;

PROCEDURE square(x);
  square := x * x;

BEGIN
  n := 1;
  WHILE n <= 3 DO
  BEGIN
    WRITELN(square(n));
    n := n + 1;
  END;
END;
.
```

Generated MIPS (abridged — the `WHILE` and the procedure body):

```mips
	.data
	varn:    .word 0
	varignore:    .word 0
	.text
	.globl main
main:
	li $v0 1
	sw $v0 varn
loop1:
	la $t0 varn
	lw $v0 ($t0)
	subu $sp $sp 4		#push $v0
	sw $v0 ($sp)
	li $v0 3
	lw $t0 ($sp)		#pop $v0
	addu $sp $sp 4
	bgt $t0 $v0 endLoop1
	...
	jal procsquare
	...
	j loop1
endLoop1:
	li $v0 10
	syscall

procsquare:
	li $v0 0
	subu $sp $sp 4		#push $v0     <- return slot
	sw $v0 ($sp)
	subu $sp, $sp, 4    #push return address to stack
	sw $ra, ($sp)
	lw $v0 8($sp)                     <- parameter x
	...
	mul $v0 $t0 $v0
	sw $v0 4($sp)                     <- write return value
donesquare:
	lw $ra, ($sp)  #pop return address from stack
	addu $sp, $sp, 4
	jr $ra
```

Running this in MARS prints `1`, `4`, `9`.

---

## Repository layout

```
compiler/                Java source root; the packages below live inside it
  scanner/               lexer + scanner test inputs
  parser/                recursive-descent parser + parserTest*.txt sample programs
  ast/                   AST node types (exec + compile)
  environment/           interpreter symbol table / scope chain
  emitter/               MIPS output and stack bookkeeping
  *.asm                  generated assembly, checked in as samples

labs/                    supporting coursework, not part of the compiler build
  mips/                  hand-written MIPS programs (see below)
    additional/          two-person MIPS challenge
  subroutines/           hand-written MIPS subroutines (see below)
  jflex/                 Lab 2 generated-lexer rules (see below)
```

Sample programs live in `compiler/parser/` as `parserTest*.txt`. Nine of the twelve
compile; see limitations below.

Deliberately **not** tracked here (see `.gitignore`): the original lab handouts and lecture
slides; the JFlex distribution, available from [jflex.de](https://jflex.de); the ~123 MB Lab 2
Twitter corpus; and various `.zip` lab submissions.

---

## Known limitations

- **Incomplete code generation.** `FOR` and `READLN` are fully supported by the interpreter but do not implement code generation. Programs containing either construct will fail during compilation because `compile()` is unimplemented.
- **Undeclared global variables.** Only variables declared in a `VAR` block are emitted in the generated `.data` section. References to undeclared globals therefore produce undefined labels in the generated MIPS assembly. The interpreter masks this issue by implicitly initializing missing variables to `0`.  The affected files (`parserTest6`, `7`, `8`, `8_5`, `11`, `12`) predate the code-generation labs and ran mainly under the interpreter.
- **Invalid sample programs.** Two provided test files intentionally fall outside the supported grammar: `customParserTest.txt` omits `THEN` in an `IF` statement, and `parserTest13.txt` uses unsupported variable declaration syntax (`VAR x := f(...)`).
- **Limited semantic analysis.** The compiler performs no type checking or semantic analysis. It supports only integer values, and the interpreter implicitly initializes undeclared variables to `0`.

---

## The lab sequence

Each lab added one stage. The directory structure still reflects it:

| Lab | Topic | Where |
|---|---|---|
| 1 | Scanner | `compiler/scanner/Scanner.java` |
| 2 | JFlex | `labs/jflex/` |
| 3 | Pascal parser | `compiler/parser/Parser.java` |
| 4 | Abstract syntax trees | `compiler/ast/` |
| 4.5 | Procedures & environments | `ProcedureDeclaration`, `ProcedureCall`, `Environment` |
| 5 | MIPS | `labs/mips/` |
| 5.5 | Memory & subroutines | `labs/subroutines/` |
| 6 | Code generation | `emitter/Emitter.java`, `compile()` across `ast/` |
| 7 | Compiling procedures | `ProcedureDeclaration.compile()`, `ProcedureCall.compile()` |

### Lab 2 — JFlex

Lab 1's scanner was a hand-written DFA. This lab experiments with a generated one, where the
rules are regular expressions, and [JFlex](https://jflex.de) compiles them into the scanner.

The input is a Twitter corpus rather than Pascal source because tweets have 
awkward lexical structure useful in this exercise (e.g. URLs with `?&=%`-laden query strings, `#hashtags`, `@mentions`, etc.) `labs/jflex/JFlexScanner Social Media.flex` recognizes five token classes and discards everything else.

| Rule | Example match | Emits |
|---|---|---|
| `"tw-"{Digit}{18}` | `tw-682713219375476736` | `<tweet identifier:…>` |
| `{Up_Letter}{Low_Letter}+"day"` | `Thursday` | `<weekday:…>` |
| `{Hashtag}{Letter}+` | `#jobsearch` | `<hashtag:…>` |
| `"RT "?"@"{Letter}*":"` | `RT @CodeMineStatus:` | `<retweet or mention:…>` |
| `("https"\|"http")"://"…` | `https://www.jobfindly.com/…` | `<link:…>` |

The corpus was a public sample of ~99,000 AWS/cloud-related tweets, with only the
`text` field being used. **The data itself is not in this repository**. The
accompanying class presentation on the regexes is included as
`labs/jflex/Jflex Regex presentation.pdf`. Jflex is not used in the end-to-end compiler but as a side-exercise.

### Lab 5 — MIPS

Ten MIPS programs in `labs/mips/`, written **by hand** to learn the target language before
generating it: array sum/average/min/max, even-or-odd, loops, multiplication, a random-number
guessing game, and a music player using the MIDI syscall. `additional/` holds a two-person
challenge (`hello.asm`, `sum.asm`) written with Audrey Cheng. Run them in MARS or SPIM.

### Lab 5.5 — Memory & subroutines

`labs/subroutines/subroutines.asm` — `max2`, `max3`, factorial, and Fibonacci implemented as
proper MIPS subroutines with stack-based save/restore. This is where the calling convention
used by the code generator was worked out by hand first.
