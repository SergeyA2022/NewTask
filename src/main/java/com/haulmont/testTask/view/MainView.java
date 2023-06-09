package com.haulmont.testTask.view;

import com.haulmont.testTask.entity.Group;
import com.haulmont.testTask.service.GroupService;
import com.haulmont.testTask.forms.GroupForm;
import com.haulmont.testTask.service.StudentService;
import com.haulmont.testTask.view.list.ListLayout;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = ListLayout.class)
@PageTitle("Dean's office")
public class MainView extends VerticalLayout {
    private final GroupService service;
    private final StudentService studentService;
    private final Grid<Group> grid = new Grid<>(Group.class, false);
    private Dialog dialog;
    Span status = new Span();
    ConfirmDialog confirmDialog = new ConfirmDialog();

    public MainView(GroupService service, StudentService studentService) {
        this.service = service;
        this.studentService = studentService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        translationColumns();
        add(getToolbar(), getContent());
        updateList();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.setFlexGrow(1, grid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    public GroupForm configureForm(Group group) {
        GroupForm form = new GroupForm(service.findAllGroups(), group);
        form.addListener(GroupForm.SaveEvent.class, event -> saveGroup(event, form));
        form.addListener(GroupForm.CloseEvent.class, e -> closeEditor(form));
        dialog = form.getDialog();
        add(dialog);
        return form;
    }

    private void configureGrid() {
        grid.addClassNames("group-grid");
        grid.setSizeFull();
        grid.setColumns(Group.Fields.number, Group.Fields.faculty);
        grid.addComponentColumn(group -> {
            Button edit = new Button("Изменить");
            edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            edit.addClickListener(e -> editGroup(group, configureForm(group)));
            return edit;
        }).setWidth("150px").setFlexGrow(0);

        HorizontalLayout layout = new HorizontalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        confirmDialog.setHeader("Ошибка");
        confirmDialog.setText(new Html(
                "<p>Запрещено удалять группу в которой есть студенты!</p>"));
        confirmDialog.setConfirmText("OK");
        confirmDialog.addConfirmListener(event -> setStatus("Acknowledged"));

        grid.addComponentColumn(group -> {
            Button delete = new Button("Удалить");
            delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
            layout.add(delete, status);
            add(layout);
            delete.addClickListener(e -> {
                if ((studentService.findAllStudentGroup(null, group).stream().count() <= 0)) {
                    deleteGroup(group);
                } else {
                    confirmDialog.open();
                }
                status.setVisible(false);
            });
            return delete;
        }).setWidth("150px").setFlexGrow(0);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void setStatus(String value) {
        status.setText("Status: " + value);
        status.setVisible(true);
    }

    private void translationColumns() {
        Grid.Column<Group> numberColumn = grid.getColumnByKey(Group.Fields.number);
        numberColumn.setHeader("Номер Группы");
        Grid.Column<Group> facultyColumn = grid.getColumnByKey(Group.Fields.faculty);
        facultyColumn.setHeader("Факультет");
    }

    private HorizontalLayout getToolbar() {
        Button addGroupButton = new Button("Добавить группу");
        addGroupButton.addClickListener(e -> {
            addGroup(configureForm(new Group()));
        });
        var toolbar = new HorizontalLayout(addGroupButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void closeEditor(GroupForm form) {
        form.setGroup(null);
        dialog.close();
        form.setVisible(false);
        removeClassName("editing");
    }

    private void saveGroup(GroupForm.SaveEvent event, GroupForm form) {
        service.saveGroup(event.getGroup());
        updateList();
        closeEditor(form);
    }

    private void deleteGroup(Group group) {
        service.deleteGroup(group);
        updateList();
    }

    private void editGroup(Group group, GroupForm form) {
        if (group == null) {
            closeEditor(form);
        } else {
            form.setGroup(group);
            dialog.open();
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void addGroup(GroupForm form) {
        grid.asSingleSelect().clear();
        editGroup(new Group(), form);
    }

    private void updateList() {
        grid.setItems(service.findAllGroups());
    }
}