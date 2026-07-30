	#This MIPS code is auto-generated from the Java compiler.
	#@author Linda Zeng
	#@version 5.14.24
	.data
	varx:    .word 0
	vary:    .word 0
	varcount:    .word 0
	.text
	.globl main
main:
	li $v0 2
	sw $v0 varx
	la $t0 varx
	lw $v0 ($t0)
	subu $sp $sp 4		#push $v0
	sw $v0 ($sp)
	li $v0 1
	lw $t0 ($sp)		#pop $v0
	addu $sp $sp 4
	addu $v0 $t0 $v0
	sw $v0 vary
	la $t0 varx
	lw $v0 ($t0)
	subu $sp $sp 4		#push $v0
	sw $v0 ($sp)
	la $t0 vary
	lw $v0 ($t0)
	lw $t0 ($sp)		#pop $v0
	addu $sp $sp 4
	addu $v0 $t0 $v0
	sw $v0 varx
	la $t0 varx
	lw $v0 ($t0)
	subu $sp $sp 4		#push $v0
	sw $v0 ($sp)
	la $t0 vary
	lw $v0 ($t0)
	lw $t0 ($sp)		#pop $v0
	addu $sp $sp 4
	mul $v0 $t0 $v0
	#prints value with a new line after
	move $a0 $v0
	li $v0 1
	syscall
	li $v0 11
	li $a0 10
	syscall
	la $t0 varx
	lw $v0 ($t0)
	subu $sp $sp 4		#push $v0
	sw $v0 ($sp)
	la $t0 vary
	lw $v0 ($t0)
	lw $t0 ($sp)		#pop $v0
	addu $sp $sp 4
	ble $t0 $v0 endif1
	la $t0 varx
	lw $v0 ($t0)
	#prints value with a new line after
	move $a0 $v0
	li $v0 1
	syscall
	li $v0 11
	li $a0 10
	syscall
	la $t0 vary
	lw $v0 ($t0)
	#prints value with a new line after
	move $a0 $v0
	li $v0 1
	syscall
	li $v0 11
	li $a0 10
	syscall
endif1:
	li $v0 14
	subu $sp $sp 4		#push $v0
	sw $v0 ($sp)
	li $v0 14
	lw $t0 ($sp)		#pop $v0
	addu $sp $sp 4
	bne $t0 $v0 endif2
	li $v0 14
	subu $sp $sp 4		#push $v0
	sw $v0 ($sp)
	li $v0 14
	lw $t0 ($sp)		#pop $v0
	addu $sp $sp 4
	beq $t0 $v0 endif3
	li $v0 3
	#prints value with a new line after
	move $a0 $v0
	li $v0 1
	syscall
	li $v0 11
	li $a0 10
	syscall
endif3:
	li $v0 14
	subu $sp $sp 4		#push $v0
	sw $v0 ($sp)
	li $v0 14
	lw $t0 ($sp)		#pop $v0
	addu $sp $sp 4
	bgt $t0 $v0 endif4
	li $v0 4
	#prints value with a new line after
	move $a0 $v0
	li $v0 1
	syscall
	li $v0 11
	li $a0 10
	syscall
endif4:
endif2:
	li $v0 15
	subu $sp $sp 4		#push $v0
	sw $v0 ($sp)
	li $v0 14
	lw $t0 ($sp)		#pop $v0
	addu $sp $sp 4
	ble $t0 $v0 endif5
	li $v0 5
	#prints value with a new line after
	move $a0 $v0
	li $v0 1
	syscall
	li $v0 11
	li $a0 10
	syscall
endif5:
	li $v0 1
	sw $v0 varcount
loop6:
	la $t0 varcount
	lw $v0 ($t0)
	subu $sp $sp 4		#push $v0
	sw $v0 ($sp)
	li $v0 15
	lw $t0 ($sp)		#pop $v0
	addu $sp $sp 4
	bgt $t0 $v0 endLoop6
	la $t0 varcount
	lw $v0 ($t0)
	#prints value with a new line after
	move $a0 $v0
	li $v0 1
	syscall
	li $v0 11
	li $a0 10
	syscall
	la $t0 varcount
	lw $v0 ($t0)
	subu $sp $sp 4		#push $v0
	sw $v0 ($sp)
	li $v0 1
	lw $t0 ($sp)		#pop $v0
	addu $sp $sp 4
	addu $v0 $t0 $v0
	sw $v0 varcount
	j loop6
endLoop6:
	li $v0 10
	syscall
