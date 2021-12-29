# ApplicationLifecycle: expose life cycle of Application to its descent business components.
Android 组件化开发时, 将 Application 的生命周期主动发送给监听者业务组件中. 业务组件可以无侵入地根据 Application 生命周期执行一些诸如初始化, 清空缓存等操作.
<br>使用了 APT 技术, 生成 Application 生命周期监听者的代理类.
<br>使用了 AGP 技术, 用于编译期将 APT 生成的代理类添加到管理类内部保存的回调列表中.

# How to use APT&AGP from remote repository?
## How to use APT from remote repository?
<br>1, Add maven support. In your root build.gradle, add repository and dependency:
```
    repositories {
        maven { url('https://repo1.maven.org/maven2/') }
        ...
        
    dependencies {
        ...
        classpath('io.github.bytebeats:applifecycle-agp:$VERSION')
        ...
```
<br>In your app build.gradle, use plugin and dependency:
```
    plugins {
        id 'applifecycle-agp'
        ...
    }
...
    implementation 'io.github.bytebeats:applifecycle-api:$VERSION'
    implementation 'io.github.bytebeats:applifecycle-annotation:$VERSION'
    annotationProcessor 'io.github.bytebeats:applifecycle-apt:$VERSION'
```
<br>In you business module build.gradle, add annotation:
```
    implementation 'io.github.bytebeats:applifecycle-api:$VERSION'
    implementation 'io.github.bytebeats:applifecycle-annotation:$VERSION'
    annotationProcessor 'io.github.bytebeats:applifecycle-apt:$VERSION'
```

# How to use APT&AGP locally?
## How to use APT locally?
<br>In apt module,
* Finish `AbstractProcessor` and manifest `resources`, in my case, it's <br>`src/main/java/me/bytebeats/applifecycle/apt/ApplicationLifecycleProcessor` and <br>`src/main/resources/META-INF/services/javax.annotation.processing.Processor` where is the `path` of AbstractProcessor
* in `build.gradle`s of modules where you want to use this local `APT`, add `dependencies`: <br>
```
  implementation project(path: ':annotation')
  implementation project(path: ':api')
  annotationProcessor project(path: ':apt')
```
* then annotate you java class with `ApplicationLifecycle` and implements `ApplicationLifecycleCallback`
* then run `./gradlew assemble` in `Terminal`, you'll see:<br>
```
> Task :business-module:compileDebugJavaWithJavac
Note: ApplicationLifecycleProcessor has been initialized
Note: verifying class: me.bytebeats.business.BusinessAppLifecycleRequest
Note: start generating proxy class for me.bytebeats.business.BusinessAppLifecycleRequest
Note: AppLifecycle$$BusinessAppLifecycleRequest$$Proxy has been generated
```
<br>this means AGP has worked successfully.

## How to use AGP locally?
<br>In agp module,
* Finish `Plugin<Project>` and `Transform` and `agp_name.properties`, in my case, it's <br>`src/main/groovy/me/bytebeats/applifecycle/agp/ApplicationLifecyclePlugin`&`ApplicationLifecycleTransform` and <br>`src/main/resources/META-INF/services/applifecycle-agp.properties` where `implementation-class` is declared.
* in `agp/build.gradle`, declared `maven-publish` plugin and `mavenLocal()` and `pluginPublication` like this:<br>
```
plugins {
    id('groovy')
    id('maven-publish')
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])

    implementation gradleApi()
    implementation localGroovy()

    implementation 'com.android.tools.build:transform-api:1.5.0'
    implementation 'com.android.tools.build:gradle:3.6.4'
}

repositories {
    mavenLocal()
}

project.publishing {
    publications {
        pluginPublication(MavenPublication) {
            from components.java
            groupId 'me.bytebeats.agp'
            artifactId 'applifecycle-agp'
            $VERSION '$VERSION'
        }
    }
}
```
* then run `./gradlew publishToMavenLocal`, then `AGP` will be published to local maven.
* then run `./gradlew assemble` in `Terminal`, you'll see:<br>
```
> Task :app:transformClassesWithAppLifecycleAgpTransformForDebug
<<<<<------AppLifecycleAgpTransform started------>>>>>
Scanning inputs
target jar input : me.bytebeats.applifecycle.business.AppLifecycle$$BusinessAppLifecycleRequest$$Proxy.class
target directory input: .../ApplicationLifecycle/app/build/intermediates/javac/debug/classes
target file input: me/bytebeats/applifecycle/app/AppLifecycle$$AppLifecycleRequest$$Proxy.class
target directory input: .../ApplicationLifecycle/app/build/tmp/kotlin-classes/debug
ASM started
find manager class: me/bytebeats/applifecycle/api/ApplicationLifecycleManager.class
visitMethod: <init>
visitMethod: init
-------onMethodEnter------
proxy class inserted: me.bytebeats.applifecycle.business.AppLifecycle$$BusinessAppLifecycleRequest$$Proxy.class
proxy class inserted: me.bytebeats.applifecycle.app.AppLifecycle$$AppLifecycleRequest$$Proxy.class
-------onMethodExit------
visitMethod: addApplicationLifecycleCallback
...
visitMethod: <clinit>
inject finished
ASM finished
<<<<<------AppLifecycleAgpTransform finished------>>>>>
```
<br>this means agp has worked successfully.

## Stargazers over time

[![Stargazers over time](https://starchart.cc/bytebeats/ApplicationLifecycle.svg)](https://starchart.cc/bytebeats/ApplicationLifecycle)

## MIT License

    Copyright (c) 2021 Chen Pan

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
