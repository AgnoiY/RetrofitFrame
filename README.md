# RrtrofitFrame
Retrofit框架

1.添加方法

   allprojects{
  
      repositroies{
      
         ....
         
        maven { url 'https://jitpack.io' }
        
      }

  }

  dependencies {
   
    /*网络请求框架*/
    
    implementation 'com.github.AgnoiY:RrtrofitFrame:1.0.18'
    
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
  
2.使用方法

　　在Application中
 
    　 onCreate()方法里进行注册：
 
 　　　　　　RetrofitLibrary.getHttpConfigure().setNotTipDialog(true);　// 网络请求基础配置类,　可选
       
 　　　　　　RetrofitLibrary.init(this, BaseUrl);
  
     　onTerminate()方法里进行注销：
     
　　　　　　RetrofitLibrary.onDestory();
      
  　调用方法
   
   　　RetrofitLibrary.getRetrofitHttp()
     
     　　　.post()　//请求方式
         
         　.apiUrl(url地址)
           
           .addParameter(map)　// 参数类型
             
           .build()
             
           .request(new HttpObserver<Ｔ(实体类)>(this, true, true) { //(Context, 是否加载弹窗, 点击返回键是否取消加载弹窗)多个重载方法
             
                    @Override
                    
                    public void onSuccess(String action, T value) {
                   
                    }
                    
                });
                
    实体类要继承BaseResponseModel<Ｔ>或BaseResponseListModel<T>
      
           public class Name extends BaseResponseModel<Name> {}
          
３．重写业务逻辑解析方式继承BaseHttpObserver，实体类无需继承上述两个，根据需要自定义

　　　
　　
