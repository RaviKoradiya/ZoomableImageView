# ZoomableImageView

ZoomableImageView is build to open any image in full screen.

[![](https://jitpack.io/v/RaviKoradiya/ZoomableImageView.svg)](https://jitpack.io/#RaviKoradiya/ZoomableImageView)

![Demo](/../master/demo_image/ezgif-2-ccb935fa45.gif?raw=true "Demo")

## Features
- Zoom any image from any context like activity, fragment or any scrollable container. 
- After zooming you can pinch zoom or double tap to zoom. 
- You can set animation speed with parameter `animation_speed`
- You also can load any web url with `image_url` or `setImageUrl(String imageUrl)`

## Dependency

Add this in your project level `build.gradle` file (**not** your module `build.gradle` file):

```gradle
allprojects {
	repositories {
        maven { url "https://jitpack.io" } // add this line
    }
}
```

Then, add the library to your module `build.gradle`
```gradle
dependencies {
    compile 'com.github.RaviKoradiya:ZoomableImageView:1.0.2'
}
```

## Usage

```xml
<com.ravikoradiya.zoomableimageview.ZoomableImageView
            android:layout_width="match_parent"
            android:layout_height="300dp"
            android:scaleType="centerCrop"
            android:src="@drawable/test3"
            app:image_url="http://..." />
```
```java
        ZoomableImageView zoomableImageView = (ZoomableImageView) findViewById(R.id.iv_zoomable);
        zoomableImageView.setImageUrl("http://...");
```
That's it!


License
--------

    Copyright 2017 Ravi Koradiya

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
