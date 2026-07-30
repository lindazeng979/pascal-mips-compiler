	.data
		even: .asciiz "Even"
		odd: .asciiz "Odd"
	.text 0x00400000
	.globl main

main:
	li $v0, 5 #read int
	syscall
	
	move $t0, $v0 #move input to register
	li $t1, 2
	div $t0, $t1 #division
	mfhi $t2  #remainder
	
	li $t3, 1 #value to compare to
	
	li $v0, 4 #print condition
	
	beq $t2, $t3, else #check if remainder = 1
	la $a0, even #even
	j after #jump past "else"
	else:
		la $a0, odd #odd
	
	after:
		syscall #prints it
	
	
	li $v0, 10 #terminate
	syscall
