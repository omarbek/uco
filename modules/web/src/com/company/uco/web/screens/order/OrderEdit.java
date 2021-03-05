package com.company.uco.web.screens.order;

import com.company.uco.entity.Account;
import com.company.uco.entity.Contact;
import com.company.uco.entity.Product;
import com.company.uco.web.screens.contact.ContactEdit;
import com.company.uco.web.screens.product.ProductEdit;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.actions.list.RefreshAction;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.screen.*;
import com.company.uco.entity.Order;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
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
    @Inject
    private Button productsTableRefreshBtn;
    @Named("productsTable.refresh")
    private RefreshAction productsTableRefresh;

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
        editScreen.getEditedEntity().setOrder(getEditedEntity());
        editScreen.getWindow().getDialogOptions().setWidthAuto();
        editScreen.getWindow().getDialogOptions().setHeightAuto();
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
        editScreen.getEditedEntity().setOrder(getEditedEntity());
        editScreen.getWindow().getDialogOptions().setWidthAuto();
        editScreen.getWindow().getDialogOptions().setHeightAuto();
        editScreen.addAfterCloseListener(new Consumer<AfterCloseEvent>() {
            @Override
            public void accept(AfterCloseEvent afterCloseEvent) {
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


}