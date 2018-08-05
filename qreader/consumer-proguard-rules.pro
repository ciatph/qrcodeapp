# Consumer Proguard Rules

-keep public class github.nisrulz.** {
  public protected *;
}

-keep public class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**