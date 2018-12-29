/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

/**
 *
 * @author Lassad
 */
public class Produit extends FactureProduit{
    
    //private  String id;
    //private  String libelle;
    private  String descrip;
    //private  String qte;
    private  String prix;
    //private String idFr;
    
    
    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public Produit(String id, String libelle, String qte,String descrip, String prix/*, String idFr*/) {
        super(id, libelle, qte);
        this.descrip = descrip;
        this.prix = prix;
        //this.idFr = idFr;
    }

    /*public String getIdFr() {
    return idFr;
    }
    
    public void setIdFr(String idFr) {
    this.idFr = idFr;
    }*/

    
    
    /*public Produit(String id, String libelle, String descrip, String qte,String prix) {
        this.id = id;
        this.libelle = libelle;
        this.descrip = descrip;
        this.qte = qte;
        this.prix = prix;
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
    }*/

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    /*public String getQte() {
        return qte;
    }

    public void setQte(String qte) {
        this.qte = qte;
    }*/
       
}
