#Takes 10 numbers as user input, and finds their
#sum, average, min, and max.
#@author Linda Zeng
#@version 4.22.24

.data
	myarray: .space 40
	enter: .asciiz "Enter a number: "
	avg: .asciiz "average: "
	sum: .asciiz "\nsum: "
	max: .asciiz "\nmax: "
	min: .asciiz "\nmin: "
.text

#initializes counter, sum, array, max, and min
li $t7 0 #counter
li $t8 0 #sum
la $t0 myarray
li $t9 -2147483648
li $t3 2147483647 

#iteratively accepts 10 user inputs into an array
#and checks them
input:
	#begins iterative loop
	beq $t7, 10, exit
	addi $t0, $t0, 4
	addi $t7, $t7, 1 
	
	#prompts the user for input
	li $v0, 4 
	la $a0, enter
	syscall
	
	#accepts input and adds it to the array
	li $v0, 5 
	syscall
	sw $a0, ($t0) 
	
	#updates the sum
	addu $t8, $t8, $v0
	
	#checks if it is min or max
	bgt $v0, $t9, maxfunc
	blt $v0, $t3, minfunc 
	j input
	maxfunc:
		move $t9, $v0
		blt $v0, $t3, minfunc
		j input
	minfunc:
		move $t3, $v0
		j input
#Prints the statistics of the array
exit: 
	#gets the average
	li $t5, 10 
	div $t8, $t5
	mflo $t5 
	
	#prints average
	li $v0, 4
	la $a0, avg
	syscall
	li $v0, 1
	move $a0, $t5
	syscall
	
	#prints sum
	li $v0, 4
	la $a0, sum
	syscall
	li $v0, 1
	move $a0, $t8
	syscall
	
	#prints max
	li $v0, 4
	la $a0, max
	syscall
	li $v0, 1
	move $a0, $t9
	syscall
	
	#prints min
	li $v0, 4
	la $a0, min
	syscall
	li $v0, 1
	move $a0, $t3
	syscall
	
	#terminate
	li $v0, 10
	syscall



