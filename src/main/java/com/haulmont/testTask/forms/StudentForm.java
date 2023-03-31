package com.haulmont.testTask.forms;

import com.haulmont.testTask.entity.Group;
import com.haulmont.testTask.entity.Student;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.LocalDateToDateConverter;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class StudentForm extends FormLayout {
    private Binder<Student> binder = new BeanValidationBinder<>(Student.class);
    private DatePicker birthday = new DatePicker("Дата рождения");
    private TextField lastName = new TextField("Фамилия");
    private TextField fistName = new TextField("Имя");
    private TextField patronymic = new TextField("Отчество");
    private ComboBox<Group> group = new ComboBox<>("Группа");
    private Button ok = new Button("OK");
    private Button Cancel = new Button("Отменить");
    private Student student;
    private Dialog dialog = new Dialog();

    public StudentForm(List<Group> groups) {
        addClassName("student-form");
        group.setItems(groups);
        group.setItemLabelGenerator(Group::getNumber);
        dialog.add(new VerticalLayout(lastName, fistName, patronymic, datePickerBasic(), group, createButtonsLayout()));
        binder.forField(patronymic)
                .bind("patronymic");
        binder.forField(birthday).withConverter(new LocalDateToDateConverter())
                .asRequired("Это поле обязательное для заполнения!")
                .bind(Student::getBirthday, Student::setBirthday);
        binder.forField(lastName)
                .asRequired("Это поле обязательное для заполнения!")
                .bind(Student::getLastName, Student::setLastName);
        binder.forField(fistName)
                .asRequired("Это поле обязательное для заполнения!")
                .bind(Student::getFistName, Student::setFistName);
        binder.forField(group)
                .asRequired("Это поле обязательное для заполнения!")
                .bind(Student::getGroup, Student::setGroup);

    }
    public void setStudent(Student student) {
        binder.setBean(student);
    }

    private Component datePickerBasic() {
        DatePicker.DatePickerI18n multiFormatI18n = new DatePicker.DatePickerI18n();
        multiFormatI18n.setDateFormats("yyyy-MM-dd", "MM/dd/yyyy",
                "dd.MM.yyyy");
        birthday.setI18n(multiFormatI18n);
        add(birthday);
        return birthday;
    }

    public Dialog getDialog() {
        return dialog;
    }


    private Component createButtonsLayout() { /*TODO Украшения кнопок чтобы нарядные были*/
        ok.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        ok.addClickListener(event -> validateAndSave());
        Cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        Cancel.addClickListener(event -> fireEvent(new StudentForm.CloseEvent(this)));
        return new HorizontalLayout(ok, Cancel);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new StudentForm.SaveEvent(this, binder.getBean()));
        }
    }

    public static abstract class StudentFormEvent extends ComponentEvent<StudentForm> {
        private final Student student;

        protected StudentFormEvent(StudentForm source, Student student) {
            super(source, false);
            this.student = student;
        }

        public Student getStudent() {
            return student;
        }
    }

    public static class SaveEvent extends StudentForm.StudentFormEvent {
        SaveEvent(StudentForm source, Student student) {
            super(source, student);
        }
    }

    public static class CloseEvent extends StudentForm.StudentFormEvent {
        CloseEvent(StudentForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
