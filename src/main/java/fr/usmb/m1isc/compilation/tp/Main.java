package fr.usmb.m1isc.compilation.tp;

import java.io.FileReader;
import java.io.InputStreamReader;
import java_cup.runtime.Symbol;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) throws Exception {
        String emoji = "\uD83E\uDD84";  // 🦄 emoji
        System.out.println("Emoji: " + emoji);
        LexicalAnalyzer yy;
        if (args.length > 0)
            yy = new LexicalAnalyzer(new FileReader(args[0]));
        else
            yy = new LexicalAnalyzer(new InputStreamReader(System.in));

        parser p = new parser(yy);
        Noeud racine = (Noeud) ((Symbol) p.parse()).value;

        // Génération dynamique du segment DATA
        String codeSegment = racine.toAsm();
        String dataSegment = racine.generateDataSegment();

        String finalSegment = dataSegment + "CODE SEGMENT\n" + codeSegment + "CODE ENDS";
        PrintWriter out = new PrintWriter("output.asm");
        out.println(finalSegment);
        out.close();
        
        // Génération du segment CODE


        // Affichage du code assembleur complet
        System.out.println(dataSegment);
        System.out.println("CODE SEGMENT");
        System.out.println(codeSegment);
        System.out.println("CODE ENDS");
    }
}