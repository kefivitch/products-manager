/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionprods;

import BDD.Parameter;
import BDD.ResultSetTableModel;
import BDD.db_connection;
import classes.Fournisseur;
import classes.Produit;
import com.sun.javafx.collections.ElementObservableListDecorator;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 *
 * @author Lassad
 */
public class magasinierInterface extends Application {

    ResultSet rsProd, rsFr;
    db_connection db = new db_connection(new Parameter().HOST_DB, new Parameter().USERNAME_DB, new Parameter().PASSWORD_DB, new Parameter().IPHOST, new Parameter().PORT);
    ObservableList<Produit> prodData = FXCollections.observableArrayList();
    ObservableList<Fournisseur> fourData = FXCollections.observableArrayList();

    @Override
    @SuppressWarnings("empty-statement")
    public void start(Stage primaryStage) throws SQLException {
        primaryStage.setTitle("Gestion de stocks | MAGASINIER");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setManaged(false);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        for (int i = 0; i < 9; i++) {
            ColumnConstraints column = new ColumnConstraints(100);
            grid.getColumnConstraints().add(column);
        }
        Text scenetitle = new Text("Magasinier");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));
        scenetitle.setTextAlignment(TextAlignment.CENTER);
        grid.add(scenetitle, 0, 0, 1, 1);
        /*Separator separator1 = new Separator();
        separator1.setOrientation(Orientation.HORIZONTAL);
        grid.add(separator1,1,1,2, 1);*/

        final Label label = new Label("Liste de Produits");
        label.setFont(new Font("Tahoma", 20));
        label.setTranslateX(50);
        TableView table = new TableView();
        table.setEditable(true);
        table.setMaxWidth(400);
        TableColumn idCol = new TableColumn("ID Produit");
        TableColumn LibCol = new TableColumn("Libelle Produit");
        TableColumn DescripCol = new TableColumn("Description");
        TableColumn prixCol = new TableColumn("Prix");
        TableColumn qtyCol = new TableColumn("Quantité");
        //TableColumn frCol = new TableColumn("Fournisseur");
        table.getColumns().addAll(idCol, LibCol, DescripCol, prixCol, qtyCol/*,frCol*/);
        final VBox vbox = new VBox();
        vbox.setSpacing(6);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);
        rsProd = db.querySelectAll("produit");
        while (rsProd.next()) {
            String str = new String();
            String cols[] = {"nomFr"};
            /*ResultSet rsFrProd = db.fcSelectCommand(cols, "fournisseur", "id = "+rsProd.getObject(6).toString());
            while (rsFrProd.next()) {
                str = new String(rsFrProd.getObject(1).toString());
                
            }*/
            prodData.add(
                    new Produit(
                            rsProd.getObject(1).toString(),//id
                            rsProd.getObject(2).toString(),//lib
                            rsProd.getObject(5).toString(),//descrip
                            rsProd.getObject(3).toString(),//prix
                            rsProd.getObject(4).toString()//qte
                            //rsProd.getObject(6).toString()//fr
                            //str
                    )
            );
        }
        
        idCol.setCellValueFactory(
                new PropertyValueFactory<Produit, String>("id")
        );
        LibCol.setCellValueFactory(
                new PropertyValueFactory<Produit, String>("Libelle")
        );
        LibCol.setCellFactory(TextFieldTableCell.forTableColumn());
        LibCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Produit, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Produit, String> t) {
                ((Produit) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setLibelle(t.getNewValue());
                String[] cols = {"libelleProd"};
                String[] values = {t.getNewValue()};
                String condition = "id=" + ((Produit) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).getId();
                db.queryUpdate("produit", cols, values, condition);
            }
        });
        DescripCol.setCellValueFactory(
                new PropertyValueFactory<Produit, String>("Descrip")
        );
        DescripCol.setCellFactory(TextFieldTableCell.forTableColumn());
        DescripCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Produit, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Produit, String> t) {
                ((Produit) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setDescrip(t.getNewValue());
                String[] cols = {"descripProd"};
                String[] values = {t.getNewValue()};
                String condition = "id=" + ((Produit) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).getId();
                db.queryUpdate("produit", cols, values, condition);
            }
        });
        prixCol.setCellValueFactory(
                new PropertyValueFactory<Produit, String>("prix")
        );
        prixCol.setCellFactory(TextFieldTableCell.forTableColumn());
        prixCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Produit, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Produit, String> t) {
                ((Produit) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setPrix(t.getNewValue());
                String[] cols = {"prix"};
                String[] values = {t.getNewValue()};
                String condition = "id=" + ((Produit) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).getId();
                db.queryUpdate("produit", cols, values, condition);
            }
        });
        qtyCol.setCellValueFactory(
                new PropertyValueFactory<Produit, String>("qte")
        );
        qtyCol.setCellFactory(TextFieldTableCell.forTableColumn());
        qtyCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Produit, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Produit, String> t) {
                ((Produit) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setQte(t.getNewValue());
                String[] cols = {"qte"};
                String[] values = {t.getNewValue()};
                String condition = "id=" + ((Produit) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).getId();
                db.queryUpdate("produit", cols, values, condition);
            }
        });
        /*ObservableList<String> ret = FXCollections.observableArrayList();
        String cols[] = {"nomFr"};
        ResultSet rstd = db.querySelect(cols, "fournisseur");
        while(rstd.next()){
            ret.add(rstd.getObject(1).toString());
        }

        ObservableList<String> frList = FXCollections.observableArrayList(ret);
        frCol.setCellValueFactory(
                //ComboBoxTableCell.forTableColumn(frList)
                new PropertyValueFactory<Produit, String>("idFr")
        );
        frCol.setCellFactory(TextFieldTableCell.forTableColumn());
        frCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Produit, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Produit, String> t) {
                String[] cols2 = {"id"};
                ResultSet res  = db.querySelect(cols2, "fournisseur");
                ObservableList<String> e = FXCollections.observableArrayList();
                
                try {
                    while(res.next()){
                        //System.out.println(res.getObject(1).toString() +" rere ");
                        e.add(res.getObject(1).toString());
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(magasinierInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                if(e.contains(t.getNewValue())) {
                    System.out.println(e.toString()+"new = "+t.getNewValue());
                    String[] cols = {"idFour"};
                String[] values = {t.getNewValue()};
                String condition = "id=" + ((Produit) t.getTableView().getItems().get(
                    t.getTablePosition().getRow()
                )).getId();
                    db.queryUpdate("produit", cols, values, condition);
                    ((Produit) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setIdFr(t.getNewValue());
                } else {
                    t.getTableView().getItems().get(
                            t.getTablePosition().getRow()
                    ).setIdFr( t.getTableView().getItems().get(
                            t.getTablePosition().getRow()
                    ).getIdFr());
                }
                /*String[] cols = {"idFour"};
                String[] values = {t.getNewValue()};
                String condition = "id=" + ((Produit) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).getId();
                db.queryUpdate("produit", cols, values, condition);
                condition = "id=" + t.getNewValue();
                ResultSet res = db.fcSelectCommand(new String[] {"*"},"fournisseur",condition);  
                ResultSetTableModel s = new ResultSetTableModel(res);
                if(s.getRowCount()>0) {
                    System.out.println(".handle()");
                ((Produit) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setIdFr(t.getNewValue());
                }\*
                
            }
        });*/
       table.setItems(prodData);

        final TextField addID = new TextField();
        addID.setPromptText("Id");
        //addID.setMaxWidth(idCol.getPrefWidth());
        final TextField addLib = new TextField();
        //addLib.setMaxWidth(LibCol.getPrefWidth());
        addLib.setPromptText("Libelle Produit");
        final TextField addDescrip = new TextField();
        //addDescrip.setMaxWidth(DescripCol.getPrefWidth());
        addDescrip.setPromptText("Description");
        final TextField addPrix = new TextField();
        //addPrix.setMaxWidth(DescripCol.getPrefWidth());
        addPrix.setPromptText("Prix");
        final TextField addQte = new TextField();
        //addQte.setMaxWidth(DescripCol.getPrefWidth());
        addQte.setPromptText("Quantité");

        final Button addButton = new Button("Ajouter");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Produit addProd = new Produit(
                        addID.getText(),
                        addLib.getText(),
                        addQte.getText(),
                        addDescrip.getText(),
                        addPrix.getText()
                        
                        //"1"
                );
                String[] cols = {"libelleProd", "descripProd", "prix", "qte"};
                String[] values = {addProd.getLibelle(), addProd.getDescrip(), addProd.getPrix(), addProd.getQte()};
                db.queryInsert("produit", cols, values);
                rsProd = db.querySelectAll("produit");
                prodData.clear();
                try {
                    while (rsProd.next()) {
                        prodData.add(new Produit(
                                rsProd.getObject(1).toString(),
                                rsProd.getObject(2).toString(),
                                rsProd.getObject(5).toString(),
                                rsProd.getObject(3).toString(),
                                rsProd.getObject(4).toString()
                        /*,"1"*/));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(magasinierInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
                //prodData.add(addProd);
                addID.clear();
                addLib.clear();
                addDescrip.clear();
                addPrix.clear();
                addQte.clear();
            }
        });
        addButton.setMinWidth(100);
        final Button delButton = new Button("Supprimer");
        delButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Produit currentProd = (Produit) table.getSelectionModel().getSelectedItem();
                prodData.remove(currentProd);
                db.queryDelete("produit", new String("id=" + currentProd.getId()));
            }

        });
        delButton.setMinWidth(100);
        //idCol.setSortType(TableColumn.SortType.ASCENDING);
        grid.add(label, 0, 2, 6, 1);
        grid.add(vbox, 0, 3, 6, 1);
        grid.add(addLib, 0, 4);
        grid.add(addDescrip, 1, 4);
        grid.add(addPrix, 2, 4);
        grid.add(addQte, 3, 4);
        grid.add(addButton, 4, 4);
        grid.add(delButton, 4, 5);

        // 2nd column --- Les Fournisseurs ---
        final Label labelFr = new Label("Liste de Fournisseurs");
        labelFr.setFont(new Font("Tahoma", 20));
        labelFr.setTranslateX(50);
        /*--- Table des fournisseurs ---*/
        TableView tableFr = new TableView();
        tableFr.setEditable(true);
        tableFr.setMaxWidth(400);

        TableColumn idFr = new TableColumn("ID Fournisseur");
        TableColumn nomFr = new TableColumn("Nom Fournisseur");
        TableColumn adressFr = new TableColumn("Adresse Fournisseur");
        TableColumn telFr = new TableColumn("Num Tel Fournisseur");
        tableFr.getColumns().addAll(idFr, nomFr, adressFr, telFr);
        final VBox vboxFr = new VBox();
        vboxFr.setSpacing(5);
        vboxFr.setPadding(new Insets(10, 0, 0, 10));
        vboxFr.getChildren().addAll(labelFr, tableFr);
        rsFr = db.querySelectAll("fournisseur");
        while (rsFr.next()) {
            fourData.add(new Fournisseur(rsFr.getObject(1).toString(), rsFr.getObject(2).toString(), rsFr.getObject(3).toString(), rsFr.getObject(4).toString()));
        }
        idFr.setCellValueFactory(
                new PropertyValueFactory<Fournisseur, String>("id")
        );
        nomFr.setCellValueFactory(
                new PropertyValueFactory<Fournisseur, String>("nom")
        );
        nomFr.setCellFactory(TextFieldTableCell.forTableColumn());
        nomFr.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Fournisseur, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Fournisseur, String> t) {
                ((Fournisseur) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setNom(t.getNewValue());
                String[] cols = {"nomFr"};
                String[] values = {t.getNewValue()};
                String condition = "id=" + ((Fournisseur) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).getId();
                db.queryUpdate("fournisseur", cols, values, condition);
            }
        });
        adressFr.setCellValueFactory(
                new PropertyValueFactory<Fournisseur, String>("adresse")
        );
        adressFr.setCellFactory(TextFieldTableCell.forTableColumn());
        adressFr.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Fournisseur, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Fournisseur, String> t) {
                ((Fournisseur) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setAdresse(t.getNewValue());
                String[] cols = {"adressFr"};
                String[] values = {t.getNewValue()};
                String condition = "id=" + ((Fournisseur) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).getId();
                db.queryUpdate("fournisseur", cols, values, condition);
            }
        });
        telFr.setCellValueFactory(
                new PropertyValueFactory<Fournisseur, String>("tel")
        );
        telFr.setCellFactory(TextFieldTableCell.forTableColumn());
        telFr.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Fournisseur, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Fournisseur, String> t) {
                ((Fournisseur) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setTel(t.getNewValue());
                String[] cols = {"telFr"};
                String[] values = {t.getNewValue()};
                String condition = "id=" + ((Fournisseur) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).getId();
                db.queryUpdate("fournisseur", cols, values, condition);
            }
        });
        tableFr.setItems(fourData);

        final TextField addIDFr = new TextField();
        addIDFr.setPromptText("Id");
        //addIDFr.setMaxWidth(idFr.getPrefWidth());
        final TextField addNomFr = new TextField();
        addNomFr.setPromptText("Nom Fournisseur");
        //addNomFr.setMaxWidth(nomFr.getPrefWidth());
        final TextField addAdresseFr = new TextField();
        addAdresseFr.setPromptText("Adresse Fournisseur");
        //addAdresseFr.setMaxWidth(adressFr.getPrefWidth());
        final TextField addTelFr = new TextField();
        addTelFr.setPromptText("Num Tel Fournisseur");
        //addTelFr.setMaxWidth(telFr.getPrefWidth());

        final Button addButtonFr = new Button("Ajouter");
        addButtonFr.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Fournisseur addFr = new Fournisseur(
                        addIDFr.getText(),
                        addNomFr.getText(),
                        addAdresseFr.getText(),
                        addTelFr.getText()
                );
                String[] cols = {"nomFr", "adressFr", "telFr"};
                if (!addFr.getNom().equals("") && !addFr.getAdresse().equals("") && !addFr.getTel().equals("")) {
                    String[] values = {addFr.getNom(), addFr.getAdresse(), addFr.getTel()};
                    db.queryInsert("fournisseur", cols, values);
                    rsFr = db.querySelectAll("fournisseur");
                    fourData.clear();
                    try {
                        while (rsFr.next()) {
                            fourData.add(new Fournisseur(rsFr.getObject(1).toString(), rsFr.getObject(2).toString(), rsFr.getObject(3).toString(), rsFr.getObject(4).toString()));
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(magasinierInterface.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //prodData.add(addProd);
                    addIDFr.clear();
                    addNomFr.clear();
                    addAdresseFr.clear();
                    addTelFr.clear();
                }
            }
        });
        addButtonFr.setMinWidth(100);
        final Button delButtonFr = new Button("Supprimer");
        delButtonFr.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Fournisseur currentFr = (Fournisseur) tableFr.getSelectionModel().getSelectedItem();
                fourData.remove(currentFr);
                db.queryDelete("fournisseur", new String("id=" + currentFr.getId()));
            }

        });
        delButtonFr.setMinWidth(100);
        grid.add(labelFr, 5, 2, 6, 1);
        grid.add(vboxFr, 5, 3, 6, 1);
        grid.add(addNomFr, 5, 4);
        grid.add(addAdresseFr, 6, 4);
        grid.add(addTelFr, 7, 4);
        grid.add(addButtonFr, 8, 4);
        grid.add(delButtonFr, 8, 5);

        //3rd Lign === Recherche ===
        // Produit
        final Label labelRech = new Label("Recherche Produit:");
        labelFr.setFont(new Font("Tahoma", 20));
        labelFr.setTranslateX(50);
        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(
                "ID Produit", "Libelle Produit", "Description Produit", "Prix", "Quantité")
        );
        cb.getSelectionModel().selectFirst();

        TextField RechProd = new TextField();
        RechProd.setPromptText("Recherche Produit");
        RechProd.setMaxWidth(RechProd.getPrefWidth());
        final Button rechBtn = new Button("Rechercher");
        rechBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String col = new String();
                switch (cb.getValue().toString()) {
                    case "Libelle Produit":
                        col = "libelleProd";
                        break;
                    case "Description Produit":
                        col = "descripProd";
                        break;
                    case "Prix":
                        col = "prix";
                        break;
                    case "Quantité":
                        col = "qte";
                        break;
                    case "ID Produit":
                        col = "id";
                        break;
                    default:
                        break;
                }
                String cond = col + " LIKE '%" + RechProd.getText() + "%'";
                rsProd = db.querySelectAll("produit", cond);
                prodData.clear();
                try {
                    while (rsProd.next()) {
                        prodData.add(new Produit(rsProd.getObject(1).toString(), rsProd.getObject(2).toString(), rsProd.getObject(3).toString(), rsProd.getObject(4).toString(), rsProd.getObject(4).toString()/*,"1"*/));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(magasinierInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
        rechBtn.setMinWidth(100);
        //cb.setMaxWidth(cb.getPrefWidth());
        grid.add(labelRech, 0, 6, 6, 1);
        grid.add(cb, 0, 7);
        grid.add(RechProd, 1, 7, 3, 1);
        grid.add(rechBtn, 4, 7);
        //Fournisseur
        final Label labelRechFr = new Label("Recherche Fournisseur:");
        labelFr.setFont(new Font("Tahoma", 20));
        labelFr.setTranslateX(50);
        ChoiceBox cbFr = new ChoiceBox(FXCollections.observableArrayList(
                "ID Fournisseur", "Nom Fournisseur", "Adresse Fournisseur", "Tel Fournisseur")
        );
        cbFr.getSelectionModel().selectFirst();

        TextField RechFr = new TextField();
        RechFr.setPromptText("Recherche Fournisseur");
        //RechFr.setMaxWidth(RechProd.getPrefWidth());
        final Button rechBtnFr = new Button("Rechercher");
        rechBtnFr.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String col = new String();
                switch (cbFr.getValue().toString()) {
                    case "Nom Fournisseur":
                        col = "nomFr";
                        break;
                    case "Adresse Fournisseur":
                        col = "adressFr";
                        break;
                    case "Tel Fournisseur":
                        col = "telFr";
                        break;
                    case "ID Fournisseur":
                        col = "id";
                        break;
                    default:
                        break;
                }
                String cond = col + " LIKE '%" + RechFr.getText() + "%'";
                rsFr = db.querySelectAll("fournisseur", cond);
                fourData.clear();
                try {
                    while (rsFr.next()) {
                        fourData.add(new Fournisseur(rsFr.getObject(1).toString(), rsFr.getObject(2).toString(), rsFr.getObject(3).toString(), rsFr.getObject(4).toString()));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(magasinierInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
        rechBtnFr.setMinWidth(100);
        //cb.setMaxWidth(cb.getPrefWidth());
        grid.add(labelRechFr, 5, 6, 6, 1);
        grid.add(cbFr, 5, 7);
        grid.add(RechFr, 6, 7, 2, 1);
        grid.add(rechBtnFr, 8, 7);

        //grid.setGridLinesVisible(true);
        Scene scene = new Scene(grid, 720, 500);
        primaryStage.setScene(scene);
        if (!primaryStage.isMaximized()) {
            primaryStage.setMaximized(true);
        }
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
