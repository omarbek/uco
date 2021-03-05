package com.company.uco.web.screens.product;

import com.haulmont.cuba.gui.screen.*;
import com.company.uco.entity.Product;

@UiController("uco_Product.browse")
@UiDescriptor("product-browse.xml")
@LookupComponent("productsTable")
@LoadDataBeforeShow
public class ProductBrowse extends StandardLookup<Product> {
}