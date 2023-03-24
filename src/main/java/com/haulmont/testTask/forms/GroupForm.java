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
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class GroupForm extends FormLayout{
    Binder<Group> binder = new BeanValidationBinder<>(Group.class);
    TextField number = new TextField("Номер группы");
    TextField faculty = new TextField("Факультет");
    Button ok = new Button("OK");
    Button Cancel = new Button("Отменить");
    private Group group;
    Dialog dialog = new Dialog();

    public GroupForm() {
        addClassName("group-form");
        dialog.add(new VerticalLayout(number, faculty, createButtonsLayout()));
        binder.bindInstanceFields(this);

    }

    public Dialog getDialog() {
        return dialog;
    }


    public void setGroup(Group group) {
        this.group = group;
        binder.readBean(group);
    }
    /*TODO Украшения кнопок чтобы нарядные были*/
    private Component createButtonsLayout() {
        ok.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        ok.addClickListener(event -> validateAndSave());
        Cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));

        return new HorizontalLayout(ok, Cancel);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(group);
            fireEvent(new SaveEvent(this, group));
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    // Events
    public static abstract class GroupFormEvent extends ComponentEvent<GroupForm> {
        private Group group;

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
        return getEventBus().addListener(eventType,listener);
    }

}
