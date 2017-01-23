## SwipeRefreshLayout Bindings ##

You can use this binding class on instances of `android.support.v4.widget.SwipeRefreshLayout`.

1. [Property](https://github.com/droideveloper/MVVMAndroid/blob/master/how-to/swipe-refresh-layout-bindings.md#property)
2. [Listener](https://github.com/droideveloper/MVVMAndroid/blob/master/how-to/swipe-refresh-layout-bindings.md#listener)

### Property ###

#### isRefreshing ####

in view.xml

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:bindings="http://schemas.android.com/apk/res-auto">
     
  <data>
    <variable name="viewModel" 
              type="org.fs.view.ViewModel" />
  </data>
  
  <android.support.v4.widget.SwipeRefreshLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    bindings:isRefreshing="@={viewModel.refreshing}">
    
  </android.support.v4.widget.SwipeRefreshLayout>  
      
</layout>        
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  private boolean refreshing;
  
  public ViewModel(ActivityView view) {
    super(view);
  }
  
  @Bindable public boolean getRefreshing() {
    return this.refreshing;
  }  
  
  public void setRefreshing(boolean refreshing) {
    this.refreshing = refreshing;
    notifyPropertyChanged(BR.refreshing);
  }
}
```

### Listener ###

#### OnRefreshListener ####

in view.xml

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:bindings="http://schemas.android.com/apk/res-auto">
     
  <data>
    <variable name="viewModel" 
              type="org.fs.view.ViewModel" />
  </data>
  
  <android.support.v4.widget.SwipeRefreshLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    bindings:refreshCallback="@{viewModel.refreshCallback}">
    
  </android.support.v4.widget.SwipeRefreshLayout>  
      
</layout>        
```

in RefreshCallback.java

```java
public class RefreshCallback implements OnRefreshed {
  
  @Override public void onRefreshed() { 
    
  }
    
}
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  public RefreshCallback refreshCallback = new RefreshCallback();
  
  public ViewModel(ActivityView view) {
    super(view);
  }
}
```

