package fr.usmb.m1isc.compilation.tp;

import java.io.FileReader;
import java.io.InputStreamReader;
import java_cup.runtime.Symbol;


public class Main {

    public static void main(String[] args) throws Exception  {
        LexicalAnalyzer yy;
        if (args.length > 0)
            yy = new LexicalAnalyzer(new FileReader(args[0]));
        else
            yy = new LexicalAnalyzer(new InputStreamReader(System.in));

        @SuppressWarnings("deprecation")
        parser p = new parser(yy);
        Noeud racine = (Noeud) ((Symbol) p.parse()).value;  // Extracting the Noeud from the Symbol
        System.out.println(racine.toString());
    }
}
