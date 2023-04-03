package com.haulmont.testTask.view;

import com.haulmont.testTask.entity.Group;
import com.haulmont.testTask.entity.Student;
import com.haulmont.testTask.forms.StudentForm;
import com.haulmont.testTask.service.GroupService;
import com.haulmont.testTask.service.StudentService;
import com.haulmont.testTask.view.list.ListLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "students", layout = ListLayout.class)
@PageTitle("Students")
public class StudentsView extends Div {
    private final GroupService service;
    private final StudentService studentService;
    private final Grid<Student> grid = new Grid<>(Student.class);
    private final TextField lastName = new TextField();
    private final ComboBox<Group> group = new ComboBox<>();
    private final Button addGroupButton = new Button("Добавить студента");
    private final Button apply = new Button("Применить");
    private StudentForm form;
    private final Dialog dialog;

    public StudentsView(GroupService service, StudentService studentService) {
        this.service = service;
        this.studentService = studentService;
        addClassName("Students-view");
        setSizeFull();
        configureGrid();
        configureForm();
        translationColumns();
        dialog = form.getDialog();
        add(getToolbar(), getContent(), dialog);
        updateList();
        closeEditor();
    }

    private void configureForm() {
        form = new StudentForm(service.findAllGroups());
        form.addListener(StudentForm.SaveEvent.class, this::saveStudent);
        form.addListener(StudentForm.CloseEvent.class, e -> closeEditor());
    }

    private void configureGrid() {
        grid.addClassNames("student-grid");
        grid.setSizeFull();
        grid.setColumns("lastName", "fistName", "patronymic", "birthday");
        grid.addColumn(students -> students.getGroup().getNumber()).setHeader("Группа");
        grid.addComponentColumn(student -> {
            Button edit = new Button("Изменить");
            edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            edit.addClickListener(e -> editGroup(student));
            return edit;
        }).setWidth("50px").setFlexGrow(2);
        grid.addComponentColumn(student -> {
            Button delete = new Button("Удалить");
            delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
            delete.addClickListener(e -> deleteStudent(student));
            return delete;
        }).setWidth("150px").setFlexGrow(2);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void translationColumns() {
        Grid.Column<Student> lastNameColumn = grid.getColumnByKey(Student.Fields.lastName);
        lastNameColumn.setHeader("Фамилия");
        Grid.Column<Student> fistNameColumn = grid.getColumnByKey(Student.Fields.fistName);
        fistNameColumn.setHeader("Имя");
        Grid.Column<Student> patronymicColumn = grid.getColumnByKey(Student.Fields.patronymic);
        patronymicColumn.setHeader("Отчество");
        Grid.Column<Student> birthdayColumn = grid.getColumnByKey(Student.Fields.birthday);
        birthdayColumn.setHeader("Дата рождения");
    }

    private HorizontalLayout getToolbar() {
        group.setItems(service.findAllGroups());
        group.setItemLabelGenerator(Group::getNumber);
        group.setClearButtonVisible(true);
        lastName.setPlaceholder("Фамилия");
        lastName.setClearButtonVisible(true);
        group.setPlaceholder("Группа");
        addGroupButton.addClickListener(e -> addStudent());
        apply.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        apply.addClickListener(event -> {
            if (group.getValue() == null) {
                updateList(lastName);
            } else {
                updateList(lastName.getValue(), group.getValue());
            }
        });
        var toolbar = new HorizontalLayout(lastName, group, apply, addGroupButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void saveStudent(StudentForm.SaveEvent event) {
        studentService.saveStudent(event.getStudent());
        updateList();
        closeEditor();
    }

    private void deleteStudent(Student student) {
        studentService.deleteStudent(student);
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setStudent(null);
        dialog.close();
        form.setVisible(false);
        removeClassName("editing");
    }

    private VerticalLayout getContent() {
        VerticalLayout content = new VerticalLayout(grid);
        content.setFlexGrow(2, grid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void editGroup(Student student) {
        if (student == null) {
            closeEditor();
        } else {
            form.setStudent(student);
            dialog.open();
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void addStudent() {
        grid.asSingleSelect().clear();
        editGroup(new Student());
    }

    public void updateList(TextField lastName) {
        grid.setItems(studentService.findAllStudentName(lastName.getValue()));
    }

    public void updateList(String lastName, Group group) {
        grid.setItems(studentService.findAllStudentGroup(lastName, group));
    }

    private void updateList() {
        grid.setItems(studentService.findAllStudent());
    }
}
