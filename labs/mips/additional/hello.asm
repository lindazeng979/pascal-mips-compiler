#MIPS program to ask the user for their name
#(of max length 32)
#and print the greeting "Hello username"
#where username is replaced with the user's name
#@author Audrey Cheng, Linda Zeng
#@ersion 05.02.2024
.data
	ask:	.asciiz "What is your name? "
	hello:	.asciiz "Hello "
.text
	#print prompt
	li $v0 4
	la $a0 ask
	syscall
	
	#get input
	li $a1 32
	li $v0 8
	syscall
	move $t0, $a0
	
	#print hello
	li $v0 4
	la $a0 hello
	syscall
	
	#print name
	move $a0, $t0
	syscall
	
	#terminate
	li $v0 10
	syscall
	
	
		