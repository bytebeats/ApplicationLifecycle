# ApplicationLifecycle: expose life cycle of Application to its descent business components.
Android 组件化开发时, 将 Application 的生命周期主动发送给监听者业务组件中. 业务组件可以无侵入地根据 Application 生命周期执行一些诸如初始化, 清空缓存等操作.
<br>使用了 APT 技术, 生成 Application 生命周期监听者的代理类.
<br>使用了 AGP 技术, 用于编译期将 APT 生成的代理类添加到管理类内部保存的回调列表中.

## Stargazers over time

[![Stargazers over time](https://starchart.cc/bytebeats/ApplicationLifecycle.svg)](https://starchart.cc/bytebeats/ApplicationLifecycle)