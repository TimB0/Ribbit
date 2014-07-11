Ribbit
======

A Self Destructing Message Android app to send photos and videos to your friends that will self-destruct after 10 seconds.  Implementing Fragments, AsyncTasks to a Parse Back-End, Picasso library for Image Viewing, Commons-IO library for Image 
Resizing.

In order to successfully build and Run this app you need to:

1.  Create a Parse Account
2.  Create your Application
3.  Create a RibbitApplication.java file in:
Ribbit / app / src / main / java / com / racecondition / ribbit / app /

4.  Add the following code:

```java
package com.racecondition.ribbit.app;

import android.app.Application;

import com.parse.Parse;

public class RibbitApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "Application ID", "Client ID");

        PushService.setDefaultPushCallback(this, com.racecondition.ribbitsdm.app.ui.MainActivity.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

}
```

add your Parse Application Id and Client Key to the parse.initialize function in the RibbitApplication.java file
