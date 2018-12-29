/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionprods;
import BDD.Parameter;
import BDD.db_connection;
import java.sql.ResultSet;
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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Lassad
 */
public class GestionProds extends Application {

    ResultSet rs;
    db_connection db = new db_connection(new Parameter().HOST_DB, new Parameter().USERNAME_DB, new Parameter().PASSWORD_DB, new Parameter().IPHOST, new Parameter().PORT);
    String username1, password1, hak;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gestion de stocks | LOGIN");
        //primaryStage.setMaximized(true);
        //primaryStage.setResizable(false);
        primaryStage.setWidth(500);
        primaryStage.setHeight(500);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);
        //grid.setGridLinesVisible(true);

        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                rs = db.querySelectAll("utilisateur", "Username='" + userTextField.getText() + "' and Password='" + pwBox.getText() + "'");
                try {
                    while (rs.next()) {
                        username1 = rs.getString("Username");
                        password1 = rs.getString("Password");
                        hak = rs.getString("Type");
                    }
                } catch (SQLException ex) {
                    actiontarget.setFill(Color.FIREBRICK);
                    actiontarget.setText("Probleme au niveau de base de données !");
                }

                if (username1 == null && password1 == null) {
                    actiontarget.setFill(Color.FIREBRICK);
                    actiontarget.setText("le nom d'utilisateur ou le mot de passe est incorrect !");
                } else {
                    actiontarget.setFill(Color.FIREBRICK);
                    try {
                        ResultSet rsRole = db.querySelectAll("roles", "id='" + rs.getInt("role") + "'");
                        String roleLibelle = new String();
                        while (rsRole.next()) {
                            roleLibelle = rsRole.getString("libelle");
                        }
                        switch (roleLibelle) {
                            case "magasinier":
                                new magasinierInterface().start(primaryStage);
                                break;
                            case "caissier":
                                new caissierInterface().start(primaryStage);
                                break;
                            case "superadmin":
                                new mainSAInterface().start(primaryStage);
                                break;
                            default:
                                break;
                        }
                        actiontarget.setText(rs.getString("role") + "=>" + roleLibelle);
                        /*redirectInterface a = new redirectInterface();
                        a.start(primaryStage);*/
                        //new redirectInterface().start(primaryStage);
                    } catch (SQLException ex) {
                        Logger.getLogger(GestionProds.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        Scene scene = new Scene(grid, 250, 250);
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
