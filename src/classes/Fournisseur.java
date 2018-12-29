/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;
import BDD.Parameter;
import BDD.db_connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.ObservableList;

/**
 *
 * @author Lassad
 */
public class Fournisseur {
    private String id;
    private String nom;
    private String adresse;
    private String tel;   

    public Fournisseur(String id, String nom, String adresse, String tel) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.tel = tel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
    public ObservableList<String> fourNames() throws SQLException {
        ObservableList<String> ret = null ;
        db_connection db = new db_connection(new Parameter().HOST_DB, new Parameter().USERNAME_DB, new Parameter().PASSWORD_DB, new Parameter().IPHOST, new Parameter().PORT);
        String cols[] = {"nomFr"};
        ResultSet rs = db.querySelect(cols, "fournisseur");
        while (rs.next()) {
            ret.add(rs.getObject(1).toString());
        }
        return ret;
    }

    public Fournisseur() {
    }
    
}
