## ImageView Bindings ##

You can use this binding class on instances of `android.widget.ImageView`.

### ImageUrl Binding Using Glide ### 

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
  
    <ImageView
      android:layout_width="match_parent"
      android:layout_height="wrap_content"
      bindings:imageUrl="@{viewModel.imageUrl}" 
      bindings:placeholder="@{@drawable/placeholder}"
      bindings:error="@{@drawable/error}"/>
      
  </LinearLayout>
      
</layout>        
```

in ViewModel.java

```java
public class ViewModel extends AbstractViewModel<ActivityView> {

  private String imageUrl;
    
  public ViewModel(ActivityView view) {
    super(view);
  }
  
  @Bindable public String getImageUrl() {
    return this.imageUrl;
  }
  
  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
    notifyPropertyChanged(BR.imageUrl);
  }  
}
```
