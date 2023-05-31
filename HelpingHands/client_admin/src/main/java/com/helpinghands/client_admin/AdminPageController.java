package com.helpinghands.client_admin;

import com.helpinghands.domain.Admin;
import com.helpinghands.domain.CerereSponsor;
import com.helpinghands.service.IService;
import com.helpinghands.service.data.UserInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminPageController {
    private IService server;
    private UserInfo admin;

    ObservableList<CerereSponsor> cerereSponsorsModel = FXCollections.observableArrayList();

    @FXML
    Button logoutButton;

    @FXML
    Button acceptButton;

    @FXML
    Button declineButton;

    @FXML
    TableColumn<CerereSponsor,String> firmaColumn;

    @FXML
    TableColumn<CerereSponsor,String> cifColumn;

    @FXML
    TableColumn<CerereSponsor,String> telefonColumn;

    @FXML
    TableColumn<CerereSponsor,String> adresaColumn;

    @FXML
    TableColumn<CerereSponsor,String> sponsorizareColumn;

    @FXML
    TableColumn<CerereSponsor,String> voluntarColumn;

    @FXML
    TableView<CerereSponsor> cerereSponsorsList;

    public void setServer(IService server){
        this.server = server;
    }

    public void setAdmin(UserInfo admin){
        this.admin = admin;
    }

    @FXML
    public void logoutButtonClicked() throws IOException {
        try {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Goodbye friend!", "See you soon,"+admin.getUtilizator().getPrenume()+" "+admin.getUtilizator().getNume()+"!");

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/helpinghands/client_admin/loginPage.fxml"));
            AnchorPane root = loader.load();

            LoginPageController controller = loader.getController();
            controller.setService(server);

            Stage stage = new Stage();
            stage.setTitle("Helping Hands Administration");
            stage.setScene(new Scene(root,788,518));
            stage.show();

            server.logout(admin.getToken());

            //aici se inchide fereastra de adminpage
            Stage thisStage = (Stage) logoutButton.getScene().getWindow();
            thisStage.close();
        } catch (IllegalArgumentException e) {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", e.getMessage());
            return;
        } catch (Exception e) {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", e.getMessage());
            return;
        }
    }

}
