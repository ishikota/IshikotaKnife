package jp.ishikotaknife;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Define my annotation here.
// Abstract method value() represents passed value of annotation.
// You would use this method in compile time
// to fetch the value associated with IshikotaBind annotation.

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface IshikotaBind {
    int value();
}
