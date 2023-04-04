package com.haulmont.testTask.forms;

import com.haulmont.testTask.entity.Group;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class GroupForm extends FormLayout {
    private final Binder<Group> binder = new BeanValidationBinder<>(Group.class);
    private final TextField number = new TextField("Номер группы");
    private final TextField faculty = new TextField("Факультет");
    private final Button ok = new Button("OK");
    private final Button cancel = new Button("Отменить");
    Dialog dialog = new Dialog();

    public GroupForm(List<Group> groups, Group gro) {
        addClassName("group-form");
        binder.forField(number)
                .asRequired("Это поле обязательное для заполнения!")
                .bind(Group::getNumber, Group::setNumber);
        binder.forField(faculty)
                .asRequired("Это поле обязательное для заполнения!")
                .bind(Group::getFaculty, Group::setFaculty);
        if (gro.getNumber() == null) {
            binder.forField(number)
                    .withValidator(
                            number -> groups.stream().noneMatch(g -> g.getNumber().equals(number)),
                            "Такая группа уже существует!")
                    .bind(Group::getNumber, Group::setNumber);
        }else{
            binder.forField(number)
                    .withValidator(
                            number -> groups.stream().filter(g -> !gro.getId().equals(g.getId())).noneMatch(g -> g.getNumber().equals(number)),
                            "Изменить на этот номер нельзя, такая группа уже существует!")
                    .bind(Group::getNumber, Group::setNumber);
        }
        dialog.add(new VerticalLayout(number, faculty, createButtonsLayout()));
    }


    public void setGroup(Group group) {
        binder.setBean(group);
    }

    public Dialog getDialog() {
        return dialog;
    }

    private Component createButtonsLayout() {
        ok.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        ok.addClickListener(event -> validateAndSave());
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));
        return new HorizontalLayout(ok, cancel);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public abstract static class GroupFormEvent extends ComponentEvent<GroupForm> {
        private final Group group;

        protected GroupFormEvent(GroupForm source, Group group) {
            super(source, false);
            this.group = group;
        }

        public Group getGroup() {
            return group;
        }
    }

    public static class SaveEvent extends GroupFormEvent {
        SaveEvent(GroupForm source, Group group) {
            super(source, group);
        }
    }

    public static class CloseEvent extends GroupFormEvent {
        CloseEvent(GroupForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
