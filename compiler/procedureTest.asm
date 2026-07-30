	#This MIPS code is auto-generated from the Java compiler.
	#@author Linda Zeng
	#@version 5.14.24
	.data
	varcount:    .word 0
	varignore:    .word 0
	vartimes:    .word 0
	.text
	.globl main
main:
	li $v0 196
	sw $v0 varcount
	li $v0 0
	sw $v0 vartimes
	#pushing parameters to memory
	li $v0 10
	subu $sp $sp 4		#push $v0
	sw $v0 ($sp)
	li $v0 13
	subu $sp $sp 4		#push $v0
	sw $v0 ($sp)
	jal procprintSquares
	#pop return value
	lw $v0 ($sp)		#pop $v0
	addu $sp $sp 4
	lw $t0 ($sp)		#pop $v0
	addu $sp $sp 4
	lw $t0 ($sp)		#pop $v0
	addu $sp $sp 4
	sw $v0 varignore
	la $t0 varcount
	lw $v0 ($t0)
	#prints value with a new line after
	move $a0 $v0
	li $v0 1
	syscall
	li $v0 11
	li $a0 10
	syscall
	la $t0 vartimes
	lw $v0 ($t0)
	#prints value with a new line after
	move $a0 $v0
	li $v0 1
	syscall
	li $v0 11
	li $a0 10
	syscall
	li $v0 10
	syscall
procprintSquares:
	li $v0 0
	subu $sp $sp 4		#push $v0
	sw $v0 ($sp)
	subu $sp, $sp, 4    #push return address to stack
	sw $ra, ($sp)
	#pushing local variables to stack
	li $v0 0
	subu $sp $sp 4		#push $v0
	sw $v0 ($sp)
	li $v0 0
	subu $sp $sp 4		#push $v0
	sw $v0 ($sp)
	lw $v0 20($sp)
	sw $v0 4($sp)
loop1:
	lw $v0 4($sp)
	subu $sp $sp 4		#push $v0
	sw $v0 ($sp)
	lw $v0 20($sp)
	lw $t0 ($sp)		#pop $v0
	addu $sp $sp 4
	bgt $t0 $v0 endLoop1
	lw $v0 4($sp)
	subu $sp $sp 4		#push $v0
	sw $v0 ($sp)
	lw $v0 8($sp)
	lw $t0 ($sp)		#pop $v0
	addu $sp $sp 4
	mul $v0 $t0 $v0
	sw $v0 0($sp)
	lw $v0 0($sp)
	#prints value with a new line after
	move $a0 $v0
	li $v0 1
	syscall
	li $v0 11
	li $a0 10
	syscall
	lw $v0 4($sp)
	subu $sp $sp 4		#push $v0
	sw $v0 ($sp)
	li $v0 1
	lw $t0 ($sp)		#pop $v0
	addu $sp $sp 4
	addu $v0 $t0 $v0
	sw $v0 4($sp)
	la $t0 vartimes
	lw $v0 ($t0)
	subu $sp $sp 4		#push $v0
	sw $v0 ($sp)
	li $v0 1
	lw $t0 ($sp)		#pop $v0
	addu $sp $sp 4
	addu $v0 $t0 $v0
	sw $v0 vartimes
	j loop1
endLoop1:
doneprintSquares:
	#popping local variables from stack
	lw $t0 ($sp)		#pop $v0
	addu $sp $sp 4
	lw $t0 ($sp)		#pop $v0
	addu $sp $sp 4
	lw $ra, ($sp)  #pop return address from stack
	addu $sp, $sp, 4
	jr $ra
