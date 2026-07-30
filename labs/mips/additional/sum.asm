#MIPS program to add two numbers
#in the range [1000, 5000] from the user
#and print their sum
#@author Audrey Cheng, Linda Zeng
#@ersion 05.02.2024

.data
	message:	.asciiz "Enter first number in the range 1000 to 5000: "
	message2:	.asciiz "Enter second number in the range 1000 to 5000: "
	wrongmessage:	.asciiz "This number is not between 1000 and 5000. "
	summessage:	.asciiz "This is the sum: "
.text

first:
	#Print first num message
	la $a0 message
	li $v0 4
	syscall
	
	#take in first num
	li $v0 5
	syscall
	move $t0, $v0
	
	#check if within bounds
	blt $t0, 1000, wrong
	bgt $t0, 5000, wrong
	
	#if within bounds
	j second

wrong:
	#Print wrong message
	li $v0 4
	la $a0 wrongmessage
	syscall
	
	#Print first num message
	la $a0 message
	syscall
	
	#take in first num
	li $v0 5
	syscall
	move $t0, $v0
	
	blt $t0, 1000, wrong
	bgt $t0, 5000, wrong
	
	#if within bounds
	j second

second:
	#Print second num message
	la $a0 message2
	li $v0 4
	syscall
	
	
	#take in second num
	li $v0 5
	syscall
	move $t1, $v0
	
	#check if within bounds
	blt $t1, 1000, wrong2
	bgt $t1, 5000, wrong2
	
	#if within bounds
	j after
	
wrong2:
	#Print wrong message
	li $v0 4
	la $a0 wrongmessage
	syscall
	
	#Print second num message
	la $a0 message
	syscall
	
	#take in second num
	li $v0 5
	syscall
	move $t1, $v0
	
	#check if within bounds
	blt $t1, 1000, wrong2
	bgt $t1, 5000, wrong2
	
	#if within bounds
	j after
	
after:
	#print message
	la $a0 summessage
	li $v0 4
	syscall
	
	#adds nums and prints
	addu $a0, $t0, $t1
	li $v0 1
	syscall
	
	#terminate
	li $v0 10
	syscall
	
	
