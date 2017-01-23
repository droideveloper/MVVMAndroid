## AppBarLayout Bindings ## 

You can use this binding class on instances of `android.support.design.widget.AppBarLayout`.

1. [Property](https://github.com/droideveloper/MVVMAndroid/blob/master/how-to/app-bar-layout-bindings.md#property)
2. [Listener](https://github.com/droideveloper/MVVMAndroid/blob/master/how-to/app-bar-layout-bindings.md#listener)

### Property ###

Properties you can bind on instances of `AppBarLayout`.

#### Offset ####

in view.xml

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:bindings="http://schemas.android.com/apk/res-auto">
      
  <data>
    <variable name="viewModel" 
              type="org.fs.view.ViewModel" />
  </data>
  
  <android.support.design.widget.CoordinatorLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent">
  
    <android.support.design.widget.AppBarLayout
      android:layout_width="match_parent"
      android:layout_height="@dimen/barLayoutHeight"
      bindings:offset="@={viewModel.offset}" >
    
      ...
    
    </android.support.design.widget.AppBarLayout>
      
  </android.support.design.widget.CoordinatorLayout>
      
</layout>        
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  private int offset;
  
  public ViewModel(ActivityView view) {
    super(view);
  }
  
  @Bindable public int getOffset() {
    return this.textParameter;
  }
  
  public void setOffset(int offset) {
    this.offset = offset;
    notifyPropertyChanged(BR.offset);
  }
}
```

### Listener ###

Listeners you can register on instances of `AppBarLayout`.
 
####  OffsetChangedListener #### 

in view.xml

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:bindings="http://schemas.android.com/apk/res-auto">
      
  <data>
    <variable name="viewModel" 
              type="org.fs.view.ViewModel" />
  </data>
  
  <android.support.design.widget.CoordinatorLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent">
  
    <android.support.design.widget.AppBarLayout
      android:layout_width="match_parent"
      android:layout_height="@dimen/barLayoutHeight"
      bindings:onOffsetChanged="@{viewModel.offsetChangedCallback}" >
    
      ...
    
    </android.support.design.widget.AppBarLayout>
      
  </android.support.design.widget.CoordinatorLayout>
      
</layout>        
```

in OffsetChangedListener.java

```java
public class OffsetChangedListener implements AppBarLayout.OnOffsetChangedListener {
  @Override public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
    //...
  }
}

```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  public final OffsetChangedListener offsetChangedCallback = new OffsetChangedListener();
  
  public ViewModel(ActivityView view) {
    super(view);
  }
}
```
