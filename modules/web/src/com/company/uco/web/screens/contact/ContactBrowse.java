package com.company.uco.web.screens.contact;

import com.company.uco.entity.Contact;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.screen.*;

@UiController("uco_Contacts.browse")
@UiDescriptor("contact-browse.xml")
@LookupComponent("contactsTable")
@LoadDataBeforeShow
public class ContactBrowse extends StandardLookup<Contact> {
}