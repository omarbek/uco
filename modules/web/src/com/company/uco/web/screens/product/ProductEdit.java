package com.company.uco.web.screens.product;

import com.haulmont.cuba.gui.screen.*;
import com.company.uco.entity.Product;

@UiController("uco_Product.edit")
@UiDescriptor("product-edit.xml")
@EditedEntityContainer("productDc")
@LoadDataBeforeShow
public class ProductEdit extends StandardEditor<Product> {
}