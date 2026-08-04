package services;

import dao.MouvementDAO;
import models.*;

import java.util.*;

public class EtatStockService {

    private MouvementDAO dao = new MouvementDAO();

    // ================= CUMP =================
    public EtatStockDTO calculCUMP(Produit p) throws Exception {

        List<Mouvement> mvts = dao.getByProduit(p.getId());

        double stock = 0;
        double valeur = 0;

        for (Mouvement m : mvts) {

            if (m.getType() == TypeMouvement.ENTREE) {
                stock += m.getQuantite();
                valeur += m.getQuantite() * m.getPrixUnitaire();
            } else {
                stock -= m.getQuantite();
            }
        }

        EtatStockDTO dto = new EtatStockDTO();

        dto.setProduitId(p.getId());
        dto.setNomProduit(p.getNom());

        dto.setStockQuantite(stock);
        dto.setValeurStock(valeur);

        dto.setPrixMoyen(stock > 0 ? valeur / stock : 0);
        dto.setMethode("CUMP");

        return dto;
    }

    // ================= FIFO =================
    public EtatStockDTO calculFIFO(Produit p) throws Exception {

        List<Mouvement> mvts = dao.getByProduit(p.getId());

        Queue<LigneStock> stock = new LinkedList<>();

        for (Mouvement m : mvts) {

            if (m.getType() == TypeMouvement.ENTREE) {
                stock.add(new LigneStock(m.getQuantite(), m.getPrixUnitaire()));
            } else {
                int qte = m.getQuantite();

                while (qte > 0 && !stock.isEmpty()) {

                    LigneStock lot = stock.peek();

                    if (lot.qte <= qte) {
                        qte -= lot.qte;
                        stock.poll();
                    } else {
                        lot.qte -= qte;
                        qte = 0;
                    }
                }
            }
        }

        double stockFinal = 0;
        double valeurFinal = 0;

        for (LigneStock l : stock) {
            stockFinal += l.qte;
            valeurFinal += l.qte * l.prix;
        }

        EtatStockDTO dto = new EtatStockDTO();

        dto.setProduitId(p.getId());
        dto.setNomProduit(p.getNom());
        dto.setStockQuantite(stockFinal);
        dto.setValeurStock(valeurFinal);
        dto.setPrixMoyen(stockFinal > 0 ? valeurFinal / stockFinal : 0);
        dto.setMethode("FIFO");

        return dto;
    }

    // ================= LIFO =================
    public EtatStockDTO calculLIFO(Produit p) throws Exception {

        List<Mouvement> mvts = dao.getByProduit(p.getId());

        Stack<LigneStock> pile = new Stack<>();

        for (Mouvement m : mvts) {

            if (m.getType() == TypeMouvement.ENTREE) {
                pile.push(new LigneStock(m.getQuantite(), m.getPrixUnitaire()));
            } else {
                int qte = m.getQuantite();

                while (qte > 0 && !pile.isEmpty()) {

                    LigneStock lot = pile.peek();

                    if (lot.qte <= qte) {
                        qte -= lot.qte;
                        pile.pop();
                    } else {
                        lot.qte -= qte;
                        qte = 0;
                    }
                }
            }
        }

        double stockFinal = 0;
        double valeurFinal = 0;

        for (LigneStock l : pile) {
            stockFinal += l.qte;
            valeurFinal += l.qte * l.prix;
        }

        EtatStockDTO dto = new EtatStockDTO();

        dto.setProduitId(p.getId());
        dto.setNomProduit(p.getNom());
        dto.setStockQuantite(stockFinal);
        dto.setValeurStock(valeurFinal);
        dto.setPrixMoyen(stockFinal > 0 ? valeurFinal / stockFinal : 0);
        dto.setMethode("LIFO");

        return dto;
    }
}