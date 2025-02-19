package fr.usmb.m1isc.compilation.tp;

import java.util.HashSet;
import java.util.Set;

public class Noeud {
    private String valeur;
    private Noeud gauche;
    public Noeud droit;
    private static int labelCounter = 0;
    public static Set<String> variables = new HashSet<>();

    private static final String MOVEEAX = "    mov eax, ";

    public Noeud(Noeud gauche, Noeud droit, String valeur) {
        this.gauche = gauche;
        this.droit = droit;
        this.valeur = valeur;
    }

   public String toAsm() {
        StringBuilder asm = new StringBuilder();
        StringBuilder data = new StringBuilder();

        if (";".equals(valeur)) {
            if (gauche != null) {
                asm.append(gauche.toAsm());
            }
            if (droit != null) {
                asm.append(droit.toAsm());
            }
            return asm.toString();
        }



        switch (valeur) {
            case "LET":

                if (gauche != null && gauche.valeur != null) {
                    variables.add(gauche.valeur);
                    data.append("    ").append(gauche.valeur).append(" DD\n");
                }
                if (isNumeric(droit.valeur)) {
                    asm.append(this.MOVEEAX + droit.valeur).append("\n");
                    asm.append("    mov ").append(gauche.valeur).append(", eax\n");
                } else {
                    asm.append(droit.toAsm());
                    asm.append("    mov ").append(gauche.valeur).append(", eax\n");
                }
                break;

            case "+":
                asm.append(gauche.toAsm());
                asm.append("    push eax\n");
                asm.append(droit.toAsm());
                asm.append("    pop ebx\n");
                asm.append("    add eax, ebx\n");
                break;

            case "-":
                asm.append(gauche.toAsm());
                asm.append("    push eax\n");
                asm.append(droit.toAsm());
                asm.append("    pop ebx\n");
                asm.append("    sub eax, ebx\n");
                break;

            case "*":
                asm.append(gauche.toAsm());
                asm.append("    push eax\n");
                asm.append(droit.toAsm());
                asm.append("    pop ebx\n");
                asm.append("    mul eax, ebx\n");
                break;

            case "/":
                asm.append(gauche.toAsm());
                asm.append("    push eax\n");
                asm.append(droit.toAsm());
                asm.append("    pop ebx\n");
                asm.append("    div ebx, eax\n");
                asm.append("    mov eax, ebx\n");
                break;

            case "%":
                asm.append(droit.toAsm());
                asm.append("    push eax\n");
                asm.append(gauche.toAsm());
                asm.append("    pop ebx\n");
                asm.append("    mov ecx, eax\n");
                asm.append("    div ecx, ebx\n");
                asm.append("    mul ecx, ebx\n");
                asm.append("    sub eax, ecx\n");
                break;

            case "ENTIER":
                asm.append("    mov eax, ").append(valeur).append("\n");
                break;

            case "IDENT":
                asm.append("    mov eax, ").append(valeur).append("\n");
                break;

            case "WHILE":
                int whileLabel = labelCounter++;
                asm.append("debut_while_").append(whileLabel).append(":\n");
                asm.append(gauche.toAsm());
                asm.append("    jz sortie_while_").append(whileLabel).append("\n");
                asm.append(droit.toAsm());
                asm.append("    jmp debut_while_").append(whileLabel).append("\n");
                asm.append("sortie_while_").append(whileLabel).append(":\n");
                break;

            case "IF":
                int ifLabel = labelCounter++;
                asm.append(gauche.toAsm());
                asm.append("    jz else_").append(ifLabel).append("\n");
                asm.append(droit.gauche.toAsm());
                if (droit.droit != null) {
                    asm.append("    jmp fin_if_").append(ifLabel).append("\n");
                    asm.append("    else_").append(ifLabel).append(":\n");
                    asm.append(droit.droit.toAsm());
                    asm.append("    fin_if_").append(ifLabel).append(":\n");
                } else {
                    asm.append("    else_").append(ifLabel).append(":\n");
                }
                break;

            case "GT": case "GTE": case "EGAL": case "LT": case "LTE": case "NEQ":
                asm.append(gauche.toAsm());
                asm.append("    push eax\n");
                asm.append(droit.toAsm());
                asm.append("    pop ebx\n");
                asm.append("    sub ebx, eax\n");
                asm.append("    ").append(getJumpInstruction(valeur)).append(" vrai_").append(labelCounter).append("\n");
                asm.append("    mov eax, 0\n");
                asm.append("    jmp ").append("sortie_").append(valeur).append(labelCounter).append("\n");
                asm.append("vrai_").append(labelCounter).append(":\n");
                asm.append("    mov eax, 1\n");
                asm.append("sortie_").append(valeur).append(labelCounter).append(":\n");
                break;
            
            case "OUTPUT":
                asm.append("    mov eax, ").append(gauche.valeur).append("\n");
                asm.append("    out eax\n");
                break;

            case "INPUT":
                asm.append("    in eax\n");
                break;

            default:
                if(isNumeric(valeur)){
                    asm.append("    mov eax, ").append(valeur).append("\n");
                }
                else {
                    asm.append("    mov eax, ").append(valeur).append("\n");
                }
                break;
        }

        return asm.toString();
    }

    private String getJumpInstruction(String op) {
        switch (op) {
            case "GT": return "jl";
            case "LT": return "jg";
            case "GTE": return "jle";
            case "LTE": return "jge";
            case "EGAL": return "je";
            case "NEQ": return "jne";
            default: return "";
        }
    }

    public static boolean isNumeric(String str) {
        try { Double.parseDouble(str); return true; }
        catch (NumberFormatException e) { return false; }
    }


    public static String generateDataSegment() {
        StringBuilder data = new StringBuilder();
        data.append("DATA SEGMENT\n");
        for (String variable : variables) {
            data.append("    ").append(variable).append(" DD\n");
        }
        data.append("DATA ENDS\n");
        return data.toString();
    }


    @Override
    public String toString() {
        return toPrefix();
    }

    public String toPrefix() {
        if (gauche == null && droit == null) {
            return valeur;
        }
        return "(" + valeur + " " + (gauche != null ? gauche.toPrefix() : "") + " " + (droit != null ? droit.toPrefix() : "") + ")";
    }
}