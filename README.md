# hotfix-android
A Simple Module For HotFix， you can use it for bug fix;
#### 使用前提
> 需要连接网络
>> 如果没有网络，将无法下载热更新的补丁而无法使用

#### 原理

下载补丁 dex 文件到指定目录， 然后将 dex 文件插入到 BaseDexClassLoader 的 dexPathList 的前面，这样在通过 ClassLoader 加载类时，会先检测到补丁中的类而加载使用，而不会继续加载有问题的类， 从而达到修复更新的目的。

#### 使用方法

> 准备工作

上方的 app 目录为一个热更新的 module，使用时将 app 引入需要热更新功能的项目中，在项目的 build.gradle 文件中依赖热更新 module。

然后，需要有一个后台程序，能够提供热更新补丁的下载， tomcat 可以很方便的达到目的。

<b>关于热更新的补丁，暂时只支持 dex 文件，可以使用 build-tools 中的 dx 工具将 class 文件打包成 dex 文件。 具体打包的方法可以百度，这里直说一个大概：</br>
	1. 配置 dx 的环境变量;</br>
	2. 将 android studio 编译过后的工程目录（output 中）复制到任意目录下，运行一下命令
		dx --dex --output=名称.dex 需要打包的文件的名称 ..
</b>

> 使用

在合适的位置调用

			Handler mHandler = new Handler() {
				@Override
		        public void handleMessage(Message msg) {
		            super.handleMessage(msg);
		            if (Build.VERSION.SDK_INT >= 23) {
		                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
		                    requestPermissions(permission, 200);
		                    return;
		                }
		            }
		            manager.hotfix(MainActivity.this);
		        }
			}
            manager = HotFixManager.instance(this)
                    .downloadPatchDexPath(dex 文件的下载地址)
                    .init(new DownloadUtil.DownCallBack() {
                        @Override
                        public void callback() {
                            mHandler.obtainMessage().sendToTarget();
                        }
                    });
        


（本 module 并没有提供判断是否需要下载热更新补丁的接口，所以具体什么时候需要下载补丁请自行判断；）
