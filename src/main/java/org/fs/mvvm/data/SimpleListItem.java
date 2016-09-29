/*
 * MVVM_Workspace Copyright (C) 2016 Fatih.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fs.mvvm.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import org.fs.mvvm.BR;
import org.fs.mvvm.utils.Objects;

public class SimpleListItem extends BaseObservable implements Parcelable {

  private String textString;

  @Bindable public String getTextString() {
    return this.textString;
  }

  public void setTextString(String textString) {
    this.textString = textString;
    notifyPropertyChanged(BR.textString);
  }

  protected SimpleListItem(Parcel input) {
    boolean hasTextString = input.readInt() == 1;
    if (hasTextString) {
      textString = input.readString();
    }
  }

  private SimpleListItem(String textString) {
    this.textString = textString;
  }

  public Builder newBuilder() {
    return new Builder()
        .textString(textString);
  }

  @Override public void writeToParcel(Parcel out, int flags) {
    boolean hasTextString = !Objects.isNullOrEmpty(textString);
    out.writeInt(hasTextString ? 1 : 0);
    if (hasTextString) {
      out.writeString(textString);
    }
  }

  @Override public int describeContents() {
    return 0;
  }

  public static final Creator<SimpleListItem> CREATOR = new Creator<SimpleListItem>() {
    @Override public SimpleListItem createFromParcel(Parcel in) {
      return new SimpleListItem(in);
    }

    @Override public SimpleListItem[] newArray(int size) {
      return new SimpleListItem[size];
    }
  };

  public static class Builder {
    private String textString;

    public Builder() { }
    public Builder textString(String textString) { this.textString = textString; return this; }
    public SimpleListItem build() { return new SimpleListItem(textString); }
  }
}
