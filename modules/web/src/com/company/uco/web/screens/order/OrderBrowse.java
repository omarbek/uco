package com.company.uco.web.screens.order;

import com.company.uco.entity.Account;
import com.company.uco.entity.Order;
import com.company.uco.web.screens.contact.ContactEdit;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.List;

@UiController("uco_Order.browse")
@UiDescriptor("order-browse.xml")
@LookupComponent("ordersTable")
@LoadDataBeforeShow
public class OrderBrowse extends StandardLookup<Order> {
    @Inject
    private DataManager dataManager;

    private Account account;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private GroupTable<Order> ordersTable;

    public void setAccount(Account account) {
        this.account = account;
    }

    @Install(to = "ordersDl", target = Target.DATA_LOADER)
    private List<Order> ordersDlLoadDelegate(LoadContext<Order> loadContext) {
        if (account != null) {
            loadContext.setQueryString("SELECT o FROM uco_Account a" +
                    " LEFT JOIN a.orders o" +
                    " where a=:account");
            loadContext.getQuery().setParameter("account", account);
        } else {
            loadContext.setQueryString("SELECT o FROM uco_Account a" +
                    " LEFT JOIN a.orders o");
        }
        return dataManager.loadList(loadContext);
    }

    @Subscribe("ordersTable.create")
    public void onOrdersTableCreate(Action.ActionPerformedEvent event) {
        OrderEdit editScreen = screenBuilders.editor(ordersTable)
                .newEntity()
                .withScreenClass(OrderEdit.class)
                .withLaunchMode(OpenMode.NEW_TAB)
                .build();
        editScreen.getEditedEntity().setAccount(account);
        editScreen.getWindow().getComponent("accountField").setEnabled(false);
        editScreen.setDisableProductCreateButton();
        editScreen.show();
    }

}