# ApplicationLifecycle: expose life cycle of Application to its descent business components.
Android 组件化开发时, 将 Application 的生命周期主动发送给监听者业务组件中. 业务组件可以无侵入地根据 Application 生命周期执行一些诸如初始化, 清空缓存等操作.
<br>使用了 APT 技术, 生成 Application 生命周期监听者的代理类.
<br>使用了 AGP 技术, 用于编译期将 APT 生成的代理类添加到管理类内部保存的回调列表中.

# How to use APT&AGP locally?
## How to use APT locally?
* Finish `AbstractProcessor` and manifest `resources`, in my case, it's `src/main/java/me/bytebeats/applifecycle/apt/ApplicationLifecycleProcessor` and `src/main/resources/META-INF/services/javax.annotation.processing.Processor` where is the `path` of AbstractProcessor
* in `build.gradle`s of modules where you want to use this local `APT`, add `dependencies`: 
  `implementation project(path: ':annotation')
    implementation project(path: ':api')
    annotationProcessor project(path: ':apt')`
* then annotate you java file with `ApplicationLifecycle` which implements `ApplicationLifecycleCallback`
* then run `./gradlew assemble` in `Terminal`, you'll see:
`> Task :business-module:compileDebugJavaWithJavac
Note: ApplicationLifecycleProcessor has been initialized
Note: verifying class: me.bytebeats.business.BusinessAppLifecycleRequest
Note: start generating proxy class for me.bytebeats.business.BusinessAppLifecycleRequest
Note: AppLifecycle$$BusinessAppLifecycleRequest$$Proxy has been generated`
this means apt has worked successfully.

## How to use AGP locally?
* Finish `Plugin<Project>` and `Transform` and `agp_name.properties`, in my case, it's `src/main/groovy/me/bytebeats/applifecycle/agp/ApplicationLifecyclePlugin`&`ApplicationLifecycleTransform` and `src/main/resources/META-INF/services/applifecycle-agp.properties` where `implementation-class` is declared.
* in `agp/build.gradle`, declared `maven-publish` plugin and `mavenLocal()` and `pluginPublication` like this:
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
            version '1.0.0'
        }
    }
}
```
* then run `./gradlew publishToMavenLocal`, then `AGP` will be published to local maven.

## Stargazers over time

[![Stargazers over time](https://starchart.cc/bytebeats/ApplicationLifecycle.svg)](https://starchart.cc/bytebeats/ApplicationLifecycle)