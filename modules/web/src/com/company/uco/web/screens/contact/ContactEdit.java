package com.company.uco.web.screens.contact;

import com.company.uco.entity.Account;
import com.company.uco.entity.ContactType;
import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.screen.*;
import com.company.uco.entity.Contact;

import javax.inject.Inject;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UiController("uco_Contacts.edit")
@UiDescriptor("contact-edit.xml")
@EditedEntityContainer("contactDc")
@LoadDataBeforeShow
public class ContactEdit extends StandardEditor<Contact> {

    @Inject
    private Notifications notifications;

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern VALID_PHONE_NUMBER_REGEX = Pattern.compile(
            "\\d{10}",
            Pattern.CASE_INSENSITIVE);

    @Subscribe
    public void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        Contact contact = getEditedEntity();
        ContactType contactType = contact.getContactType();
        boolean validate = true;
        if (ContactType.EMAIL.equals(contactType)) {
            validate = isValidateEmail(contact.getValue());
        } else if (ContactType.PHONE.equals(contactType)) {
            validate = isValidatePhone(contact.getValue());
        }
        if (!validate) {
            notifications.create()
                    .withCaption("Description is empty!")
                    .show();
            event.preventCommit();
        }
    }

    private boolean isValidateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    private boolean isValidatePhone(String emailStr) {
        Matcher matcher = VALID_PHONE_NUMBER_REGEX.matcher(emailStr);
        return matcher.find();
    }

//    @Subscribe("contactTypeField")
//    public void onContactTypeFieldValueChange(HasValue.ValueChangeEvent<ContactType> event) {
//        ContactType contactType = event.getValue();
//        if (ContactType.PHONE.equals(contactType)) {
//            maskValueField.setMask("+#(###)###-##-##");
//            valueField.setVisible(false);
//            maskValueField.setVisible(true);
//        } else {
//            valueField.setVisible(true);
//            maskValueField.setVisible(false);
//        }
//    }

}