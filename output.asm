DATA SEGMENT
    a DD
    b DD
    aux DD
DATA ENDS
CODE SEGMENT
    in eax
    mov a, eax
    in eax
    mov b, eax
    mov eax, a
    push eax
    mov eax, b
    pop ebx
    sub ebx, eax
    jl vrai_1
    mov eax, 0
    jmp sortie_GT1
vrai_1:
    mov eax, 1
sortie_GT1:
    jz else_0
    mov eax, a
    push eax
    mov eax, 1
    pop ebx
    add eax, ebx
    mov aux, eax
    jmp fin_if_0
    else_0:
    mov eax, b
    push eax
    mov eax, 1
    pop ebx
    add eax, ebx
    mov aux, eax
    fin_if_0:
    mov eax, aux
    out eax
CODE ENDS
