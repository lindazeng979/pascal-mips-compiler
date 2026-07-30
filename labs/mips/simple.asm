#Takes in an integer
#from the user, and
#prints the int 
#multiplied by 10
#@author Linda Zeng
#@version 4.20.24

	.text 0x00400000
	.globl main

main:
	#accepts user input
	li $v0, 5 
	syscall
	
	#multiplies by 10
	li $t0, 10 
	multu $v0, $t0 
	mflo $a0 
	
	#prints the product
	li $v0, 1
	syscall
	
	#terminate
	li $v0, 10 
	syscall
