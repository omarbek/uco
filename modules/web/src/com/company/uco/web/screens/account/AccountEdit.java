package com.company.uco.web.screens.account;

import com.company.uco.entity.Contact;
import com.company.uco.web.screens.contact.ContactBrowse;
import com.company.uco.web.screens.contact.ContactEdit;
import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.EntityStates;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.actions.list.EditAction;
import com.haulmont.cuba.gui.actions.list.RefreshAction;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.uco.entity.Account;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@UiController("uco_Account.edit")
@UiDescriptor("account-edit.xml")
@EditedEntityContainer("accountDc")
@LoadDataBeforeShow
public class AccountEdit extends StandardEditor<Account> {
    @Inject
    private DataManager dataManager;
    @Inject
    private Table<Contact> contactsTable;
    @Inject
    private ScreenBuilders screenBuilders;
    @Named("contactsTable.refresh")
    private RefreshAction contactsTableRefresh;
    @Inject
    private Button contactsTableCreateBtn;

    public void setDisableContractCreateButton() {
        contactsTableCreateBtn.setEnabled(false);
    }

//    @Subscribe(id = "contactsDc", target = Target.DATA_CONTAINER)
//    public void onContactsDcCollectionChange(CollectionContainer.CollectionChangeEvent<Contact> event) {
//        List<? extends Contact> list = event.getSource().getItems()
//                .stream()
//                .filter(acc -> acc.getAccount().equals(getEditedEntity()))
//                .collect(Collectors.toList());
//        event.getSource().setItems((Collection<Contact>) list);
//    }

    @Install(to = "contactsDl", target = Target.DATA_LOADER)
    private List<Contact> contactsDlLoadDelegate(LoadContext<Contact> loadContext) {
        Account account = getEditedEntity();
        loadContext.setQueryString("SELECT c FROM uco_Account a" +
                " LEFT JOIN a.contacts c" +
                " where a=:account");
        loadContext.getQuery().setParameter("account", account);
        return dataManager.loadList(loadContext);
    }

//    @Install(to = "contactsTable.create", subject = "newEntitySupplier")
//    private Contact contactsTableCreateNewEntitySupplier() {
//        Contact contact = dataManager.create(Contact.class);
//        contact.setAccount(getEditedEntity());
//        return contact;
//    }

    @Subscribe("contactsTable.create")
    public void onContactsTableCreate(Action.ActionPerformedEvent event) {
        ContactEdit editScreen = screenBuilders.editor(contactsTable)
                .newEntity()
                .withScreenClass(ContactEdit.class)
                .withLaunchMode(OpenMode.DIALOG)
                .build();
        editScreen.getEditedEntity().setAccount(getEditedEntity());
        editScreen.getWindow().getDialogOptions().setWidthAuto();
        editScreen.getWindow().getDialogOptions().setHeightAuto();
        Component accountField = editScreen.getWindow().getComponents().stream()
                .filter(q -> q.getId().equals("accountField")).findFirst().get();
        accountField.setEnabled(false);
        editScreen.show();
    }

    @Subscribe("contactsTable.edit")
    public void onContactsTableEdit(Action.ActionPerformedEvent event) {
        ContactEdit editScreen = screenBuilders.editor(contactsTable)
                .newEntity()
                .withScreenClass(ContactEdit.class)
                .withLaunchMode(OpenMode.DIALOG)
                .build();
        editScreen.setEntityToEdit(contact);
        editScreen.getEditedEntity().setAccount(getEditedEntity());
        editScreen.getWindow().getDialogOptions().setWidthAuto();
        editScreen.getWindow().getDialogOptions().setHeightAuto();
        Component accountField = editScreen.getWindow().getComponents().stream()
                .filter(q -> q.getId().equals("accountField")).findFirst().get();
        accountField.setEnabled(true);
        editScreen.addAfterCloseListener(new Consumer<AfterCloseEvent>() {
            @Override
            public void accept(AfterCloseEvent afterCloseEvent) {
                contactsTableRefresh.execute();
            }
        });

        editScreen.show();
    }

    private Contact contact;

    @Subscribe("contactsTable")
    public void onContactsTableSelection(Table.SelectionEvent<Contact> event) {
        if (event.getSelected().iterator().hasNext()) {
            contact = event.getSelected().iterator().next();
        } else {
            contact = null;
        }
    }

//    @Subscribe("contactsTableCreateBtn")
//    public void onContactsTableCreateBtnClick(Button.ClickEvent event) {
//        contactEditDialog.show();
//    }
}