# RrtrofitFrame
Retrofit框架

1.添加方法:

   allprojects{
  
      repositroies{
      
         ....
         
        maven { url 'https://jitpack.io' }
        
      }

  }

  dependencies {
   
    /*网络请求框架*/
    
    implementation 'com.github.AgnoiY:RetrofitFrame:1.1.8'
    
    implementation 'com.squareup.okhttp3:okhttp:3.10.0'
    
    implementation 'com.squareup.retrofit2:retrofit:2.3.0'
    
    implementation 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
    
    implementation 'com.squareup.retrofit2:converter-gson:2.3.0'
    
    /*RxJava&RxAndroid*/
    
    implementation 'io.reactivex.rxjava2:rxandroid:2.0.1'
    
    implementation 'io.reactivex.rxjava2:rxjava:2.1.3'
    
    /*RxLifecycle基础库*/
    
    implementation 'com.trello.rxlifecycle2:rxlifecycle:2.1.0' //可选
    
    implementation 'com.trello.rxlifecycle2:rxlifecycle-components:2.1.0'　//可选
  
  }
  
2.使用方法:

　　在Application中:
 
    　 onCreate()方法里进行注册：
 
 　　　　　　RetrofitLibrary.getHttpConfigure().setNotTipDialog(true);　// 网络请求基础配置类,　可选
       
 　　　　　　RetrofitLibrary.init(this, BaseUrl);
  
     　onTerminate()方法里进行注销：
     
　　　　　　RetrofitLibrary.onDestory();
      
  　调用方法:
   
   　　RetrofitLibrary.getRetrofitHttp()
     
     　　　.post()　//请求方式
         
         　.apiUrl(url地址)
           
           .addParameter(map)　// 参数类型
             
           .build()
             
           .request(new HttpObserver<Ｔ(实体类)>(context, true, true) {//(Context, 是否加载弹窗, 点击返回键是否取消加载弹窗)多个重载方法
             
                    @Override
                    
                    public void onSuccess(String action, T value) { //action: 标识（请求不设置，默认是apiUrl）
                   
                    }
                    
                });
                
    实体类:继承BaseResponseModel<Ｔ>
      
           public class Name extends BaseResponseModel<Name> {}
           
3.文件上传：

           .upload(new UploadObserver<T(实体类)>(context, false){//(Context, 是否加载弹窗)、无参数构造器

                    @Override
                    
                    public void onSuccess(String action, T value) {
                   
                    }
                    
                    /**
                    
                     * 上传进度回调
                     
                     *
                     
                     * @param file         源文件
                     
                     * @param currentSize  当前上传值
                     
                     * @param totalSize    总大小
                     
                     * @param progress     进度
                     
                     * @param currentIndex 当前下标
                     
                     * @param totalFile    总文件数
                     
                     */
                     
                    public void onProgress(File file, long currentSize, long totalSize, 
                    
                                   float progress, int currentIndex, int totalFile);

               });
          
4．业务逻辑基类不一样，通过注解改变

　　　
　　
