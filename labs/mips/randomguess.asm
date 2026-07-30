#Chooses a random number between 0 and 100
#and repeatedly asks the user to guess
#the number by telling the user if their
#guess is too high or low
#@author Linda Zeng
#@version 4.20.24
	.data
		low: .asciiz "Too low \nEnter new number: "
		high: .asciiz "Too high \nEnter new number: "
		correct: .asciiz "Correct Value!"
		prompt: .asciiz "Enter a guess between 0 and 100: "
	.text 0x00400000
	.globl main

main:
	
	#Get random number with upper bound 100
	li $a1, 100 
	li $v0, 42
	syscall
	move $t7, $a0 
	
	#Prompts the user for input 
	li $v0, 4 
	la $a0, prompt
	syscall
	
	#Accepts user input
	li $v0, 5 
	syscall	
	move $t5, $v0 
	
	#Repeatedly accepts user input and checks it against correct number
	looptest:
		beq $t5, $t7, endloop 
		li $v0, 4 
		bgt $t5, $t7, else
		#if input is less than correct number
			la $a0, low 
			syscall
			j after
		#if input is greater than correct number
		else:
			la $a0, high 
			syscall
			j after
		#Reads new int
		after:
			li $v0, 5 
			syscall	
			move $t5, $v0 
			j looptest
	#Exits loop and prints if input is equal to correct number
	endloop: 
		li $v0, 4 
		la $a0, correct
		syscall

	#terminate
	li $v0, 10 
	syscall
