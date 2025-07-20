package com.healconnect.healconnect.util; // Corrected package

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image; // Added import
import javafx.scene.image.ImageView; // Added import
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class ActionButtonCellFactory<S> implements Callback<TableColumn<S, Void>, TableCell<S, Void>> { // Changed Integer to Void

    private final ButtonHandler<S> viewHandler;
    private final ButtonHandler<S> editHandler;
    private final ButtonHandler<S> deleteHandler;

    public ActionButtonCellFactory(ButtonHandler<S> viewHandler,
                                   ButtonHandler<S> editHandler,
                                   ButtonHandler<S> deleteHandler) {
        this.viewHandler = viewHandler;
        this.editHandler = editHandler;
        this.deleteHandler = deleteHandler;
    }

    @Override
    public TableCell<S, Void> call(TableColumn<S, Void> param) { // Changed Integer to Void
        return new TableCell<S, Void>() { // Changed Integer to Void
            private final HBox container = new HBox(5);
            private final Button viewBtn = new Button("", new ImageView(new Image(getClass().getResourceAsStream("/images/view_icon.png"), 16, 16, true, true)));
            private final Button editBtn = new Button("", new ImageView(new Image(getClass().getResourceAsStream("/images/edit_icon.png"), 16, 16, true, true)));
            private final Button deleteBtn = new Button("", new ImageView(new Image(getClass().getResourceAsStream("/images/delete_icon.png"), 16, 16, true, true)));

            {
                viewBtn.getStyleClass().add("table-action-button");
                editBtn.getStyleClass().add("table-action-button");
                deleteBtn.getStyleClass().add("table-action-button");

                container.getChildren().addAll(viewBtn, editBtn, deleteBtn);
            }

            @Override
            protected void updateItem(Void item, boolean empty) { // Changed Integer to Void
                super.updateItem(item, empty);
                if (empty) { // No need to check item == null for Void
                    setGraphic(null);
                } else {
                    S rowData = getTableView().getItems().get(getIndex());

                    viewBtn.setOnAction(e -> viewHandler.handle(rowData));
                    editBtn.setOnAction(e -> editHandler.handle(rowData));
                    deleteBtn.setOnAction(e -> deleteHandler.handle(rowData));

                    setGraphic(container);
                }
            }
        };
    }

    public interface ButtonHandler<S> {
        void handle(S data);
    }
}
