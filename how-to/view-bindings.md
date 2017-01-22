## View Bindings ##

You can use this binding class on instances of `android.view.View`.

1. Commands
..1. [Parameterized Commands](https://github.com/droideveloper/MVVMAndroid/blob/master/how-to/view-bindings.md#parameterized-commands)
..2. [Non-Parameterized Commands](https://github.com/droideveloper/MVVMAndroid/blob/master/how-to/view-bindings.md#non-parameterized-commands)
2. Notify
..1. [With-Action](https://github.com/droideveloper/MVVMAndroid/blob/master/how-to/view-bindings.md#with-action)
..2. [Without-Action](https://github.com/droideveloper/MVVMAndroid/blob/master/how-to/view-bindings.md#without-action)


### Commands ###
Binding a command on view is equal to adding `view.setOnClickListener()`.

#### Parameterized Commands #####

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
      
        <Button
          android:layout_width="wrap_content"
          android:layout_height="wrap_content"
          android:text="@string/app_name"
          style="@style/Widget.AppCompat.Button.Borderless.Colored" 
          bindings:command="@{viewModel.textCommand}" 
          bindings:commandParameter="@{viewModel.textParameter}"/>
          
      </LinearLayout>
      
</layout>        
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  public final ParameterizedCommand<String> textCommand = new ParameterizedCommand<>(ViewModel.this::textCommandExecute);
  private String textParameter;
  
  public ViewModel(ActivityView view) {
    super(view);
  }
  
  @Bindable public String getTextParameter() {
    //here the text passed to command execution
    return this.textParameter;
  }
  
  public void textCommandExecute(String text) {
    //here the text you get.
  }
}
```

#### Non-Parameterized Commands ####

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
      
        <Button
          android:layout_width="wrap_content"
          android:layout_height="wrap_content"
          android:text="@string/app_name"
          style="@style/Widget.AppCompat.Button.Borderless.Colored" 
          bindings:command="@{viewModel.callbackCommand}" />
          
      </LinearLayout>
      
</layout>        
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  public final RelayCommand callbackCommand = new RelayCommand(ViewModel.this::callbackCommandExecute);
  private String textParameter;
  
  public ViewModel(ActivityView view) {
    super(view);
  }
  
  public void callbackCommandExecute() {
    
  }
}
```

### Notify ###
Binding notification text is equal to `Snackbar.show()`. 
Advised to use on top-level layout because Snackbar registered on the view bind.

#### With-Action ####

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
        android:layout_height="match_parent"
        android:orientation="vertical" 
        bindings:notifyText="@{viewModel.textNotification}"
        bindings:actionText="@{@string/actionOk}"
        bindings:relayCommand="@{viewModel.callbackCommand}" />
      
</layout>        
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  public final RelayCommand callbackCommand = new RelayCommand(ViewModel.this::callbackCommandExecute);
  private String textNotification;
  
  public ViewModel(ActivityView view) {
    super(view);
  }
  
  @Bindable public String getTextNotification() {
    return this.textNotification;
  }
  
  public void setTextNotification(String textNotification) {
    this.textNotification = textNotification;
    notifyPropertyChanged(BR.textNotification);
  }
  
  public void callbackCommandExecute() {
    //handle callback
  }
}
```

#### Without-Action ####

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
        android:layout_height="match_parent"
        android:orientation="vertical" 
        bindings:notifyText="@{viewModel.textNotification}" />
      
</layout>        
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  private String textNotification;
  
  public ViewModel(ActivityView view) {
    super(view);
  }
  
  @Bindable public String getTextNotification() {
    return this.textNotification;
  }
  
  public void setTextNotification(String textNotification) {
    this.textNotification = textNotification;
    notifyPropertyChanged(BR.textNotification);
  }
 
}
```