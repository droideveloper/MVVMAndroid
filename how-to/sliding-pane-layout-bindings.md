## SlidingPaneLayout Bindings ##

You can use this binding class on instances of `android.support.v4.widget.SlidingPaneLayout`.

1. Property
  * [isOpen](https://github.com/droideveloper/MVVMAndroid/blob/master/how-to/sliding-pane-layout-bindings.md#isopen)
  
2. Listener
  * [LayoutSlided](https://github.com/droideveloper/MVVMAndroid/blob/master/how-to/sliding-pane-layout-bindings.md#layoutslided)
  * [LayoutOpenedOrClosed](https://github.com/droideveloper/MVVMAndroid/blob/master/how-to/sliding-pane-layout-bindings.md#layoutopenedorclosed)
  
### Property ###

Properties we can bind on DrawerLayout instance.

#### IsOpen ####

in view.xml

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:bindings="http://schemas.android.com/apk/res-auto">
      
  <data>
    <variable name="viewModel" 
              type="org.fs.view.ViewModel" />
  </data>
    
  <android.support.v4.widget.SlidingPaneLayout
      xmlns:android="http://schemas.android.com/apk/res/android"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      bindings:isOpen="@={viewModel.isOpen}">
  
  </android.support.v4.widget.SlidingPaneLayout>  
      
</layout>        
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  private boolean isOpen;
  
  public ViewModel(ActivityView view) {
    super(view);
  }
  
  @Bindable public boolean getIsOpen() {
    return this.isOpen;
  }
  
  public void setIsOpen(boolean isOpen) {
    this.isOpen = isOpen;
    notifyPropertyChanged(BR.isOpen);
  }
}
```

### Listener ###

Listeners we can bind on DrawerLayout instance.

#### LayoutSlided ####

in view.xml

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:bindings="http://schemas.android.com/apk/res-auto">
      
  <data>
    <variable name="viewModel" 
              type="org.fs.view.ViewModel" />
  </data>
    
  <android.support.v4.widget.SlidingPaneLayout
      xmlns:android="http://schemas.android.com/apk/res/android"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      bindings:onSlided="@{viewModel.layoutSlidedCallback}">
  
  </android.support.v4.widget.SlidingPaneLayout>  
      
</layout>        
```

in LayoutSlided.java

```java
public class LayoutSlided implements OnLayoutSlided {

  @Override public void onLayoutSlided(View viewLayout, float slideOffset) { 
    
  }

}
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  public final LayoutSlided layoutSlidedCallback = new LayoutSlided();
  
  public ViewModel(ActivityView view) {
    super(view);
  }
}
```

#### LayoutOpenedOrClosed ####

in view.xml

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:bindings="http://schemas.android.com/apk/res-auto">
      
  <data>
    <variable name="viewModel" 
              type="org.fs.view.ViewModel" />
  </data>
    
  <android.support.v4.widget.SlidingPaneLayout
      xmlns:android="http://schemas.android.com/apk/res/android"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      bindings:onOpenOrClose="@{viewModel.layoutOpenedOrClosedCallback}">
  
  </android.support.v4.widget.SlidingPaneLayout>  
      
</layout>        
```

in LayoutOpenedOrClosed.java

```java
public class LayoutOpenedOrClosed implements OnLayoutOpenedOrClosed {

  @Override public void onLayoutOpenedOrClosed(boolean isOpen) { 
    
  }
}
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  public final LayoutOpenedOrClosed layoutOpenedOrClosedCallback = new LayoutOpenedOrClosed();
  
  public ViewModel(ActivityView view) {
    super(view);
  }
}
```