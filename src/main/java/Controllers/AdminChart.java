package Controllers;

import Service.Repondservice;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.Map;

public class AdminChart {

    @FXML
    private VBox pieChartContainer;

    private Repondservice repondservice;

    @FXML
    public void initialize() {
        repondservice = new Repondservice();
        PieChart pieChart = createPieChart();
        pieChartContainer.getChildren().add(pieChart);
    }

    private PieChart createPieChart() {
        Map<String, Integer> data = repondservice.fetchDataFromDatabase();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        data.forEach((status, count) -> pieChartData.add(new PieChart.Data(status, count)));

        PieChart pieChart = new PieChart(pieChartData);
       // pieChart.setTitle("Reclamation Status Distribution");

        return pieChart;
    }
    @FXML
    private void GoToAdmin (ActionEvent event) {
        try {
            Parent adminLoginRoot = FXMLLoader.load(getClass().getResource("/Main/fenetre1_reclamation.fxml"));
            Scene adminLoginScene = new Scene(adminLoginRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(adminLoginScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
}}
