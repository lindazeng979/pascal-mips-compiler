# This file represents the subroutines
# implemented in the subroutine lab,
# including max2, max3, fact, fib, 
# @author Linda Zeng
# @version 5.16.24
	.data
	input:	.asciiz "\nEnter an integer: "
	neg:	.asciiz "Your number is negative! Try again! "
	max:	.asciiz "\n Max value: "
	factorial:	.asciiz "\n Factorial is: "
	fibbonaci:	.asciiz "\n Fibonacci is: "
	list:	.space 12
	sum:	.asciiz "\n Sum of list values: "
	.text
#main method to call Fibbonaci method
#switch "fibb" and "fibbonaci" to "fact" and "factorial" if we want to call factorial
mainFibb:
	li $v0 4
	la $a0 input
	syscall
	
	li $v0 5
	syscall
	move $a0, $v0	#take in n value
	
	jal fibb
	
	move $t0, $v0
	
	li $v0 4
	la $a0 fibbonaci
	syscall
	
	move $a0, $t0
	li $v0, 1
	syscall

	li $v0 10
	syscall
#main method to call max3
maxMain:
	li $v0 4
	la $a0 input
	syscall
	
	li $v0 5
	syscall
	move $t0, $v0
	
	li $v0 4
	la $a0 input
	syscall
	
	li $v0 5
	syscall
	move $t1, $v0
	
	li $v0 4
	la $a0 input
	syscall
	
	li $v0 5
	syscall
	move $t2, $v0
	
	move $a0, $t0
	move $a1, $t1
	move $a2, $t2
	
	jal max3
	
	move $t0, $v0
	
	li $v0 4
	la $a0 max
	syscall
	
	move $a0, $t0
	li $v0, 1
	syscall
	
	li $v0 10
	syscall

# This method takes in two integers and
# returns the greater value.
#
# @precondition two numbers are in $a0, $a1
# @postcondition max in stored in $v0
# @return the max
max2:
	subu $sp, $sp, 4
	sw $ra, ($sp)
	
	bgt $a0, $a1, after
	move $v0, $a1
	j return
	
	after:
	move $v0, $a0
	
	return: 
	lw $ra, ($sp)
	addu $sp, $sp, 4
	jr $ra
	
# This method takes in three integers and
# returns the greater value, using max2.
#
# @precondition 3 numbers are in $a0, $a1, $a2
# @postcondition max in stored in $v0
# @return the max
max3:
	subu $sp, $sp, 4
	sw $ra, ($sp)
	
	jal max2
	move $a0, $v0	
	move $a1, $a2
	jal max2
	
	end:
	lw $ra, ($sp)
	addu $sp, $sp, 4
	jr $ra
	
# This method takes in a
# non negative integer n
# and returns n factorial.
# 
# @precondition n is in $a0
# @postcondition n factorial is in $v0
# @return n!
fact:
	#push to stack
	subu $sp, $sp, 8
	sw $ra, ($sp)	#return address
	sw $s0, 4($sp)	#n value
	
	#basecase
	li $v0 1	
	beq $a0, 0, done

	#recursion
	move $s0, $a0
	subu $a0, $a0, 1
	jal fact
	
	#multiply
	mult $s0, $v0
	mflo $v0
	
	done:
		#pop from stack
		lw $ra, ($sp)
		addu $sp, $sp, 4
		lw $s0, ($sp)
		addu $sp, $sp, 4
		jr $ra	
		
		
# This method takes in a
# non negative integer n
# and returns fibonacci
# sequence's nth term.
# 
# @precondition n is in $a0
# @postcondition fib(n) is in $v0
# @return fib(n)
fibb:
	#push to stack
	subu $sp, $sp, 4
	sw $ra, ($sp)	#return address
	
	#basecase
	li $v0 1	
	ble $a0, 1, doneFib
	
	subu $sp, $sp, 4
	sw $a0, ($sp)	#n value to stack

	#recursion
	subu $a0, $a0, 1
	jal fibb
	
	lw $a0, ($sp)	#pop n from stack
	sw $v0, ($sp)	#push fib(n-1) to stack
	
	#recursion
	subu $a0, $a0, 2
	jal fibb
	
	lw $t0, ($sp)	#pop fib(n-1) from stack
	addu $sp, $sp, 4
	
	#add
	addu $v0, $v0, $t0
	
	doneFib:
		#pop from stack
		lw $ra, ($sp)
		addu $sp, $sp, 4
		jr $ra	

# CODE BELOW IS IRRELEVANT	
#main method to call listSum
mainSum:
	la $a0 list
	li $t0 3
	
	takeInNum:
		li $v0 4
		la $a0 input
		syscall
	
		li $v0 5
		syscall	#take in n value
		sw $v0, ($a0)	
		
	subu $t0, $t0,1
	addu $a0, $a0, 4
	bgt $t0, 0, takeInNum
	
	jal sumList
	
	move $t0, $v0
	
	li $v0 4
	la $a0 sum
	syscall
	
	move $a0, $t0
	li $v0, 1
	syscall

	li $v0 10
	syscall
		
# This method takes in a
# list and returns the 
# sum of its values
# 
# @precondition list pointer is in $a0
# @postcondition sum is in $v0
# @return sum of list values
sumList:
	#push to stack
	subu $sp, $sp, 4
	sw $ra, ($sp)	#return address
	
	#basecase
	li $v0 0
	lbu $t1,($a0)	
	beq $t1, $zero, doneSum
	
	subu $sp, $sp, 4
	sw $t1, ($sp)	#first value to stack

	#recursion
	addu $a0, $a0, 4
	jal fibb	#second value in $v0
	
	lw $t0, ($sp)	#pop first value from stack
	addu $sp, $sp, 4
	
	#add
	addu $v0, $v0, $t0	#add values
	
	doneSum:
		#pop from stack
		lw $ra, ($sp)
		addu $sp, $sp, 4
		jr $ra	