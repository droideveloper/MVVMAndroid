## RecyclerView Bindings ##

You can use this binding class on instances of `android.support.v7.widget.RecyclerView`.

1. [ItemSource](https://github.com/droideveloper/MVVMAndroid/blob/master/how-to/recycler-view-bindings.md#itemsource)
2. [LayoutManager](https://github.com/droideveloper/MVVMAndroid/blob/master/how-to/recycler-view-bindings.md#layoutmanager)
3. [ItemAnimator](https://github.com/droideveloper/MVVMAndroid/blob/master/how-to/recycler-view-bindings.md#itemanimator)
4. [TouchHelper](https://github.com/droideveloper/MVVMAndroid/blob/master/how-to/recycler-view-bindings.md#touchhelper)
5. [Position](https://github.com/droideveloper/MVVMAndroid/blob/master/how-to/recycler-view-bindings.md#position)
6. [Positions](https://github.com/droideveloper/MVVMAndroid/blob/master/how-to/recycler-view-bindings.md#positions)
7. [Item](https://github.com/droideveloper/MVVMAndroid/blob/master/how-to/recycler-view-bindings.md#item)
8. [Items](https://github.com/droideveloper/MVVMAndroid/blob/master/how-to/recycler-view-bindings.md#items)
9. [IsLoading](https://github.com/droideveloper/MVVMAndroid/blob/master/how-to/recycler-view-bindings.md#isloading)

### ItemSource ###

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
  
    <android.support.v7.widget.RecyclerView
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"      
      bindings:itemSource="@{viewModel.itemSource}" />
      
  </LinearLayout>
      
</layout>        
```

in AdapterViewHolder.java 

```java
public AdapterViewHolder extends AbstractRecyclerBindingHolder<BaseObservable> {
 ...
}
```

in AdapterItemSource.java

```java
public class AdapterItemSource extends AbstractRecyclerBindingAdapter<BaseObservable, AdapterViewHolder> {
  ...
}
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  //create adapter from dagger2, dependency injection
  @Inject AdapterItemSource itemSource;
  private ObservableList<BaseObservable> dataSet;
    
  public ViewModel(ActivityView view) {
    super(view);
    this.dataSet = new ObservableList<BaseObservable>();
  }
  
  @Bindable public AdapterItemSource getAdapterItemSource() {
    return this.adapterItemSource;
  }
}
```

### LayoutManager ###

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
  
    <android.support.v7.widget.RecyclerView
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"      
      bindings:itemSource="@{viewModel.itemSource}" 
      bindings:layoutManager="@={viewModel.layoutManager}" />
      
  </LinearLayout>
      
</layout>        
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  //create adapter from dagger2, dependency injection
  @Inject AdapterItemSource itemSource;
  @Inject LayoutManager layoutManager;
  private ObservableList<BaseObservable> dataSet;
      
  public ViewModel(ActivityView view) {
    super(view);
    this.dataSet = new ObservableList<BaseObservable>();
  }
  
  @Bindable public AdapterItemSource getAdapterItemSource() {
    return this.adapterItemSource;
  }
  
  @Bindable public LayoutManager getLayoutManager() {
    return this.layoutManager;
  }
}
```

### ItemAnimator ###

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
  
    <android.support.v7.widget.RecyclerView
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"      
      bindings:itemSource="@{viewModel.itemSource}" 
      bindings:itemAnimator="@{viewModel.itemAnimator}" />
      
  </LinearLayout>
      
</layout>        
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  //create adapter from dagger2, dependency injection
  @Inject AdapterItemSource itemSource;
  @Inject ItemAnimator itemAnimator;
  private ObservableList<BaseObservable> dataSet;
      
  public ViewModel(ActivityView view) {
    super(view);
    this.dataSet = new ObservableList<BaseObservable>();
  }
  
  @Bindable public AdapterItemSource getAdapterItemSource() {
    return this.adapterItemSource;
  }
  
  @Bindable public ItemAnimator getItemAnimator() {
    return this.itemAnimator;
  }
}
```

### TouchHelper ###

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
  
    <android.support.v7.widget.RecyclerView
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"      
      bindings:itemSource="@{viewModel.itemSource}" 
      bindings:touchHelper="@={viewModel.touchHelper}" />
      
  </LinearLayout>
      
</layout>        
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  //create adapter from dagger2, dependency injection
  @Inject AdapterItemSource itemSource;
  @Inject ItemTouchHelper touchHelper;
  private ObservableList<BaseObservable> dataSet;
      
  public ViewModel(ActivityView view) {
    super(view);
    this.dataSet = new ObservableList<BaseObservable>();
  }
  
  @Bindable public AdapterItemSource getAdapterItemSource() {
    return this.adapterItemSource;
  }
  
  @Bindable public ItemTouchHelper getTouchHelper() {
    return this.touchHelper;
  }
}
```

### Position ###

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
  
    <android.support.v7.widget.RecyclerView
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"      
      bindings:itemSource="@{viewModel.itemSource}" 
      bindings:position="@={viewModel.position}" />
      
  </LinearLayout>
      
</layout>        
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  //create adapter from dagger2, dependency injection
  @Inject AdapterItemSource itemSource;
  private ObservableList<BaseObservable> dataSet;
  
  private int position;
    
  public ViewModel(ActivityView view) {
    super(view);
    this.dataSet = new ObservableList<BaseObservable>();
  }
  
  @Bindable public AdapterItemSource getAdapterItemSource() {
    return this.adapterItemSource;
  }
  
  @Bindable public int getPosition() {
    return this.position;
  }
  
  public void setPosition(int position) {
    this.position = position;
    notifyPropertyChanged(BR.position);
  }
}
```

### Positions ###

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
  
    <android.support.v7.widget.RecyclerView
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"      
      bindings:itemSource="@{viewModel.itemSource}" 
      bindings:positions="@={viewModel.positions}" />
      
  </LinearLayout>
      
</layout>        
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  //create adapter from dagger2, dependency injection
  @Inject AdapterItemSource itemSource;
  private ObservableList<BaseObservable> dataSet;
  
  private Collection<Integer> positions;
    
  public ViewModel(ActivityView view) {
    super(view);
    this.dataSet = new ObservableList<BaseObservable>();
  }
  
  @Bindable public AdapterItemSource getAdapterItemSource() {
    return this.adapterItemSource;
  }
  
  @Bindable public Collection<Integer> getPositions() {
    return this.position;
  }
  
  public void setPositions(Collection<Integer> positions) {
    this.positions = positions;
    notifyPropertyChanged(BR.positions);
  }
}
```

### Item ###

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
  
    <android.support.v7.widget.RecyclerView
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"      
      bindings:itemSource="@{viewModel.itemSource}" 
      bindings:item="@={viewModel.item}" />
      
  </LinearLayout>
      
</layout>        
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  //create adapter from dagger2, dependency injection
  @Inject AdapterItemSource itemSource;
  private ObservableList<BaseObservable> dataSet;
  
  private BaseObservable item;
    
  public ViewModel(ActivityView view) {
    super(view);
    this.dataSet = new ObservableList<BaseObservable>();
  }
  
  @Bindable public AdapterItemSource getAdapterItemSource() {
    return this.adapterItemSource;
  }
  
  @Bindable public BaseObservable getItem() {
    return this.item;
  }
  
  public void setItem(BaseObservable item) {
    this.item = item;
    notifyPropertyChanged(BR.item);
  }
}
```

### Items ###

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
  
    <android.support.v7.widget.RecyclerView
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"      
      bindings:itemSource="@{viewModel.itemSource}" 
      bindings:items="@={viewModel.items}" />
      
  </LinearLayout>
      
</layout>        
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  //create adapter from dagger2, dependency injection
  @Inject AdapterItemSource itemSource;
  private ObservableList<BaseObservable> dataSet;
  
  private Collection<BaseObservable> items;
    
  public ViewModel(ActivityView view) {
    super(view);
    this.dataSet = new ObservableList<BaseObservable>();
  }
  
  @Bindable public AdapterItemSource getAdapterItemSource() {
    return this.adapterItemSource;
  }
  
  @Bindable public Collection<BaseObservable> getItems() {
    return this.items;
  }
  
  public void setItems(Collection<BaseObservable> items) {
    this.items = items;
    notifyPropertyChanged(BR.items);
  }
}
```

### IsLoading ###

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
  
    <android.support.v7.widget.RecyclerView
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"      
      bindings:itemSource="@{viewModel.itemSource}" 
      bindings:isLoading="@={viewModel.isLoading}" />
      
  </LinearLayout>
      
</layout>        
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  //create adapter from dagger2, dependency injection
  @Inject AdapterItemSource itemSource;
  private ObservableList<BaseObservable> dataSet;
  
  private boolean isLoading;
    
  public ViewModel(ActivityView view) {
    super(view);
    this.dataSet = new ObservableList<BaseObservable>();
  }
  
  @Bindable public AdapterItemSource getAdapterItemSource() {
    return this.adapterItemSource;
  }
  
  @Bindable public boolean getIsLoading() {
    return this.isLoading;
  }
  
  public void setIsLoading(boolean isLoading) {
    this.isLoading = isLoading;
    notifyPropertyChanged(BR.isLoading);
  }
}