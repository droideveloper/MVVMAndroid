## NavigationView Bindings ##

You can use this binding class on instances of `android.support.design.widget.NavigationView`.

1. [Property](https://github.com/droideveloper/MVVMAndroid/blob/master/how-to/navigation-view-bindings.md#property)
2. [Listener](https://github.com/droideveloper/MVVMAndroid/blob/master/how-to/navigation-view-bindings.md#listener)

### Property ###

Properties we can bind on NavigationView instance.

#### MenuItem ####

in view.xml

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:bindings="http://schemas.android.com/apk/res-auto">
      
  <data>
    <variable name="viewModel" 
              type="org.fs.view.ViewModel" />
  </data>
    
  <android.support.design.widget.NavigationView
      xmlns:android="http://schemas.android.com/apk/res/android"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      bindings:menuItem="@={viewModel.selectedMenuItem}" />
        
</layout>        
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  private int selectedMenuItem;
  
  public ViewModel(ActivityView view) {
    super(view);
  }
  
  @Bindable public int getSelectedMenuItem() {
    return this.selectedMenuItem;
  }
  
  public void setSelectedMenuItem(int selectedMenuItem) {
    this.selectedMenuItem = selectedMenuItem;
    notifyPropertyChanged(BR.selectedMenuItem);
  }
}
```

### Listener ###

Listener we can bind on NavigationView instance.

#### NavigationSelectedListener ####

in view.xml

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:bindings="http://schemas.android.com/apk/res-auto">
      
  <data>
    <variable name="viewModel" 
              type="org.fs.view.ViewModel" />
  </data>
    
  <android.support.design.widget.NavigationView
      xmlns:android="http://schemas.android.com/apk/res/android"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      bindings:onNavigationSelected="@{viewModel.navigationSelectedCallback}" />
        
</layout>        
```

in NavigationSelectedListener.java

```java
public class NavigationSelectedListener implements OnNavigationSelected {
  @Override public boolean onNavigationSelected(MenuItem item) {
    return false;
  }
}
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  public final NavigationSelectedListener navigationSelectedCallback = new NavigationSelectedListener();
  
  public ViewModel(ActivityView view) {
    super(view);
  }
}
```