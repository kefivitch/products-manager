/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionprods;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Lassad
 */
public class mainSAInterface extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Button btnStock = new Button();
        btnStock.setText("Gestion des stocks & fournisseurs");
        btnStock.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                try {
                    new magasinierInterface().start(primaryStage);
                } catch (SQLException ex) {
                    Logger.getLogger(mainSAInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        btnStock.setMinWidth(300);
        Button btnFacture = new Button();
        btnFacture.setText("Gestion des factures");
        btnFacture.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                try {
                    new caissierInterface().start(primaryStage);
                } catch (SQLException ex) {
                    Logger.getLogger(mainSAInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        btnFacture.setMinWidth(300);
        Button btnUser = new Button();
        btnUser.setText("Gestion des utilisateurs");
        btnUser.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                try {
                    new userInterface().start(primaryStage);
                } catch (SQLException ex) {
                    Logger.getLogger(mainSAInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        btnUser.setMinWidth(300);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setManaged(false);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        /* set numbers of columns */
        for (int i = 0; i < 1; i++) {
            ColumnConstraints column = new ColumnConstraints(300);
            grid.getColumnConstraints().add(column);
        }
        grid.add(btnStock, 0, 0);
        grid.add(btnFacture, 0, 1);
        grid.add(btnUser, 0, 2);
        Scene scene = new Scene(grid,400, 300); 
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
