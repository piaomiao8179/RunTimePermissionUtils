# RunTimePermissionUtils
按照郭霖大神思路写出的Android 6.0权限处理
具体说明：
--前言：本Demo借鉴郭霖大神的Android6.0运行时权限思路，不做任何商业用途，仅供学习。

--使用步骤：
    <li> 将BaseActivity拷贝到项目中，其余新建Activity继承BaseActivity。
    <li> 在项目中的任何位置调用requestRuntimePermission方法，该方法需要两个参数，
         第一个参数为需要申请的权限，第二个参数为授权完成后的回调。
    <li> 若项目中已经有了自己的基类，让基类继承BaseActivity即可。


--该API思路清晰简单。
