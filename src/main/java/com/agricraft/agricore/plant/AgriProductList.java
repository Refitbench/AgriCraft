/*
 */
package com.agricraft.agricore.plant;

import com.agricraft.agricore.core.AgriCore;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author RlonRyan
 */
public class AgriProductList {

    private final List<AgriProduct> products;

    public AgriProductList() {
        this.products = new ArrayList<>();
    }

    public AgriProductList(List<AgriProduct> products) {
        this.products = products;
    }

    public final List<AgriProduct> getAll() {
        return new ArrayList<>(this.products);
    }

    public final List<AgriProduct> getRandom(Random rand) {
        final List<AgriProduct> produce = new ArrayList<>();
        products.forEach((product) -> {
            if (product.shouldDrop(rand)) {
                produce.add(product);
            }
        });
        return produce;
    }

    public boolean validate() {
        Iterator<AgriProduct> iter = this.products.iterator();
        while (iter.hasNext()) {
            AgriProduct product = iter.next();
            if (!product.validate()) {
                if (product.isRequired()) {
                    AgriCore.getCoreLogger().info("Invalid List: Invalid Required Product!");
                    return false;
                } else {
                    AgriCore.getCoreLogger().info("Product List: Removing Invalid Non-Required Product!");
                    iter.remove();
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("\nProduct List:\n");
        sb.append("\t- Products: ");
        this.products.forEach((e) -> {
            sb.append("\n\t\t- ").append(e.toString().replaceAll("\n", "\n\t\t").trim());
        });
        return sb.toString();
    }

}
