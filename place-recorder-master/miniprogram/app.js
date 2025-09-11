//app.js

const _SI = require('secret-info.js');
var env;

// app 配置对象
var app = {
  onLaunch: function() {

    // 展示本地存储能力
    var logs = wx.getStorageSync('logs') || [];
    logs.unshift(Date.now());
    wx.setStorageSync('logs', logs);

    checkUpdate();
    cloudInitialize();

    // 登录，换取 openid
    wx.getStorage({
      key: 'openid',
      success: res => {
        this.globalData.openid = res.data;
        this.setOpenId(this.globalData.openid)
        console.log("wx.getStorage of openid success: ", this.globalData.openid);
      },
      fail: res => {
        console.log("openid not found!");
        this.getOpenid();
      }
    })
    // this.globalData.openid = wx.getStorageSync("openid")

    // 获取用户信息
    wx.getSetting({
      success: res => {
        if (res.authSetting['scope.userInfo']) {
          wx.getUserProfile({
            success: res => {
              // 可以将 res 发送给后台解码出 unionId
              this.globalData.userInfo = res.userInfo;
              console.log(res);

              // 由于 getUserProfile 是网络请求，可能会在 Page.onLoad 之后才返回
              // 所以此处加入 callback 以防止这种情况
              if (this.userInfoReadyCallback) {
                this.userInfoReadyCallback(res);
              }
            }
          });
        }
      }
    });


  },
  // 设置OpenID的方法
  setOpenId(openId) {
    this.globalData.openId = openId;
  },
  
  // 获取OpenID的方法
  getOpenIdFromCache() {
    return this.globalData.openId;
  },
  getOpenid: function() {
    console.log("getOpenid in app.js called.");
    wx.login({
      success: (res) => {
        if (res.code) {
          // 调用后端接口
          wx.request({
            url: 'http://127.0.0.1:8081/place/main/getOpenId',
            method: 'POST',
            data: {
              jsCode: res.code
            },
            success: (response) => {
              if (response.data.code === 0) {
                const openId = response.data.data;
                console.log('获取到的OpenID:', openId);
                // 存储OpenID到本地
                this.globalData.openid = openId;
                this.setOpenId(openId)
                wx.setStorage({
                    key: 'openid',
                    data: openId,
                });
              } else {
                console.error('获取OpenID失败:', response.data.message);
              }
            },
            fail: (error) => {
              console.error('网络请求失败:', error);
            }
          });
        } else {
          console.error('登录失败:', res.errMsg);
        }
      }
        });
  },

  globalData: {
    userInfo: undefined,

    openid: undefined,
    logged: false,

    wgs84: undefined,
    gcj02: undefined
  }
}

/**
 * 调用 App() 函数创建实例。
 * 传递的参数为 app 对象。
 */
App(app);



/**
 * 启动时检查更新，若有新版本，马上应用并重启。
 * 
 * 这里的函数定义形式也可以是 var updater = function() {...} 。
 * 二者的区别是，变量形式的函数定义，需要考虑提前声明（需要在使用之前创建（声明）），其本质是个“函数引用”类型的变量；
 * 而函数形式的函数定义则不用考虑提前声明。
 */
function checkUpdate() {
  const updateManager = wx.getUpdateManager();

  updateManager.onCheckForUpdate(function(res) {
    // 请求完新版本信息的回调
    console.log(res.hasUpdate);
  })

  updateManager.onUpdateReady(function() {
    wx.showModal({
      title: '更新提示',
      content: '新版本已经准备好，是否重启应用？',
      success(res) {
        if (res.confirm) {
          // 新的版本已经下载好，调用 applyUpdate 应用新版本并重启
          updateManager.applyUpdate();
        }
      }
    });
  });

  updateManager.onUpdateFailed(function() {
    // 新版本下载失败
  });
}


/**
 *  云能力初始化
 */
function cloudInitialize() {
  if (!wx.cloud) {
    console.error('请使用 2.2.3 或以上的基础库以使用云能力')
  } else {
    wx.getSystemInfo({
      success(res) {
        console.log("SystemInfo: ", res);
        env = (res.platform == "devtools") ? _SI.envID.test : _SI.envID.release;
        console.log("env:", env);
      }
    });
    wx.cloud.init({
      env: env,
      traceUser: true,
    });
    console.log("wx.cloud.init success!");
  }
}