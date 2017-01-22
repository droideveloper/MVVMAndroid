## TextView Bindings ##

You can use this binding class on instances of `android.widget.TextView`.

1. Converter
2. Listeners
  * [SoftKeyboardListener](https://github.com/droideveloper/MVVMAndroid/blob/master/how-to/text-view-bindings.md#softkeyboardlistener)
  * [BeforeTextChangeListener](https://github.com/droideveloper/MVVMAndroid/blob/master/how-to/text-view-bindings.md#beforetextchangelistener)
  * [AfterTextChangedListener](https://github.com/droideveloper/MVVMAndroid/blob/master/how-to/text-view-bindings.md#aftertextchangedlistener)
  
### Converter ###
Converts input object into required type by given Converter. After successful conversion it will be set on text.

in view.xml

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:bindings="http://schemas.android.com/apk/res-auto">
      
  <data>
    <variable name="viewModel" 
              type="org.fs.view.ViewModel" />
  </data>
  
  <LinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical">  
    
  
    <TextView
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      bindings:converter="@{viewModel.textConverter}" 
      bindings:fromObject="@{viewModel.textObjectParameter}"/>
      
  </LinearLayout>
      
</layout>        
```

in Converter.java

```java
public class Converter implements IConverter<Object, String> {
  
  @Override public String convert(Object object, Locale locale) {
    return object.toString(locale);
  }  
}
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  public final Converter textConverter = new Converter();
  private Object textObjectParameter;
  
  public ViewModel(ActivityView view) {
    super(view);
  }
  
  @Bindable public Object getTextParameter() {
    //here the text passed to command execution
    return this.textParameter;
  }  
}
```

### Listeners ###
Binds proper Listener on TextView instance.

#### SoftKeyboardListener ####

in view.xml

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:bindings="http://schemas.android.com/apk/res-auto">
      
  <data>
    <variable name="viewModel" 
              type="org.fs.view.ViewModel" />
  </data>
  
  <LinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical">    
  
    <TextView
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      bindings:onSoftKeyboardAction="@{viewModel.keyboardActionListener}"/>
      
  </LinearLayout>
      
</layout>        
```

in SoftKeyboardListener.java

```java
public class SoftKeyboardListener implements OnSoftKeyboardAction {
  
  @Override public boolean onEditorAction(int actionId) {
    return true; //if actionId is handled return true else otherwise.
  }  
}
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  public final SoftKeyboardListener keyboardActionListener = new SoftKeyboardListener();
  
  public ViewModel(ActivityView view) {
    super(view);
  }
}
```

#### BeforeTextChangeListener ####

in view.xml

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:bindings="http://schemas.android.com/apk/res-auto">
      
  <data>
    <variable name="viewModel" 
              type="org.fs.view.ViewModel" />
  </data>
  
  <LinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical">    
  
    <TextView
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      bindings:beforeChanged="@{viewModel.beforeTextChangeListener}"/>
      
  </LinearLayout>
      
</layout>        
```

in BeforeChangeListener.java

```java
public class BeforeChangeListener implements OnBeforeChanged {
  
  @Override public void beforeChanged(CharSequence charSequence, int start, int count, int after) {
    
  }  
}
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  public final BeforeChangeListener beforeTextChangeListener = new BeforeChangeListener();
  
  public ViewModel(ActivityView view) {
    super(view);
  }
}
```

#### AfterTextChangedListener ####

in view.xml

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:bindings="http://schemas.android.com/apk/res-auto">
      
  <data>
    <variable name="viewModel" 
              type="org.fs.view.ViewModel" />
  </data>
  
  <LinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical">    
  
    <TextView
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      bindings:afterChanged="@{viewModel.afterTextChangedListener}"/>
      
  </LinearLayout>
      
</layout>        
```

in AfterChangedListener.java

```java
public class AfterChangedListener implements OnAfterChanged {
  
  @Override public void afterChanged(Editable editable) {
    
  }  
}
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  public final AfterChangedListener afterTextChangedListener = new AfterChangedListener();
  
  public ViewModel(ActivityView view) {
    super(view);
  }
}
```