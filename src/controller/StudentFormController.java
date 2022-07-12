package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import model.Student;
import util.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author : Gihan Madhusankha
 * 2022-07-12 9:44 AM
 **/

public class StudentFormController {
    public JFXTextField txtId;
    public JFXTextField txtEmail;
    public JFXTextField txtName;
    public JFXTextField txtContact;
    public JFXTextField txtNic;
    public JFXTextField txtAddress;
    public JFXButton btnAdd;
    public JFXButton btnClearForm;
    public JFXButton btnNew;
    public TextField txtSearch;
    public TableView<Student> tblStudent;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colEmail;
    public TableColumn colContact;
    public TableColumn colAddress;
    public TableColumn colNic;
    public TableColumn colOperate;
    private ObservableList<Student> obList = null;

    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("stId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("stName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colOperate.setCellValueFactory(param -> {
            ImageView edit = new ImageView("assets/edit.png");
            ImageView delete = new ImageView("assets/delete.png");

            HBox hBox = new HBox(edit, delete);
            edit.setFitHeight(20);
            edit.setFitWidth(20);
            delete.setFitHeight(20);
            delete.setFitWidth(20);
            edit.setStyle("-fx-cursor: Hand");
            delete.setStyle("-fx-cursor: Hand");

            hBox.setStyle("-fx-alignment: Center");
            HBox.setMargin(edit, new Insets(2,3,2,2));
            HBox.setMargin(delete, new Insets(2,2,2,3));

            return new ReadOnlyObjectWrapper<>(hBox);

        });

        try {
            loadAllStudents();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadAllStudents() throws SQLException, ClassNotFoundException {
        obList = FXCollections.observableArrayList();
        ResultSet resultSet = SQLUtil.executeQuery("SELECT * FROM student");

        while (resultSet.next()) {
            obList.add(new Student(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6)
            ));
        }
        tblStudent.setItems(obList);
    }

    public void newBtnOnAction(ActionEvent actionEvent) {
    }

    public void clearFormOnAction(ActionEvent actionEvent) {
        clearForm();
    }

    public void addBtnOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        boolean save = SQLUtil.executeUpdate("INSERT INTO Student VALUES(?,?,?,?,?,?)",
                txtId.getText(), txtName.getText(), txtEmail.getText(), txtContact.getText(), txtAddress.getText(), txtNic.getText());

        if (save) {
            obList.clear();
            loadAllStudents();
            clearForm();
            new Alert(Alert.AlertType.CONFIRMATION, "Saved Student").show();

        } else {
            new Alert(Alert.AlertType.ERROR, "Something else").show();
        }
    }

    private void clearForm() {
        txtId.clear();
        txtName.clear();
        txtEmail.clear();
        txtContact.clear();
        txtAddress.clear();
        txtNic.clear();
    }


}
