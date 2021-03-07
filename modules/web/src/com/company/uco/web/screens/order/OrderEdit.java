package com.company.uco.web.screens.order;

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
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.util.OperationResult;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@UiController("uco_Order.edit")
@UiDescriptor("order-edit.xml")
@EditedEntityContainer("orderDc")
@LoadDataBeforeShow
public class OrderEdit extends StandardEditor<Order> {
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private Table<Product> productsTable;
    @Inject
    private Button productsTableCreateBtn;
    @Inject
    private DataManager dataManager;
    @Named("productsTable.refresh")
    private RefreshAction productsTableRefresh;

    private Order order;
    @Inject
    private TextField<Double> amountField;

    public void setDisableProductCreateButton() {
        productsTableCreateBtn.setEnabled(false);
    }

    @Subscribe
    public void onInitEntity(InitEntityEvent<Order> event) {
        event.getEntity().setOrderDate(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
    }

    @Subscribe("productsTable.create")
    public void onProductsTableCreate(Action.ActionPerformedEvent event) {
        ProductEdit editScreen = screenBuilders.editor(productsTable)
                .newEntity()
                .withScreenClass(ProductEdit.class)
                .withLaunchMode(OpenMode.DIALOG)
                .build();
        order = getEditedEntity();
        editScreen.getEditedEntity().setOrder(order);
        editScreen.getWindow().getDialogOptions().setWidthAuto();
        editScreen.getWindow().getDialogOptions().setHeightAuto();
        editScreen.addAfterCloseListener(new Consumer<AfterCloseEvent>() {
            @Override
            public void accept(AfterCloseEvent afterCloseEvent) {
                FluentValueLoader<Product> products = dataManager.loadValue("select p from uco_Product p" +
                        " where p.order = :order", Product.class).parameter("order", order);
                List<Product> list = products.list();
                Double amount = 0.0;
                for (Product p : list) {
                    amount += (p.getPrice() * p.getQuantity());
                }
                order.setAmount(amount);
                dataManager.commit(order);
                amountField.setValue(amount);
                FluentLoader.ById<Order, UUID> orderUUIDById = dataManager.load(Order.class).id(order.getId());
                order = orderUUIDById.one();
            }
        });
        editScreen.show();
    }

    @Subscribe("productsTable.edit")
    public void onProductsTableEdit(Action.ActionPerformedEvent event) {
        ProductEdit editScreen = screenBuilders.editor(productsTable)
                .newEntity()
                .withScreenClass(ProductEdit.class)
                .withLaunchMode(OpenMode.DIALOG)
                .build();
        editScreen.setEntityToEdit(product);
        order = getEditedEntity();
        editScreen.getWindow().getDialogOptions().setWidthAuto();
        editScreen.getWindow().getDialogOptions().setHeightAuto();
        editScreen.addAfterCloseListener(new Consumer<AfterCloseEvent>() {
            @Override
            public void accept(AfterCloseEvent afterCloseEvent) {
                FluentValueLoader<Product> products = dataManager.loadValue("select p from uco_Product p" +
                        " where p.order = :order", Product.class).parameter("order", order);
                List<Product> list = products.list();
                Double amount = 0.0;
                for (Product p : list) {
                    amount += (p.getPrice() * p.getQuantity());
                }
                order.setAmount(amount);
                dataManager.commit(order);
                amountField.setValue(amount);
                FluentLoader.ById<Order, UUID> orderUUIDById = dataManager.load(Order.class).id(order.getId());
                order = orderUUIDById.one();
                productsTableRefresh.execute();
            }
        });
        editScreen.show();
    }

    @Install(to = "productsDl", target = Target.DATA_LOADER)
    private List<Product> productsDlLoadDelegate(LoadContext<Product> loadContext) {
        Order order = getEditedEntity();
        loadContext.setQueryString("SELECT p FROM uco_Order o" +
                " LEFT JOIN o.products p" +
                " where o=:order");
        loadContext.getQuery().setParameter("order", order);
        return dataManager.loadList(loadContext);
    }

    private Product product;

    @Subscribe("productsTable")
    public void onProductsTableSelection(Table.SelectionEvent<Product> event) {
        if (event.getSelected().iterator().hasNext()) {
            product = event.getSelected().iterator().next();
        } else {
            product = null;
        }
    }

    @Override
    protected void commitAndClose(Action.ActionPerformedEvent event) {
        closeWithDiscard();
    }

    @Install(to = "productsTable.remove", subject = "afterActionPerformedHandler")
    private void productsTableRemoveAfterActionPerformedHandler(RemoveOperation.AfterActionPerformedEvent<Product> afterActionPerformedEvent) {
        FluentValueLoader<Product> products = dataManager.loadValue("select p from uco_Product p" +
                " where p.order = :order", Product.class).parameter("order", order);
        List<Product> list = products.list();
        Double amount = 0.0;
        for (Product p : list) {
            amount += (p.getPrice() * p.getQuantity());
        }
        order.setAmount(amount);
        dataManager.commit(order);
        amountField.setValue(amount);
        FluentLoader.ById<Order, UUID> orderUUIDById = dataManager.load(Order.class).id(order.getId());
        order = orderUUIDById.one();
    }

//    @Override
//    protected OperationResult commitChanges() {
//        disableCommitActions();//here 3
//        return super.commitChanges();
//    }
//
//    @Override
//    public OperationResult closeWithCommit() {
//        disableCommitActions();//here 2
//        return super.closeWithCommit();
//    }

    @Override
    protected void cancel(Action.ActionPerformedEvent event) {
        closeWithDiscard();
    }
}