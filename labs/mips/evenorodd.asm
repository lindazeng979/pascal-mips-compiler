#Accepts integer from user, and
#prints "Even" or "Odd" depending
#on if the integer is
#even or odd
#@author Linda Zeng
#@version 4.20.24

	.data
		even: .asciiz "Even"
		odd: .asciiz "Odd"
	.text 0x00400000
	.globl main

main:
	#accepts user input
	li $v0, 5 
	syscall
	
	#divides input to find remainder
	move $t0, $v0 #move input to register
	li $t1, 2
	div $t0, $t1
	mfhi $t2  
	
	#checks if the remainder is 0 or 1
	li $t3, 1 
	beq $t2, $t3, else 
	la $a0, even 
	j after
	else:
		la $a0, odd #odd
	
	#prints even or odd
	after:
		li $v0, 4
		syscall 
	
	#terminate
	li $v0, 10 
	syscall
