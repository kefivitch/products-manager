/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.util.Objects;

/**
 *
 * @author Lassad
 */
public class FactureProduit {
    private String id;
    private String libelle;
    private String qte;

    public FactureProduit(String id, String libelle, String qte) {
        this.id = id;
        this.libelle = libelle;
        this.qte = qte;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getQte() {
        return qte;
    }

    public void setQte(String qte) {
        this.qte = qte;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.id == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FactureProduit other = (FactureProduit) obj;
        return Objects.equals(this.id, other.id);
    }
    
}
