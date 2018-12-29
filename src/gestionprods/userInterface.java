/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionprods;

import BDD.Parameter;
import BDD.db_connection;
import classes.Facture;
import classes.Fournisseur;
import classes.User;
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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
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
public class userInterface extends Application {
    ResultSet rsUser;
    db_connection db = new db_connection(new Parameter().HOST_DB, new Parameter().USERNAME_DB, new Parameter().PASSWORD_DB, new Parameter().IPHOST, new Parameter().PORT);
    ObservableList<User> userData = FXCollections.observableArrayList();
    @Override
    public void start(Stage primaryStage) throws SQLException {
        primaryStage.setTitle("Gestion de stocks | Gestion utilisateurs");
        /* grid configuration */
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setManaged(false);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        /* set numbers of columns */
        for (int i = 0; i < 4; i++) {
            ColumnConstraints column = new ColumnConstraints(100);
            grid.getColumnConstraints().add(column);
        }
        /* Setting title */
        Text scenetitle = new Text("Superadmin");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));
        scenetitle.setTextAlignment(TextAlignment.CENTER);
        /* Liste utilisateurs */
        final Label label = new Label("Liste de utilisateurs");
        label.setFont(new Font("Tahoma", 20));
        label.setTranslateX(50);
        /* Table Users */
        //Setting table
        TableView tableUser = new TableView();
        tableUser.setEditable(true);
        tableUser.setMaxWidth(300);
        //Setting columns
        TableColumn idUserCol = new TableColumn("ID User");
        TableColumn usernameUserCol = new TableColumn("Username");
        TableColumn passwordUserCol = new TableColumn("Password");
        TableColumn roleUserCol = new TableColumn("Role");
        tableUser.getColumns().addAll(idUserCol, usernameUserCol, passwordUserCol,roleUserCol);
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(label, tableUser);
        /* Filling columns */
        rsUser = db.querySelectAll("utilisateur");
        
        while (rsUser.next()) {
            userData.add(
                    new User(
                            rsUser.getObject(1).toString(),
                            rsUser.getObject(2).toString(),
                            rsUser.getObject(3).toString(),
                            rsUser.getObject(4).toString()
                    )
            );
        }
        idUserCol.setCellValueFactory(
                new PropertyValueFactory<User, String>("id")
        );
        usernameUserCol.setCellValueFactory(
                new PropertyValueFactory<User, String>("username")
        );
        usernameUserCol.setCellFactory(TextFieldTableCell.forTableColumn());
        usernameUserCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<User, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<User, String> t) {
                ((User) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setUsername(t.getNewValue());
                String[] cols = {"username"};
                String[] values = {t.getNewValue()};
                String condition = "id=" + ((User) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).getId();
                db.queryUpdate("utilisateur", cols, values, condition);
            }
        });
        passwordUserCol.setCellValueFactory(
                new PropertyValueFactory<User, String>("password")
        );
        passwordUserCol.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordUserCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<User, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<User, String> t) {
                ((User) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setPassword(t.getNewValue());
                String[] cols = {"password"};
                String[] values = {t.getNewValue()};
                String condition = "id=" + ((User) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).getId();
                db.queryUpdate("utilisateur", cols, values, condition);
            }
        });
        roleUserCol.setCellValueFactory(
                new PropertyValueFactory<User, String>("role")
        );
        roleUserCol.setCellFactory(TextFieldTableCell.forTableColumn());
        roleUserCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<User, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<User, String> t) {
                ((User) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setRole(t.getNewValue());
                String[] cols = {"role"};
                String[] values = {t.getNewValue()};
                String condition = "id=" + ((User) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).getId();
                db.queryUpdate("utilisateur", cols, values, condition);
            }
        });
        tableUser.setItems(userData);
        /*Adding inputs Users */
        final TextField addIDUser = new TextField();
        addIDUser.setPromptText("Id User");
        //addID.setMaxWidth(idCol.getPrefWidth());
        final TextField addUsername = new TextField();
        //addLib.setMaxWidth(LibCol.getPrefWidth());
        addUsername.setPromptText("Username");
        final TextField addPasswordUser = new TextField();
        //addDescrip.setMaxWidth(DescripCol.getPrefWidth());
        addPasswordUser.setPromptText("Password User");
        final TextField addRoleUser = new TextField();
        //addDescrip.setMaxWidth(DescripCol.getPrefWidth());
        addRoleUser.setPromptText("Role User");
        
        final Button addButtonUser = new Button("Ajouter");
        addButtonUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                User addUser = new User(
                        addIDUser.getText(),
                        addUsername.getText(),
                        addPasswordUser.getText(),
                        addRoleUser.getText()
                );
                String[] cols = {"username", "password","role"};
                String[] values = {addUser.getUsername(), addUser.getPassword(),addUser.getRole()};
                db.queryInsert("utilisateur", cols, values);
                rsUser = db.querySelectAll("utilisateur");
                userData.clear();
                try {
                    while (rsUser.next()) {
                        userData.add(new User(
                                rsUser.getObject(1).toString(),
                                rsUser.getObject(2).toString(),
                                rsUser.getObject(3).toString(),
                                rsUser.getObject(4).toString()
                        ));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(magasinierInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
                //prodData.add(addProd);
                addIDUser.clear();
                addUsername.clear();
                addPasswordUser.clear();
                addRoleUser.clear();
            }
        });
        addButtonUser.setMinWidth(100);
        final Button delButtonUser = new Button("Supprimer");
        delButtonUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                User currentUser = (User) tableUser.getSelectionModel().getSelectedItem();
                userData.remove(currentUser);
                db.queryDelete("utilisateur", new String("id=" + currentUser.getId()));
            }
            
        });
        delButtonUser.setMinWidth(100);
        // Recherche Utilisateur
        final Label labelRech = new Label("Recherche utilisateur :");
        labelRech.setFont(new Font("Tahoma", 20));
        labelRech.setTranslateX(50);
        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(
                "ID User", "Username", "Password", "Role")
        );
        cb.getSelectionModel().selectFirst();

        TextField RechUser = new TextField();
        RechUser.setPromptText("Recherche utilisateur");
        //RechFr.setMaxWidth(RechProd.getPrefWidth());
        final Button rechBtn = new Button("Rechercher");
        rechBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String col = new String();
                switch (cb.getValue().toString()) {
                    case "ID User":
                        col = "id";
                        break;
                    case "Username":
                        col = "username";
                        break;
                    case "Password":
                        col = "password";
                        break;
                    case "Role":
                        col = "role";
                        break;
                    default:
                        break;
                }
                String cond = col + " LIKE '%" + RechUser.getText() + "%'";
                rsUser = db.querySelectAll("utilisateur", cond);
                userData.clear();
                try {
                    while (rsUser.next()) {
                        userData.add(
                                new User(
                                        rsUser.getObject(1).toString(),
                                        rsUser.getObject(2).toString(),
                                        rsUser.getObject(3).toString(),
                                        rsUser.getObject(4).toString()
                                )
                        );
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(magasinierInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
        rechBtn.setMinWidth(100);

        /* GRID : ADD components*/
        grid.add(scenetitle, 0, 0, 1, 1);
        grid.add(label, 0, 1, 2, 1);
        grid.add(vbox, 0, 2, 3, 1);
        grid.add(addUsername,0,3);
        grid.add(addPasswordUser,1,3);
        grid.add(addRoleUser,2,3);
        grid.add(addButtonUser,3,3);
        grid.add(delButtonUser,3,4);
        grid.add(cb,0,5);
        grid.add(RechUser,1,5,2,1);
        grid.add(rechBtn,3,5);
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
