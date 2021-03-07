package com.company.uco.web.screens.order;

import com.company.uco.entity.Account;
import com.company.uco.entity.Order;
import com.company.uco.entity.Product;
import com.company.uco.web.screens.product.ProductEdit;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.FluentLoader;
import com.haulmont.cuba.core.global.FluentValueLoader;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.RemoveOperation;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.actions.list.RefreshAction;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@UiController("uco_Order.browse")
@UiDescriptor("order-browse.xml")
@LookupComponent("ordersTable")
@LoadDataBeforeShow
public class OrderBrowse extends StandardLookup<Order> {

    private Account account;
    private Order order;
    private Product product;

    @Inject
    private DataManager dataManager;
    @Named("productsTable.refresh")
    private RefreshAction productsTableRefresh;
    @Named("ordersTable.refresh")
    private RefreshAction ordersTableRefresh;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private Table<Product> productsTable;
    @Inject
    private Button productsTableCreateBtn;

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
        Order order = dataManager.create(Order.class);
        order.setAmount(0.0);
        order.setOrderDate(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        order.setAccount(account);
        dataManager.commit(order);
        ordersTableRefresh.execute();
    }

    @Install(to = "productsDl", target = Target.DATA_LOADER)
    private List<Product> productsDlLoadDelegate(LoadContext<Product> loadContext) {
        loadContext.setQueryString("SELECT p FROM uco_Order o" +
                " LEFT JOIN o.products p" +
                " where o=:order");
        loadContext.getQuery().setParameter("order", order);
        return dataManager.loadList(loadContext);
    }

    @Subscribe("ordersTable")
    public void onOrdersTableSelection(Table.SelectionEvent<Order> event) {
        if (event.getSelected().iterator().hasNext()) {
            order = event.getSelected().iterator().next();
        } else {
            order = null;
        }
        productsTableRefresh.execute();
        productsTableCreateBtn.setEnabled(true);
    }

    @Subscribe("productsTable.create")
    public void onProductsTableCreate(Action.ActionPerformedEvent event) {
        ProductEdit editScreen = screenBuilders.editor(productsTable)
                .newEntity()
                .withScreenClass(ProductEdit.class)
                .withLaunchMode(OpenMode.DIALOG)
                .build();
        editScreen.getEditedEntity().setOrder(order);
        editScreen.getWindow().getDialogOptions().setWidthAuto();
        editScreen.getWindow().getDialogOptions().setHeightAuto();
        editScreen.addAfterCloseListener(new Consumer<AfterCloseEvent>() {
            @Override
            public void accept(AfterCloseEvent afterCloseEvent) {
                FluentValueLoader<Product> products = dataManager.loadValue("select p from uco_Product p" +
                        " where p.order = :order", Product.class).parameter("order", OrderBrowse.this.order);
                List<Product> list = products.list();
                Double amount = 0.0;
                for (Product p : list) {
                    amount += (p.getPrice() * p.getQuantity());
                }
                order.setAmount(amount);
                dataManager.commit(order);
                FluentLoader.ById<Order, UUID> orderUUIDById = dataManager.load(Order.class).id(order.getId());
                order = orderUUIDById.one();
                ordersTableRefresh.execute();
            }
        });
        editScreen.show();
    }

    @Install(to = "ordersTable.remove", subject = "afterActionPerformedHandler")
    private void ordersTableRemoveAfterActionPerformedHandler(RemoveOperation.AfterActionPerformedEvent<Order> afterActionPerformedEvent) {
        productsTableCreateBtn.setEnabled(false);
    }

    @Install(to = "productsTable.remove", subject = "afterActionPerformedHandler")
    private void productsTableRemoveAfterActionPerformedHandler(RemoveOperation.AfterActionPerformedEvent<Product> afterActionPerformedEvent) {
        FluentValueLoader<Product> products = dataManager.loadValue("select p from uco_Product p" +
                " where p.order = :order", Product.class).parameter("order", OrderBrowse.this.order);
        List<Product> list = products.list();
        Double amount = 0.0;
        for (Product p : list) {
            amount += (p.getPrice() * p.getQuantity());
        }
        order.setAmount(amount);
        dataManager.commit(order);
        FluentLoader.ById<Order, UUID> orderUUIDById = dataManager.load(Order.class).id(order.getId());
        order = orderUUIDById.one();
        ordersTableRefresh.execute();
    }

    @Subscribe("productsTable.edit")
    public void onProductsTableEdit(Action.ActionPerformedEvent event) {
        ProductEdit editScreen = screenBuilders.editor(productsTable)
                .newEntity()
                .withScreenClass(ProductEdit.class)
                .withLaunchMode(OpenMode.DIALOG)
                .build();
        editScreen.setEntityToEdit(product);
        editScreen.getWindow().getDialogOptions().setWidthAuto();
        editScreen.getWindow().getDialogOptions().setHeightAuto();
        editScreen.addAfterCloseListener(new Consumer<AfterCloseEvent>() {
            @Override
            public void accept(AfterCloseEvent afterCloseEvent) {
                FluentValueLoader<Product> products = dataManager.loadValue("select p from uco_Product p" +
                        " where p.order = :order", Product.class).parameter("order", OrderBrowse.this.order);
                List<Product> list = products.list();
                Double amount = 0.0;
                for (Product p : list) {
                    amount += (p.getPrice() * p.getQuantity());
                }
                order.setAmount(amount);
                dataManager.commit(order);
                FluentLoader.ById<Order, UUID> orderUUIDById = dataManager.load(Order.class).id(order.getId());
                order = orderUUIDById.one();
                ordersTableRefresh.execute();
                productsTableRefresh.execute();
            }
        });
        editScreen.show();
    }

    @Subscribe("productsTable")
    public void onProductsTableSelection(Table.SelectionEvent<Product> event) {
        if (event.getSelected().iterator().hasNext()) {
            product = event.getSelected().iterator().next();
        } else {
            product = null;
        }
    }

    @Install(to = "ordersTable.edit", subject = "afterCloseHandler")
    private void ordersTableEditAfterCloseHandler(AfterCloseEvent afterCloseEvent) {
        ordersTableRefresh.execute();
    }
}