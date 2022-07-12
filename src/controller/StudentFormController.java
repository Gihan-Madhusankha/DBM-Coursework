package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import model.Student;
import util.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

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
    private Student student = null;

    public void initialize() {
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
            HBox.setMargin(edit, new Insets(2, 3, 2, 2));
            HBox.setMargin(delete, new Insets(2, 2, 2, 3));

            clickedEditBtn(edit);
            clickedDeleteBtn(delete);

            return new ReadOnlyObjectWrapper<>(hBox);
        });

        try {
            loadAllStudents();
            searchStudent();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void searchStudent() {
        FilteredList<Student> filteredList = new FilteredList<>(obList, b -> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {

            filteredList.setPredicate(searchModel -> {

                if (newValue.isEmpty() || newValue == null) {
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();
                if (searchModel.getStId().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (searchModel.getStName().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (searchModel.getEmail().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (searchModel.getContact().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (searchModel.getAddress().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                }
                return searchModel.getNic().toLowerCase().indexOf(searchKeyword) > -1;

            });
        });

        SortedList<Student> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tblStudent.comparatorProperty());
        tblStudent.setItems(sortedList);
    }

    private void clickedDeleteBtn(ImageView delete) {
        delete.setOnMouseClicked(event -> {
            student = tblStudent.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure delete " + student.getStId() + " student?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();

            if (buttonType.get().equals(ButtonType.YES)) {
                try {
                    boolean d = SQLUtil.executeUpdate("DELETE FROM Student WHERE student_id = ?", student.getStId());

                    if (d) {
                        obList.clear();
                        loadAllStudents();
                        clearForm();
                        new Alert(Alert.AlertType.CONFIRMATION, "Deleted").show();

                    } else {
                        new Alert(Alert.AlertType.ERROR, "Something else").show();
                    }

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }


        });
    }

    private void clickedEditBtn(ImageView edit) {
        edit.setOnMouseClicked(event -> {
            student = tblStudent.getSelectionModel().getSelectedItem();
            txtId.setText(student.getStId());
            txtName.setText(student.getStName());
            txtEmail.setText(student.getEmail());
            txtContact.setText(student.getContact());
            txtAddress.setText(student.getAddress());
            txtNic.setText(student.getNic());
            txtId.setEditable(false);
            btnAdd.setText("UPDATE");
        });
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

    public void clearFormOnAction(ActionEvent actionEvent) {
        clearForm();
    }

    public void addBtnOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        if (btnAdd.getText().equals("ADD")) {

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

        } else {
            boolean update = SQLUtil.executeUpdate("UPDATE Student SET student_name = ?, email = ?, contact = ?, address = ?, nic = ? WHERE student_id = ?",
                    txtName.getText(), txtEmail.getText(), txtContact.getText(), txtAddress.getText(), txtNic.getText(), txtId.getText());

            if (update) {
                obList.clear();
                loadAllStudents();
                clearForm();
                new Alert(Alert.AlertType.CONFIRMATION, "Updated").show();

            } else {
                new Alert(Alert.AlertType.ERROR, "Something else").show();
            }
        }
    }

    private void clearForm() {
        txtId.clear();
        txtName.clear();
        txtEmail.clear();
        txtContact.clear();
        txtAddress.clear();
        txtNic.clear();
        txtId.setEditable(true);
        btnAdd.setText("ADD");
    }


}
