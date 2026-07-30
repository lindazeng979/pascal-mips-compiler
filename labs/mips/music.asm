#Plays music
#@author Linda Zeng
#@version 4.24.24

.data
	pitch: .byte 64, 62, 72, 62, 64, 64, 64, 61
	duration: .word 1000
	instrument: .byte 60
	volume: .byte 120
.text

#Defines pitch, duration, instrument, volume
li $v0, 33
la $t0, pitch
lb $a0, ($t0)
lw $a1, duration
lb $a2,  instrument
lb $a3, volume

#iteratively plays notes according to the ppitch array
play:
	beq $a0, 61, end
	addi $t0, $t0, 4
	lb $a0, ($t0)
	syscall
	j play
end:
	#terminate
	li $v0, 10
	syscall
	
	
	
	

