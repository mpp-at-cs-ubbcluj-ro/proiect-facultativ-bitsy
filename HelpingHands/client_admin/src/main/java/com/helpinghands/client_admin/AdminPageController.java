package com.helpinghands.client_admin;

import com.helpinghands.domain.Admin;
import com.helpinghands.domain.CerereSponsor;
import com.helpinghands.service.IService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AdminPageController {
    private IService server;
    private Admin admin;

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

}
