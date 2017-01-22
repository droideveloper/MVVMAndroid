## FrameLayout Bindings ##

You can use this binding class on instances of `android.widget.FrameLayout`.

### Fragment Bindings ###

in view.xml

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:bindings="http://schemas.android.com/apk/res-auto">
      
  <data>
    <variable name="viewModel" 
              type="org.fs.view.ViewModel" />
  </data>
  
  <FrameLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    bindings:fragment="@{viewModel.fragment}" 
    bindings:fragmentManager="@{viewModel.fragmentManager}" 
    bindings:enterAnim="@{@anim/activity_anim_translate_right_in}"
    bindings:exitAnim="@{@anim/activity_anim_translate_right_out}"
    bindings:animReverse="@{@bool/autoReverse}" />
      
</layout>        
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {
  
  private Fragment        fragment;
  private FragmentManager fragmentManager;
  
  public ViewModel(ActivityView view) {
    super(view);
  }
  
  @Bindable public FragmentManager getFragmentManager() {
    return this. fragmentManager;
  }
 
  @Bindable public Fragment getFragment() {
    return this.fragment;
  }
  
  public void setFragment(Fragment fragment) {
    this.fragment = fragment;
    notifyPropertyChanged(BR.fragment);
  } 
}
```
