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

　　  implementation 'com.github.AgnoiY:RrtrofitFrame:1.0.17'
  
  }
  
2.使用方法

　　在Application中
 
    　 onCreate()方法里进行注册：
 
 　　　　　　RetrofitLibrary.init(this, BaseUrl);
  
     　onTerminate()方法里进行注销：
     
　　　　　　RetrofitLibrary.onDestory();
      
  　调用方法
   
   　　RetrofitLibrary.getRetrofitHttp()
     
     　　　　.post()　//请求方式
         
         　　.apiUrl(url地址)
           
             .addParameter(map)　// 参数类型
             
             .build()
             
             .request(new HttpObserver<实力类>(this, true, true) { //(Context, 是否加载弹窗, 点击返回键是否取消加载弹窗)　多个重载方法
             
                    @Override
                    
                    public void onSuccess(String action, 实力类 value) {
                   
                    }
                    
                });
          
３．重写业务逻辑解析方式继承BaseHttpObserver

　　　
　　
