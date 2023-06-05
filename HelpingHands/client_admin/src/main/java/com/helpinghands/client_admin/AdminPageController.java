package com.helpinghands.client_admin;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.helpinghands.domain.Admin;
import com.helpinghands.domain.CerereSponsor;
import com.helpinghands.domain.Eveniment;
import com.helpinghands.domain.Post;
import com.helpinghands.service.IService;
import com.helpinghands.service.data.UserInfo;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AdminPageController {
    private IService server;
    private UserInfo admin;

    ObservableList<CerereSponsor> cerereSponsorsModel = FXCollections.observableArrayList();
    ObservableList<Eveniment> evenimentsModel = FXCollections.observableArrayList();

    @FXML
    Button logoutButton;
    @FXML
    Button notifyButton;

    @FXML
    Button acceptButton;

    @FXML
    Button declineButton;

    @FXML
    TextArea descriereNotificare;

    @FXML
    TableColumn<Eveniment,String> evenimentColumn;

    @FXML
    TableColumn<Eveniment,String> inceputColumn;

    @FXML
    TableColumn<Eveniment,String> sfarsitColumn;

    @FXML
    TableColumn<Eveniment,String> locatieColumn;

    @FXML
    TableColumn<Eveniment,String> descriereColumn;

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

    @FXML
    TableView<Eveniment> evenimentList;

    public void setServer(IService server){
        this.server = server;
    }

    public void setAdmin(UserInfo admin){
        this.admin = admin;
        initModel();
    }

    public void initModel(){
        Eveniment[] messages = server.getActualEvenimente();
//        List<Eveniment> evt = StreamSupport.stream(messages, false)
//                .collect(Collectors.toList());
        List<Eveniment> evt = List.of(messages);
        System.out.println(evt.toArray().length);
        System.out.println(evt);
        //evt.forEach(System.out::println);
        evenimentsModel.setAll(evt);
    }

    @FXML
    public void initialize(){
        //pt evenimente
        evenimentColumn.setCellValueFactory(new PropertyValueFactory<Eveniment,String>("name"));
        locatieColumn.setCellValueFactory(new PropertyValueFactory<Eveniment,String>("location"));
        inceputColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getStartDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        ));
        sfarsitColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getStartDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        ));
        descriereColumn.setCellValueFactory(new PropertyValueFactory<Eveniment,String>("description"));

        descriereNotificare.setWrapText(true);

        //models
        //cerereSponsorsList.setItems(cerereSponsorsModel);
        evenimentList.setItems(evenimentsModel);
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

    @FXML
    public void notifyUsersButtonClicked(){
        try{
            Eveniment eveniment = (Eveniment) evenimentList.getSelectionModel().getSelectedItem();
            if(eveniment == null)
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Selecteaza un eveniment pentru a putea trimite notificarea asupra acestuia!");
            else {
                String descriere = descriereNotificare.getText();
                Post newPost = new Post(descriere, LocalDateTime.now(),eveniment,this.admin.getUtilizator());
                this.server.addPost(newPost);
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Utilizatorii au fost notificati asupra evenimentului cu succes!");
            }
        }catch (IllegalArgumentException e) {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", e.getMessage());
            return;
        } catch (Exception e) {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", e.getMessage());
            return;
        }
    }
}
