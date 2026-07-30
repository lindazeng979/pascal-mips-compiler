	#This MIPS code is auto-generated from the Java compiler.
	#@author Linda Zeng
	#@version 5.14.24
	.data
	varf:    .word 0
	varignore:    .word 0
	.text
	.globl main
main:
	li $v0 2
	sw $v0 varf
	#pushing parameters to memory
	li $v0 3
	subu $sp $sp 4		#push $v0
	sw $v0 ($sp)
	jal procfoo
	#pop return value
	lw $v0 ($sp)		#pop $v0
	addu $sp $sp 4
	lw $t0 ($sp)		#pop $v0
	addu $sp $sp 4
	sw $v0 varignore
	la $t0 varf
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
procfoo:
	li $v0 0
	subu $sp $sp 4		#push $v0
	sw $v0 ($sp)
	subu $sp, $sp, 4    #push return address to stack
	sw $ra, ($sp)
	#pushing local variables to stack
	#pushing parameters to memory
	lw $v0 8($sp)
	subu $sp $sp 4		#push $v0
	sw $v0 ($sp)
	la $t0 varf
	lw $v0 ($t0)
	lw $t0 ($sp)		#pop $v0
	addu $sp $sp 4
	addu $v0 $t0 $v0
	subu $sp $sp 4		#push $v0
	sw $v0 ($sp)
	jal procbar
	#pop return value
	lw $v0 ($sp)		#pop $v0
	addu $sp $sp 4
	lw $t0 ($sp)		#pop $v0
	addu $sp $sp 4
	sw $v0 varignore
donefoo:
	#popping local variables from stack
	lw $ra, ($sp)  #pop return address from stack
	addu $sp, $sp, 4
	jr $ra
procbar:
	li $v0 0
	subu $sp $sp 4		#push $v0
	sw $v0 ($sp)
	subu $sp, $sp, 4    #push return address to stack
	sw $ra, ($sp)
	#pushing local variables to stack
	lw $v0 8($sp)
	#prints value with a new line after
	move $a0 $v0
	li $v0 1
	syscall
	li $v0 11
	li $a0 10
	syscall
donebar:
	#popping local variables from stack
	lw $ra, ($sp)  #pop return address from stack
	addu $sp, $sp, 4
	jr $ra
