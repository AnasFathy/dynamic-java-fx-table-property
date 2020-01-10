package com.sine.fx.property.dynamic;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Test extends Application
{
	public static void main(String[] args)
	{
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{

		List<Student> students = new LinkedList<>();
		students.add(PropertyAttcher.attach(Student.class));
		students.add(PropertyAttcher.attach(Student.class));
		students.add(PropertyAttcher.attach(Student.class));
		students.add(PropertyAttcher.attach(Student.class));
		VBox box = new VBox();
		TableView<Student> table = new TableView<>();
		TableColumn<Student, String> nameColumn = new TableColumn<>("name");
		TableColumn<Student, Integer> ageColumn = new TableColumn<>("age");
		nameColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
		ageColumn.setCellValueFactory(new PropertyValueFactory<Student, Integer>("age"));
		table.getColumns().add(nameColumn);
		table.getColumns().add(ageColumn);

		box.getChildren().add(table);
		primaryStage.setScene(new Scene(box));
		primaryStage.show();

		Button button = new Button("click me");
		button.setOnAction(eve -> students.forEach(student -> {
			student.setName("Anoos " + new Random().nextInt(100));
			student.setAge(new Random().nextInt(71));
		}));
		box.getChildren().add(button);
		table.getItems().addAll(students);
	}

}
