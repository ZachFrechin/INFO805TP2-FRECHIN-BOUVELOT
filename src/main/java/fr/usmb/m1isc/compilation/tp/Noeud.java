package fr.usmb.m1isc.compilation.tp;

public class Noeud {
    private String valeur;
    private Noeud gauche;
    private Noeud droit;

    public Noeud(Noeud gauche, Noeud droit, String valeur) {
        this.gauche = gauche;
        this.droit = droit;
        this.valeur = valeur;
    }

    public String toPrefix() {
        if (gauche == null && droit == null) {
            return valeur;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(valeur); 
        if (gauche != null) {
            sb.append(" ");
            sb.append(gauche.toPrefix());
        }
        if (droit != null) {
            sb.append(" ");
            sb.append(droit.toPrefix());
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String toString() {
        return toPrefix();
    }
}
