/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionprods;

import BDD.Parameter;
import BDD.db_connection;
import classes.Facture;
import classes.FactureProduit;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
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
public class caissierInterface extends Application {
    
    ResultSet rsFact, rsProd, rsQteProd;
    db_connection db = new db_connection(new Parameter().HOST_DB, new Parameter().USERNAME_DB, new Parameter().PASSWORD_DB, new Parameter().IPHOST, new Parameter().PORT);
    ObservableList<Facture> factData = FXCollections.observableArrayList();
    ObservableList<FactureProduit> prodData = FXCollections.observableArrayList();

    //ObservableList<Fournisseur> fourData = FXCollections.observableArrayList();
    @Override
    public void start(Stage primaryStage) throws SQLException {
        primaryStage.setTitle("Gestion de stocks | CAISSIER");
        /* grid configuration */
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setManaged(false);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        /* set numbers of columns */
        for (int i = 0; i < 6; i++) {
            ColumnConstraints column = new ColumnConstraints(100);
            grid.getColumnConstraints().add(column);
        }
        /* Setting title */
        Text scenetitle = new Text("Caissier");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));
        scenetitle.setTextAlignment(TextAlignment.CENTER);
        /* Liste Factures */
        final Label label = new Label("Liste de Factures");
        label.setFont(new Font("Tahoma", 20));
        label.setTranslateX(50);
        /* Table Factures */
        //Setting table
        TableView tableFact = new TableView();
        tableFact.setEditable(true);
        tableFact.setMaxWidth(300);
        //Setting columns
        TableColumn idFactCol = new TableColumn("ID Facture");
        TableColumn dateFactCol = new TableColumn("Date Facture");
        TableColumn recieverFactCol = new TableColumn("To");
        tableFact.getColumns().addAll(idFactCol, dateFactCol, recieverFactCol);
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(label, tableFact);
        /* Filling columns */
        rsFact = db.querySelectAll("facture");
        while (rsFact.next()) {
            factData.add(
                    new Facture(
                            rsFact.getObject(1).toString(),
                            rsFact.getObject(2).toString(),
                            rsFact.getObject(3).toString()
                    )
            );
        }
        idFactCol.setCellValueFactory(
                new PropertyValueFactory<Facture, String>("id")
        );
        dateFactCol.setCellValueFactory(
                new PropertyValueFactory<Facture, String>("date")
        );
        dateFactCol.setCellFactory(TextFieldTableCell.forTableColumn());
        dateFactCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Facture, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Facture, String> t) {
                ((Facture) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setDate(t.getNewValue());
                String[] cols = {"date"};
                String[] values = {t.getNewValue()};
                String condition = "id=" + ((Facture) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).getId();
                db.queryUpdate("facture", cols, values, condition);
            }
        });
        recieverFactCol.setCellValueFactory(
                new PropertyValueFactory<Facture, String>("to")
        );
        recieverFactCol.setCellFactory(TextFieldTableCell.forTableColumn());
        recieverFactCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Facture, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Facture, String> t) {
                ((Facture) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setTo(t.getNewValue());
                String[] cols = {"reciever"};
                String[] values = {t.getNewValue()};
                String condition = "id=" + ((Facture) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).getId();
                db.queryUpdate("facture", cols, values, condition);
            }
        });
        /* Filling the table */
        tableFact.setItems(factData);
        /*Adding inputs factures */
        final TextField addIDFact = new TextField();
        addIDFact.setPromptText("Id Facture");
        //addID.setMaxWidth(idCol.getPrefWidth());
        final TextField addDateFact = new TextField();
        //addLib.setMaxWidth(LibCol.getPrefWidth());
        addDateFact.setPromptText("Date Facture");
        final TextField addRecieverFact = new TextField();
        //addDescrip.setMaxWidth(DescripCol.getPrefWidth());
        addRecieverFact.setPromptText("Recepteur de Facture");
        
        final Button addButtonFact = new Button("Ajouter");
        addButtonFact.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Facture addFact = new Facture(
                        addIDFact.getText(),
                        addDateFact.getText(),
                        addRecieverFact.getText()
                );
                String[] cols = {"date", "reciever"};
                String[] values = {addFact.getDate(), addFact.getTo()};
                db.queryInsert("facture", cols, values);
                rsFact = db.querySelectAll("facture");
                factData.clear();
                try {
                    while (rsFact.next()) {
                        factData.add(new Facture(rsFact.getObject(1).toString(), rsFact.getObject(2).toString(), rsFact.getObject(3).toString()));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(magasinierInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
                //prodData.add(addProd);
                addIDFact.clear();
                addDateFact.clear();
                addRecieverFact.clear();
            }
        });
        addButtonFact.setMinWidth(100);
        final Button delButtonFact = new Button("Supprimer");
        delButtonFact.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Facture currentFact = (Facture) tableFact.getSelectionModel().getSelectedItem();
                factData.remove(currentFact);
                db.queryDelete("fact_prod",new String("idFact = "+currentFact.getId()));
                db.queryDelete("facture", new String("id=" + currentFact.getId()));
            }
            
        });
        delButtonFact.setMinWidth(100);
        /* Title 2nd Table : Produits de Facture */
        Label labelProd = new Label();
        if (tableFact.getSelectionModel().isEmpty()) {
            labelProd.setText("Pas de facture selectionnée !");
            labelProd.setFont(new Font("Tahoma", 20));
            labelProd.setTranslateX(50);
        } else {
            Facture currentFact = (Facture) tableFact.getSelectionModel().getSelectedItem();
            labelProd.setText("Liste de Facture " + currentFact.getId());
            labelProd.setFont(new Font("Tahoma", 20));
            labelProd.setTranslateX(50);
            
        }
        /* Table Produits */
        //Setting table
        TableView tableProd = new TableView();
        tableProd.setEditable(true);
        tableProd.setMaxWidth(300);
        //Setting columns
        TableColumn idProdCol = new TableColumn("ID Produit");
        TableColumn libelleProdCol = new TableColumn("Libelle Produit");
        TableColumn qteProdCol = new TableColumn("Quantité de Facture");
        tableProd.getColumns().addAll(idProdCol, libelleProdCol, qteProdCol);
        final VBox vboxProd = new VBox();
        vboxProd.setSpacing(5);
        vboxProd.setPadding(new Insets(10, 10, 10, 10));
        vboxProd.getChildren().addAll(labelProd, tableProd);

        /* Refresh Function */
        tableFact.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Facture currentFact = (Facture) tableFact.getSelectionModel().getSelectedItem();
                labelProd.setText("Liste de Facture " + currentFact.getId());
                prodData.clear();
                /* Filling columns */
                String nomCol[] = {"id", "libelleProd", "qte"};
                String cond = "idFact = " + currentFact.getId();
                rsProd = db.SelectIn(nomCol, "produit", "id", "idProd", "fact_prod", cond);
                rsQteProd = db.querySelectAll("fact_prod", cond);
                try {
                    while (rsProd.next()) {
                        rsQteProd.next();
                        prodData.add(
                                new FactureProduit(
                                        rsProd.getObject(1).toString(),
                                        rsProd.getObject(2).toString(),
                                        rsQteProd.getObject(3).toString()
                                )
                        );
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(caissierInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
                idProdCol.setCellValueFactory(
                        new PropertyValueFactory<FactureProduit, String>("id")
                );
                libelleProdCol.setCellValueFactory(
                        new PropertyValueFactory<FactureProduit, String>("libelle")
                );
                qteProdCol.setCellValueFactory(
                        new PropertyValueFactory<FactureProduit, String>("qte")
                );
                qteProdCol.setCellFactory(TextFieldTableCell.forTableColumn());
                qteProdCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<FactureProduit, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<FactureProduit, String> t) {
                        Facture Fact = (Facture) tableFact.getSelectionModel().getSelectedItem();
                        ((FactureProduit) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())).setQte(t.getNewValue());
                        String[] cols = {"qte"};
                        String[] values = {t.getNewValue()};
                        String condition = "(idProd=" + ((FactureProduit) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())).getId() + ") AND (idFact = " + Fact.getId() + ")";
                        db.queryUpdate("fact_prod", cols, values, condition);
                    }
                });
                /* Filling the table */
                tableProd.setItems(prodData);
            }
        });
        /*Adding inputs produits */
        String cols[] = {"libelleProd"};
        ResultSet rsLibProd = db.querySelect(cols, "produit");
        ObservableList<String> libelles = FXCollections.observableArrayList();
        while (rsLibProd.next()) {
            // Here comes the problem
            // This shows all the different n_ecrou
            //System.out.println(rsLibProd.getObject(1).toString());

            libelles.add(new String(rsLibProd.getObject(1).toString()));
        }
        final ChoiceBox libProd = new ChoiceBox((ObservableList) libelles);
        libProd.getSelectionModel().selectFirst();
        libProd.setMinWidth(100);
        final TextField addQteProd = new TextField();
        //addLib.setMaxWidth(LibCol.getPrefWidth());
        addDateFact.setPromptText("Qte de Produit");
        final Button AddBtnProd = new Button("Ajouter");
        AddBtnProd.setMinWidth(100);
        AddBtnProd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Facture Fact = (Facture) tableFact.getSelectionModel().getSelectedItem();
                if (!tableFact.getSelectionModel().isEmpty()) {
                    String cols[] = {"id"};
                    String cond = "libelleProd LIKE '" + libProd.getSelectionModel().getSelectedItem().toString() + "'";
                    ResultSet RsProd = db.fcSelectCommand(cols, "produit", cond);
                    String id = "";
                    try {
                        RsProd.next();
                        id = RsProd.getObject(1).toString();
                    } catch (SQLException ex) {
                        Logger.getLogger(caissierInterface.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    FactureProduit prodAdd = new FactureProduit(id, "", "");
                    if (prodData.contains(prodAdd)) {
                        Integer indexProdExist = prodData.indexOf(prodAdd);
                        //System.out.println(".handle()"+indexProdExist);
                        FactureProduit prodExist = prodData.get(indexProdExist);
                        Integer qteTot = Integer.parseInt(prodExist.getQte()) + Integer.parseInt(addQteProd.getText());
                        prodAdd = new FactureProduit(Fact.getId(), prodExist.getId(), qteTot.toString());
                        String colsUpdate[] = {"idFact", "idProd", "qte"};
                        String valuesUpdate[] = {Fact.getId(), prodExist.getId(), prodAdd.getQte()};
                        String condUpdate = "( idFact like " + Fact.getId() + " ) AND ( idProd like " + prodExist.getId() + ")";
                        db.queryUpdate("fact_prod", colsUpdate, valuesUpdate, condUpdate);
                        //prodData.remove(prodAdd);
                    } else {
                        String colsInsert[] = {
                            Fact.getId(),
                            id,
                            addQteProd.getText()
                        };
                        db.queryInsert("fact_prod", colsInsert);
                    }
                    /*clear tableview and refresh it */
                    prodData.clear();
                    String nomCol[] = {"id", "libelleProd", "qte"};
                    cond = "idFact = " + Fact.getId();
                    rsProd = db.SelectIn(nomCol, "produit", "id", "idProd", "fact_prod", cond);
                    rsQteProd = db.querySelectAll("fact_prod", cond);
                    try {
                        while (rsProd.next()) {
                            rsQteProd.next();
                            prodData.add(
                                    new FactureProduit(
                                            rsProd.getObject(1).toString(),
                                            rsProd.getObject(2).toString(),
                                            rsQteProd.getObject(3).toString()
                                    )
                            );
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(caissierInterface.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            
        });
        delButtonFact.setMinWidth(100);
        final Button delBtnProd = new Button("Supprimer");
        delBtnProd.setMinWidth(100);
        delBtnProd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Facture currentFact = (Facture) tableFact.getSelectionModel().getSelectedItem();
                FactureProduit currentProd = (FactureProduit) tableProd.getSelectionModel().getSelectedItem();
                prodData.remove(currentProd);
                db.queryDelete("fact_prod",new String("idFact = "+currentFact.getId()+" AND idProd = "+currentProd.getId()));
                //db.queryDelete("facture", new String("id=" + currentFact.getId()));
            }
            
        });
        //Recherche factures
        final Label labelRech = new Label("Recherche Facture :");
        labelRech.setFont(new Font("Tahoma", 10));
        labelRech.setTranslateX(20);
        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(
                "ID Facture", "Date Facture", "Recepteur de facture")
        );
        cb.getSelectionModel().selectFirst();

        TextField RechFact = new TextField();
        RechFact.setPromptText("Recherche Facture");
        RechFact.setMaxWidth(RechFact.getPrefWidth());
        final Button rechBtn = new Button("Rechercher");
        rechBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String col = new String();
                switch (cb.getValue().toString()) {
                    case "ID Facture":
                        col = "id";
                        break;
                    case "Date Facture":
                        col = "date";
                        break;
                    case "Recepteur de facture":
                        col = "reciever";
                        break;
                    
                    default:
                        break;
                }
                String cond = col + " LIKE '%" + RechFact.getText() + "%'";
                rsFact = db.querySelectAll("facture", cond);
                factData.clear();
                try {
                    while (rsFact.next()) {
                        factData.add(new Facture(rsFact.getObject(1).toString(), rsFact.getObject(2).toString(), rsFact.getObject(3).toString()));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(magasinierInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
        rechBtn.setMinWidth(100);
        // Imprimer
        final Button imprBtn = new Button("Imprimer la facture");
        
        imprBtn.setMinWidth(318);
        imprBtn.setOnAction(e->{
            try {
                Facture currentFact = (Facture) tableFact.getSelectionModel().getSelectedItem();
                if (currentFact != null) {
                    PrintWriter writer = new PrintWriter(new String("Facture" + currentFact.getId()+".txt"), "UTF-8");
                    writer.println("Facture n° " + currentFact.getId() + " date : "+ currentFact.getDate());
                    writer.println("ID Produit --- Libelle Produit --- Prix unitaire --- Quantité --- Total Produit ");
                    /* Filling columns */
                    String nomCol[] = {"id", "libelleProd", "qte","prix"};
                    String cond = "idFact = " + currentFact.getId();
                    rsProd = db.SelectIn(nomCol, "produit", "id", "idProd", "fact_prod", cond);
                    rsQteProd = db.querySelectAll("fact_prod", cond);
                    Integer total = 0;
                    try {
                        while (rsProd.next()) {
                            rsQteProd.next();
                            writer.println(
                                rsProd.getObject(1).toString()+" ------------- "+
                                rsProd.getObject(2).toString()+ " ------------- "+
                                rsProd.getObject(4).toString()+" ------------- "+
                                rsQteProd.getObject(3).toString()+ " ------------- "+
                                Integer.parseInt(rsProd.getObject(4).toString())*Integer.parseInt(rsQteProd.getObject(3).toString())
                            );
                            total += Integer.parseInt(rsProd.getObject(4).toString())*Integer.parseInt(rsQteProd.getObject(3).toString());
                            
                        }
                        writer.println("Total à payer = " + total);
                    } catch (SQLException ex) {
                        Logger.getLogger(caissierInterface.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    writer.close();
                }
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(caissierInterface.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(caissierInterface.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        //Recherche produits
        /*
        final Label labelRechProd = new Label("Recherche Produit :");
        labelRechProd.setFont(new Font("Tahoma", 10));
        labelRechProd.setTranslateX(20);
        ChoiceBox cbProd = new ChoiceBox(FXCollections.observableArrayList(
                "ID Produit", "Libelle Produit", "Quantité")
        );
        cbProd.getSelectionModel().selectFirst();

        TextField RechProd = new TextField();
        RechProd.setPromptText("Recherche Produit");
        RechProd.setMaxWidth(RechProd.getPrefWidth());
        final Button rechBtnProd = new Button("Rechercher");
        rechBtnProd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String col = new String();
                Facture currentFact = (Facture) tableFact.getSelectionModel().getSelectedItem();
                if(cbProd.getValue().toString().equals("ID Produit")) {
                    String cond = "idProd = " + RechProd.getText() + " AND idFact = " + currentFact.getId();
                    ResultSet rsFactProd = db.querySelectAll("fact_prod",cond);
                    try {
                        rsFactProd.next();
                        String RechId = rsFactProd.getObject(2).toString();
                        //TODO : complete the search 
                    } catch (SQLException ex) {
                        Logger.getLogger(caissierInterface.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    factData.clear();
                    try {
                        while (rsProd.next()) {
                            prodData.add(
                                    new FactureProduit(
                                            rsProd.getObject(1).toString(),
                                            rsProd.getObject(2).toString(),
                                            rsProd.getObject(3).toString()
                                    )
                            );
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(magasinierInterface.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                /*String cond = col + " LIKE '%" + RechFact.getText() + "%'";
                rsFact = db.querySelectAll("facture", cond);
                factData.clear();
                try {
                while (rsFact.next()) {
                factData.add(new Facture(rsFact.getObject(1).toString(), rsFact.getObject(2).toString(), rsFact.getObject(3).toString()));
                }
                } catch (SQLException ex) {
                Logger.getLogger(magasinierInterface.class.getName()).log(Level.SEVERE, null, ex);
                }\*
            }

        });
        rechBtnProd.setMinWidth(100);
        */
        /* Adding components */
        grid.add(scenetitle, 0, 0, 1, 1);
        grid.add(label, 0, 1, 2, 1);
        grid.add(vbox, 0, 2, 3, 1);
        grid.add(addDateFact, 0, 3);
        grid.add(addRecieverFact, 1, 3);
        grid.add(addButtonFact, 2, 3);
        grid.add(delButtonFact, 2, 4);
        grid.add(labelProd, 3, 1, 2, 1);
        grid.add(vboxProd, 3, 2, 3, 1);
        grid.add(libProd, 3, 3);
        grid.add(addQteProd, 4, 3);
        grid.add(AddBtnProd, 5, 3);
        grid.add(delBtnProd, 5, 4);
        grid.add(labelRech, 0, 5, 3, 1);
        grid.add(cb, 0, 6);
        grid.add(RechFact, 1, 6);
        grid.add(rechBtn, 2, 6);
        /*grid.add(cbProd, 3, 6);
        grid.add(RechProd, 4, 6);
        grid.add(rechBtnProd, 5, 6);*/
        grid.add(imprBtn,3,5,3,1);
        /* Setting the scene */
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
