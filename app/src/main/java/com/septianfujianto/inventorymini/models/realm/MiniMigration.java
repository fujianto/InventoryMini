package com.septianfujianto.inventorymini.models.realm;

import com.septianfujianto.inventorymini.App;
import com.septianfujianto.inventorymini.R;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Created by Septian A. Fujianto on 1/30/2017.
 */

public class MiniMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();


        if (oldVersion == 0) {
            RealmObjectSchema productSchema = schema.get("Product");

            productSchema.addField("product_image", String.class)
                    .transform(new RealmObjectSchema.Function() {
                        @Override
                        public void apply(DynamicRealmObject obj) {
                            obj.setByte("product_image", Byte.valueOf(obj.getString("product_image")));
                        }
                    });

            oldVersion++;
        }

        if (oldVersion == 1) {
            schema.create("Location")
                    .addPrimaryKey("location_id")
                    .addField("location_id", int.class)
                    .addField("location_name", String.class);

            schema.get("Product")
                    .addField("location_id", int.class);

            oldVersion++;
        }

        if (oldVersion == 2) {
            RealmObjectSchema productSchema = schema.get("Product");
            productSchema.addField("product_brand", String.class)
                    .transform(new RealmObjectSchema.Function() {
                        @Override
                        public void apply(DynamicRealmObject obj) {
                            obj.set("product_brand", App.getContext().getString(R.string.other_location));
                        }
                    })
            .addField("product_qty_label", String.class)
                    .transform(new RealmObjectSchema.Function() {
                        @Override
                        public void apply(DynamicRealmObject obj) {
                            obj.set("product_qty_label", "pcs");
                        }
                    })
            .addField("product_weight", int.class)
                    .transform(new RealmObjectSchema.Function() {
                        @Override
                        public void apply(DynamicRealmObject obj) {
                            obj.set("product_weight", 1);
                        }
                    })
            .addField("product_weight_label", String.class)
                    .transform(new RealmObjectSchema.Function() {
                        @Override
                        public void apply(DynamicRealmObject obj) {
                            obj.set("product_weight_label", "gr");
                        }
                    })
            .addField("product_qty_watch", int.class)
                    .transform(new RealmObjectSchema.Function() {
                        @Override
                        public void apply(DynamicRealmObject obj) {
                            obj.set("product_qty_watch", 1);
                        }
                    });

            oldVersion++;
        }
    }
}
