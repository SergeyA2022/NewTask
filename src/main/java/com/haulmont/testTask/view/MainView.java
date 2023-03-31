package com.haulmont.testTask.view;

import com.haulmont.testTask.entity.Group;
import com.haulmont.testTask.service.GroupService;
import com.haulmont.testTask.forms.GroupForm;
import com.haulmont.testTask.view.list.ListLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = ListLayout.class)
@PageTitle("Dean's office")
public class MainView extends VerticalLayout {
    private final GroupService service;
    private final Grid<Group> grid = new Grid<>(Group.class, false);
    private GroupForm form;
    private final Dialog dialog;

    public MainView(GroupService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();
        translationColumns();
        dialog = form.getDialog();
        add(getToolbar(), getContent(), dialog);
        updateList();
        closeEditor();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.setFlexGrow(1, grid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new GroupForm(service.findAllGroup());
        form.addListener(GroupForm.SaveEvent.class, this::saveGroup);
        form.addListener(GroupForm.CloseEvent.class, e -> closeEditor());
    }

    private void configureGrid() {
        grid.addClassNames("group-grid");
        grid.setSizeFull();
        grid.setColumns(Group.Fields.number, Group.Fields.faculty);
        grid.addComponentColumn(group -> {
            Button edit = new Button("Изменить");
            edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            edit.addClickListener(e -> editGroup(group));
            return edit;
        }).setWidth("150px").setFlexGrow(0);
        grid.addComponentColumn(group -> {
            Button delete = new Button("Удалить");
            delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
            delete.addClickListener(e -> deleteGroup(group));
            return delete;
        }).setWidth("150px").setFlexGrow(0);
        grid.getColumns().forEach(col -> col.setAutoWidth(true)); /* TODO Настройка столбцов*/
    }

    private void translationColumns() {
        Grid.Column<Group> numberColumn = grid.getColumnByKey(Group.Fields.number);
        numberColumn.setHeader("Номер Группы");
        Grid.Column<Group> facultyColumn = grid.getColumnByKey(Group.Fields.faculty);
        facultyColumn.setHeader("Факультет");
    }

    private HorizontalLayout getToolbar() {
        Button addGroupButton = new Button("Добавить группу");
        addGroupButton.addClickListener(e -> addGroup());
        var toolbar = new HorizontalLayout(addGroupButton);
        toolbar.addClassName("toolbar");/*TODO Добавление некоторых имен классов к компонентам упрощает последующую стилизацию приложения с помощью CSS*/
        return toolbar;
    }

    private void closeEditor() {
        form.setGroup(null);
        dialog.close();
        form.setVisible(false);
        removeClassName("editing");
    }

    private void saveGroup(GroupForm.SaveEvent event) {
        service.saveGroup(event.getGroup());
        updateList();
        closeEditor();
    }

    private void deleteGroup(Group group) {
        service.deleteGroup(group);
        updateList();
        closeEditor();
    }

    private void editGroup(Group group) {
        if (group == null) {
            closeEditor();
        } else {
            form.setGroup(group);
            dialog.open();
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void addGroup() {
        grid.asSingleSelect().clear();
        editGroup(new Group());
    }

    private void updateList() {
        grid.setItems(service.findAllGroup()); /*TODO Поиск по номеру и факультету*/
    }
}