// 页面逻辑
Page({
  data: {
    title: '', // 标题
    description: '', // 描述
    images: [], // 图片列表
    isSubmitting: false, // 是否正在提交
  },

  // 监听标题输入
  onTitleInput(e) {
    this.setData({
      title: e.detail.value
    });
  },

  // 监听描述输入
  onDescriptionInput(e) {
    this.setData({
      description: e.detail.value
    });
  },

  // 选择图片
  // 在选择图片时显示自定义菜单
chooseImage() {
  const that = this;
  
  // 显示自定义操作菜单
  wx.showActionSheet({
    itemList: ['拍照','相册'],
    success(res) {
      const tapIndex = res.tapIndex;
      if (tapIndex === 0) {
        // 跳转到自定义相机页面
        that.goToCustomCamera();
      } else if (tapIndex === 1) {
        // 从相册选择
        that.chooseFromAlbum();
      }
    },
    fail(res) {
      console.log('用户取消选择', res);
    }
  });
},

// 从相册选择
chooseFromAlbum() {
  const that = this;

  wx.chooseImage({
    count: 1,
    sizeType: ['compressed'],
    sourceType: ['album'], // 只从相册选择
    success(res) {
      const tempFilePaths = res.tempFilePaths;
      console.log('接收到选择图片:', tempFilePaths);
      // 处理选择的图片
      that.setData({
        images: tempFilePaths
      });
    }
  });
},

// 跳转到自定义相机页面
goToCustomCamera() {
  wx.navigateTo({
    url: '/pages/camera/camera', // 自定义相机页面路径
    success(res) {
      console.log('跳转到自定义相机成功');
    },
    fail(err) {
      console.error('跳转失败:', err);
      wx.showToast({
        title: '无法打开相机',
        icon: 'none'
      });
    }
  });
},
setCameraImage: function(imagePath) {
  console.log('接收到相机图片:', imagePath);
  this.setData({
    images: this.data.images.concat(imagePath)
  });
},
  /** 
  chooseImage() {
    const that = this;
    wx.chooseImage({
      count: 1, // 最多选择1张图片
      sizeType: ['compressed'], // 压缩图
      sourceType: ['album', 'camera'], // 相册或相机
      success(res) {
        const tempFilePaths = res.tempFilePaths;
        that.setData({
          images: that.data.images.concat(tempFilePaths)
        });
      }
    });
  },
  */

  // 删除图片
  deleteImage(e) {
    const index = e.currentTarget.dataset.index;
    const images = this.data.images;
    images.splice(index, 1);
    this.setData({
      images
    });
  },

  // 表单提交
  formSubmit(e) {
    const that = this;
    const formData = e.detail.value;
    
    // 表单验证
  // 手机号正则验证
const phoneRegex = /^1[3-9]\d{9}$/;

if (!formData.title) {
  wx.showToast({
    title: '请输入手机号',
    icon: 'none'
  });
  return;
}

if (!phoneRegex.test(formData.title)) {
  wx.showToast({
    title: '请输入正确的手机号格式',
    icon: 'none'
  });
  return;
}
    
    if (!formData.description) {
      wx.showToast({
        title: '请输入描述',
        icon: 'none'
      });
      return;
    }
    
    if (this.data.images.length === 0) {
      wx.showToast({
        title: '请选择刚刚拍摄的图片',
        icon: 'none'
      });
      return;
    }
    
    // 设置提交状态
    this.setData({
      isSubmitting: true
    });
    
    // 上传图片到服务器
    this.uploadImage(0, formData);
  },
  
  // 上传图片
  uploadImage(index, formData) {
    const that = this;
    const imagePath = this.data.images[index];
    
    wx.uploadFile({
      url: 'http://127.0.0.1:8081/place/main/submit', //服务器上传地址
      filePath: imagePath,
      name: 'image',
      formData: {
        'title': formData.title,
        'description': formData.description
      },
      success(res) {
        const data = JSON.parse(res.data);
        if (data.code === 0) {
          // 上传成功
          wx.showToast({
            title: '上传成功',
            icon: 'success',
            duration: 2000,
            complete() {
              setTimeout(() => {
                // 重置表单
                that.setData({
                  title: '',
                  description: '',
                  images: [],
                  isSubmitting: false
                });
              }, 2000);
            }
          });
        } else {
          // 上传失败
          wx.showToast({
            title: '上传失败: ' + data.msg,
            icon: 'none',
            duration: 3000
          });
          that.setData({
            isSubmitting: false
          });
        }
      },
      fail() {
        wx.showToast({
          title: '网络错误，请重试',
          icon: 'none'
        });
        that.setData({
          isSubmitting: false
        });
      }
    });
  },
  
  // 计算属性：是否可以提交
  computed: {
    canSubmit() {
      return this.data.title && this.data.description && this.data.images.length > 0 && !this.data.isSubmitting;
    }
  }
});