## Toolbar Bindings ##

You can use this binding class on instances of `android.support.v7.widget.Toolbar`.

### TitleText ###

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
    android:orientation="vertical">

    <android.support.v7.widget.Toolbar 
      android:layout_width="match_parent"
      android:layout_height="70dp"
      bindings:titleText="@={viewModel.titleText}" />
    
  </LinearLayout>

</layout>
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {
  
  private String titleText;
  
  public ViewModel(ActivityView view) {
    super(view);
  }
  
  @Bindable public String getTitleText() {
    return this.titleText;
  }
  
  public void setTitleText(String titleText) {
    this.titleText = titleText;
    notifyPropertyChanged(BR.titleText);
  }  
}
```

### SubTitleText ###

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
    android:orientation="vertical">

    <android.support.v7.widget.Toolbar 
      android:layout_width="match_parent"
      android:layout_height="70dp"
      bindings:subTitleText="@={viewModel.subTitleText}" />
    
  </LinearLayout>

</layout>
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {
  
  private String subTitleText;
  
  public ViewModel(ActivityView view) {
    super(view);
  }
  
  @Bindable public String getSubTitleText() {
    return this.titleText;
  }
  
  public void setSubTitleText(String subTitleText) {
    this.subTitleText = subTitleText;
    notifyPropertyChanged(BR.subTitleText);
  }  
}
```

### Navigation ###

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
    android:orientation="vertical">

    <android.support.v7.widget.Toolbar 
      android:layout_width="match_parent"
      android:layout_height="70dp"
      bindings:navigationIconCompat="@{@drawable/ic_arrow_back}"
      bindings:navigationCommand="@{viewModel.navigationCommand}"
      bindings:navigationCommandParameter="@{viewModel.navigationCommandParameter}"
      bindings:onNavigated="@{viewModel.navigatedCallback}" />
    
  </LinearLayout>

</layout>
```

in NavigatedListener.java

```java
public class NavigatedListener implements OnNavigated {
  
  @Override public void onNavigated(View view) {
    //handle navigated callback
  }
}

```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {
  
  public final ParameterizedCommand<String> navigationCommand = new ParameterizedCommand<>(ViewModel.this::commandExecutedCallback);
  public final NavigatedListener navigatedCallback = new NavigatedListener();
  private String navigationCommandParameter;  
  
  public ViewModel(ActivityView view) {
    super(view);
  } 
  
  @Bindable public String getNavigationCommandParameter() {
    return this.navigationCommandParameter;
  }
  
  public void commandExecutedCallback(String str) {
    //command executed
  }
}
```

### Menu ###

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
    android:orientation="vertical">

    <android.support.v7.widget.Toolbar 
      android:layout_width="match_parent"
      android:layout_height="70dp"
      bindings:toolbarMenu="@{@menu/toolbar_menu}"
      bindings:toolbarMenuCallback="@{viewModel.toolbarMenuCallback}"/>
    
  </LinearLayout>

</layout>
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {
    
  public ViewModel(ActivityView view) {
    super(view);
  }
  
  @Bindable public Toolbar.OnMenuItemClickListener getToolbarMenuCallback() {
    return new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        return false;
      }
    };
  }   
}
```