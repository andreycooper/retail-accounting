package by.cooper.android.retailaccounting.dagger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * This annotation needs in PhonesComponent for another scope from LoginComponent
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface AppScope {
}
