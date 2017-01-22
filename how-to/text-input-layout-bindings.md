## TextInputLayout Bindings ##

You can use this binding class on instances of `android.support.design.widget.TextInputLayout`.

### Validator ###
Adds validator property on TextInputLayout context.

in view.xml

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:bindings="http://schemas.android.com/apk/res-auto">
      
  <data>
    <variable name="viewModel" 
              type="org.fs.view.ViewModel" />
  </data>
  
  <android.support.design.widget.TextInputLayout
      android:layout_width="match_parent"
      android:layout_height="wrap_content">

    <android.support.design.widget.TextInputEditText
        android:inputType="text"
        android:imeOptions="actionDone"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" 
        bindings:validator="@{viewModel.validator}"
        bindings:errorString="@{@string/error}"/>

  </android.support.design.widget.TextInputLayout>
      
</layout>        
```

in Validator.java

```java
public class Validator implements IValidator<String> {
  
  @Override public Validation validate(String str, Locale locale) {
    return Validation.VALID;
  }  
}
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  public final Validator validator = new Validator();
  
  public ViewModel(ActivityView view) {
    super(view);
  }
  
}
```