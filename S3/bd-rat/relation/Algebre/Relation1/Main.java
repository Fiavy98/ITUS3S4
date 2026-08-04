package main;

import relation.Relation;

public class Main {
    public static void main(String[] args) {
        Relation relationA = new Relation();
        relationA.addAttribute(Relation.createAttribute("Id", Integer.class));
        relationA.addAttribute(Relation.createAttribute("Name", String.class));
        relationA.addRow(1, "Essai numéro 1");
        relationA.addRow(2, "Essai numéro 2");
        relationA.addRow(3, "Essai numéro 3");

        Relation relationB = new Relation();
        relationB.addAttribute(Relation.createAttribute("Id", Integer.class));
        relationB.addAttribute(Relation.createAttribute("Name", String.class));
        relationB.addRow(2, "Essai numéro 2");
        relationB.addRow(4, "Essai numéro 4");

        // Exemple de sélection
        Relation selected = relationA.selection("Id", 2);
        System.out.println("Sélection:");
        selected.afficheRow();

        // Exemple de projection
        Relation projected = relationA.projection("Name");
        System.out.println("Projection:");
        projected.afficheRow();

        // Exemple d'union
        Relation unioned = relationA.union(relationB);
        System.out.println("Union:");
        unioned.afficheRow();

        // Exemple de différence
        Relation differed = relationA.difference(relationB);
        System.out.println("Différence:");
        differed.afficheRow();

        // Exemple d'intersection
        Relation intersected = relationA.intersection(relationB);
        System.out.println("Intersection:");
        intersected.afficheRow();

        // Exemple de produit cartésien
        Relation cartesian = relationA.cartesianProduct(relationB);
        System.out.println("Produit cartésien:");
        cartesian.afficheRow();
    }
}
