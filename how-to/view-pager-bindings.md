## ViewPager Bindings ##

You can use this binding class on instances of `android.support.v4.view.ViewPager`.

#### ItemSource #####

in view.xml

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:bindings="http://schemas.android.com/apk/res-auto">
      
  <data>
    <variable name="viewModel" 
              type="org.fs.view.ViewModel" />
  </data>  
  
  <android.support.v4.view.ViewPager
    android:layout_width="match_parent"
    android:layout_height="match_parent" 
    bindings:itemSource="@{viewModel.itemSource}" />      
      
</layout>        
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  private PagerAdapter itemSource;
  
  public ViewModel(ActivityView view) {
    super(view);
  }
  
  @Bindable public PagerAdapter getItemSource() {
    return this.itemSource;
  }  
}
```

#### PageAnimator #####

in view.xml

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:bindings="http://schemas.android.com/apk/res-auto">
      
  <data>
    <variable name="viewModel" 
              type="org.fs.view.ViewModel" />
  </data>  
  
  <android.support.v4.view.ViewPager
    android:layout_width="match_parent"
    android:layout_height="match_parent" 
    bindings:pageAnimator="@{viewModel.pageAnimator}" />      
      
</layout>        
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  private ViewPager.PageTransformer pageAnimator;
  
  public ViewModel(ActivityView view) {
    super(view);
  }
  
  @Bindable public ViewPager.PageTransformer getPageAnimator() {
    return this.pageAnimator;
  }  
}
```

#### SelectedPage #####

in view.xml

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:bindings="http://schemas.android.com/apk/res-auto">
      
  <data>
    <variable name="viewModel" 
              type="org.fs.view.ViewModel" />
  </data>  
  
  <android.support.v4.view.ViewPager
    android:layout_width="match_parent"
    android:layout_height="match_parent" 
    bindings:selectedPage="@={viewModel.selectedPage}" />      
      
</layout>        
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  private int selectedPage;
  
  public ViewModel(ActivityView view) {
    super(view);
  }
  
  @Bindable public int getSelectedPage() {
    return this.selectedPage;
  }  
  
  public void setSelectedPage(int selectedPage) {
    this.selectedPage = selectedPage;
    notifyPropertyChanged(BR.selectedPage);
  }
}
```

#### Item #####

in view.xml

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:bindings="http://schemas.android.com/apk/res-auto">
      
  <data>
    <variable name="viewModel" 
              type="org.fs.view.ViewModel" />
  </data>  
  
  <android.support.v4.view.ViewPager
    android:layout_width="match_parent"
    android:layout_height="match_parent" 
    bindings:item="@={viewModel.selectedItem}" />      
      
</layout>        
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  private AbstractEntity selectedItem;
  
  public ViewModel(ActivityView view) {
    super(view);
  }
  
  @Bindable public AbstractEntity getSelectedItem() {
    return this.selectedItem;
  }  
  
  public void setSelectedItem(AbstractEntity selectedItem) {
    this.selectedItem = selectedItem;
    notifyPropertyChanged(BR.selectedItem);
  }
}
```

#### PageScrolled, PageSelected and PageScrollStateChanged #####

in view.xml

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:bindings="http://schemas.android.com/apk/res-auto">
      
  <data>
    <variable name="viewModel" 
              type="org.fs.view.ViewModel" />
  </data>  
  
  <android.support.v4.view.ViewPager
    android:layout_width="match_parent"
    android:layout_height="match_parent" 
    bindings:onPageScrolled="@{viewModel.pageScrolled}"
    bindings:onPageSelected="@{viewModel.pageSelected}"
    bindings:onPageScrollStateChanged="@{viewModel.pageScrollStateChanged}" />      
      
</layout>        
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  public ViewModel(ActivityView view) {
    super(view);
  }
  
  @Bindable public OnPageScrolled getPageScrolled() {
    return new OnPageScrolled() {
      @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
       
      }
    };
  }  
  
  @Bindable public OnPageSelected getPageSelected() {
    return new OnPageSelected() {
      @Override public void onPageSelected(int position) {
        
      }
    };
  }
  
  @Bindable public OnPageScrollStateChanged getPageScrollStateChanged() {
    return new OnPageScrollStateChanged() {
      @Override public void onPageScrollStateChanged(int state) {
        
      }
    };
  }
}
```