package com.company.uco.web.screens.account;

import com.company.uco.entity.Account;
import com.company.uco.entity.Order;
import com.company.uco.web.screens.order.OrderBrowse;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

@UiController("uco_Account.browse")
@UiDescriptor("account-browse.xml")
@LookupComponent("accountsTable")
@LoadDataBeforeShow
public class AccountBrowse extends StandardLookup<Account> {
    @Inject
    private Button ordersBtn;
    @Inject
    private GroupTable<Account> accountsTable;
    @Inject
    protected UiComponents uiComponents;
    @Inject
    private DataManager dataManager;

    private Account account;
    private LoadContext<Order> loadContext;
    @Inject
    private LookupScreenFacet<Order, OrderBrowse> orderViewDialog;
    @Inject
    private ScreenBuilders screenBuilders;
    @Named("accountsTable.orders")
    private BaseAction accountsTableOrders;
    @Inject
    private Table<Order> ordersTable;

    private List<Order> list;

    @Subscribe("accountsTable.orders")
    public void onAccountsTableOrders(Action.ActionPerformedEvent event) {
        OrderBrowse editScreen = screenBuilders.lookup(ordersTable)
//                .newEntity()
                .withScreenClass(OrderBrowse.class)
//                .withLaunchMode(OpenMode.NEW_TAB)
                .build();
        editScreen.setAccount(account);
//        ordersTable.getItems().getItems();
        editScreen.show();
    }

    @Subscribe("accountsTable")
    public void onAccountsTableSelection(Table.SelectionEvent<Account> event) {
        if (event.getSelected().iterator().hasNext()) {
            account = event.getSelected().iterator().next();
            ordersBtn.setEnabled(true);
        } else {
            account = null;
            ordersBtn.setEnabled(false);
        }
    }

//    @Subscribe("ordersBtn")
//    public void onOrdersBtnClick(Button.ClickEvent event) {
//        orderViewDialog.getContainer().setItems(new ArrayList<>());
//        orderViewDialog.show();
//    }

//    @Install(to = "ordersDl", target = Target.DATA_LOADER)
//    private List<Order> ordersDlLoadDelegate(LoadContext<Order> loadContext) {
//        if (loadContext != null) {
//            this.loadContext = loadContext;
//        }
//        if (account == null || loadContext == null) {
//            return new ArrayList<>();
//        }
//        loadContext.setQueryString("SELECT o FROM uco_Account a" +
//                " LEFT JOIN a.orders o" +
//                " where a=:account");
//        loadContext.getQuery().setParameter("account", account);
//        loadContext.setQueryString("select e from uco_Order e where e.amount=1");
//        return dataManager.loadList(loadContext);
//    }

    @Subscribe
    public void onInit(InitEvent event) {
        accountsTable.addGeneratedColumn("photo", this::renderAvatarImageComponent);
    }

    private Component renderAvatarImageComponent(Account account) {
        FileDescriptor imageFile = account.getPhoto();
        if (imageFile == null) {
            return null;
        }
        Image image = smallAvatarImage();
        image.setSource(FileDescriptorResource.class)
                .setFileDescriptor(imageFile);

        return image;
    }

    private Image smallAvatarImage() {
        Image image = uiComponents.create(Image.class);
        image.setScaleMode(Image.ScaleMode.CONTAIN);
        image.setHeight("40");
        image.setWidth("40");
        image.setStyleName("avatar-icon-small");
        return image;
    }

}