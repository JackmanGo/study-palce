// pages/history/history.js
const app = getApp();

Page({
  data: {
    isLoading: false,
    queryResult: [],
    hasMore: true,
    page: 1,
    pageSize: 10
  },

  onLoad() {
    this.loadData();
  },

  onPullDownRefresh() {
    // 下拉刷新
    this.setData({
      page: 1,
      pageSize: this.data.pageSize,
      appOpenId: `${appOpenId}`
    });
    this.loadData().then(() => {
      wx.stopPullDownRefresh();
    });
  },

  onReachBottom() {
    // 上拉加载更多
    if (this.data.hasMore && !this.data.isLoading) {
      this.loadMoreData();
    }
  },

  // 加载数据
  async loadData() {
    this.setData({ isLoading: true });
    
    try {
      const appOpenId = app.getOpenIdFromCache();
      const response = await new Promise((resolve, reject) => {
        wx.request({
          url: 'http://127.0.0.1:8081/place/main/records',
          method: 'GET',
          header: {
            'Content-Type': 'application/json'
          },
          data: {
            page: 1,
            pageSize: this.data.pageSize,
            appOpenId: `${appOpenId}`
          },
          success: resolve,
          fail: reject
        });
      });
      
      if (response.statusCode === 200) {
        const data = response.data;
        this.setData({
          queryResult: data.data || [],
          hasMore: false,
          page: 1
        });
      } else {
        this.showError('网络请求失败：' + response.statusCode);
      }
    } catch (error) {
      console.error('加载数据失败:', error);
      this.showError('加载失败，请检查网络');
    } finally {
      this.setData({ isLoading: false });
    }
  },

  // 加载更多数据
  async loadMoreData() {
    if (!this.data.hasMore) return;
    
    this.setData({ isLoading: true });
    const nextPage = this.data.page + 1;
    
    try {
      const token = wx.getStorageSync('token');
      const response = await new Promise((resolve, reject) => {
        wx.request({
          url: `${app.globalData.baseUrl}/api/checkin/records`,
          method: 'GET',
          header: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
          data: {
            page: nextPage,
            pageSize: this.data.pageSize
          },
          success: resolve,
          fail: reject
        });
      });

      if (response.statusCode === 200) {
        const data = response.data;
        if (data.code === 200) {
          const newRecords = data.data.records || [];
          this.setData({
            queryResult: [...this.data.queryResult, ...newRecords],
            hasMore: data.data.hasMore || false,
            page: nextPage
          });
        } else {
          this.showError('加载更多失败：' + data.message);
        }
      } else {
        this.showError('网络请求失败：' + response.statusCode);
      }
    } catch (error) {
      console.error('加载更多失败:', error);
      this.showError('加载失败，请检查网络');
    } finally {
      this.setData({ isLoading: false });
    }
  },

  // 图片预览
  previewImage(e) {
    const imageUrl = e.currentTarget.dataset.url;
    if (imageUrl) {
      wx.previewImage({
        urls: [imageUrl],
        current: imageUrl
      });
    }
  },

  // 显示错误提示
  showError(message) {
    wx.showToast({
      title: message,
      icon: 'none',
      duration: 2000
    });
  },

  // 重新加载
  onRetry() {
    this.loadData();
  }
});