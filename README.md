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

　　  implementation 'com.github.AgnoiY:RrtrofitFrame:Tag'
  
  }
  
2.使用方法

　在Application中
 
     onCreate()方法里进行注册：
 
 　　　　　RetrofitLibrary.init(this, BaseUrl);
  
     onTerminate()方法里进行注销：
     
     　　　RetrofitLibrary.onDestory();
