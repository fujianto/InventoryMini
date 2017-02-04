package com.septianfujianto.inventorymini.models.realm;

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
    }
}
