# IshikotaKnife
Simple View injection framework for learning [annotation processing][1], [javapoet][2], [compile-testing][3]


# Usage
First, you put `@IshikotaBind` annotation on the field where you want to inject View.
```java
public class ExampleActivity extends AppCompatActivity {
    @IshikotaBind(R.id.text)
    public TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      IshikotaKnife.bind(this);
      mTextView.setText("We injected !!");
    }
}
```

In compile time, below source code which suffixed with `$$IshikotaViewBinder` is created.
```java
public class ExampleActivity$$IshikotaViewBinder {
  public void bind(jp.ikota.ishikotaknifeuser.ExampleActivity target) {
    ((ExampleActivity)target).mTextView = (android.widget.TextView)target.findViewById(2131492944);
  }
}
```

Finally, `IshikotaKnife.bind` method invokes `ExampleActivity$$IshikotaViewBinder.bind` method and inject views.  
So now, we can access our views without NullPointerException!!
```java
public class IshikotaKnife {
  public static void bind(Activity target) {
    final Class clazz = Class.forName(target.getClass().getCanonicalName() + "$$IshikotaViewBinder");
    final Method bind = clazz.getMethod("bind", target.getClass());
    bind.invoke(clazz.newInstance(), target);
  }
}
```

# Gradle

```groovy
apply plugin: 'com.android.application'

buildscript {
  repositories {
    jcenter()
  }
  dependencies {
    classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
  }
}

apply plugin: 'com.neenbedankt.android-apt'

android {
    //...
}

dependencies {
    //...
    compile 'jp.ikota:ishikotaknife:0.0.1'
    compile 'jp.ikota:ishikotaknife-annotations:0.0.1'
    apt 'jp.ikota:ishikotaknife-compiler:0.0.1'
}
```

License
-------
    Copyright 2015 Kota Ishimoto

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    
[1]: http://docs.oracle.com/javase/7/docs/api/javax/annotation/processing/package-summary.html
[2]: http://github.com/square/javapoet
[3]: http://github.com/google/compile-testing
