# WeChat
## 1、ViewPager的适配器
FragmentPagerAdapter:
    onDestoryView,onDesory
    fragment并没有销毁
FragmentStatePagerAdapter:
    onDestoryView,onDesory,onCreate,onCreateView
    fragment被销毁
PageAdapter:
    在简单的页面中可直接使用的话，不必嵌套在Fragment中
## 2、activity与fragment之间的通信
activity->fragment:拿到fragment.xxx方法
fragment->activity:不是fragment要调用activity的方法，而是fragment对外提供自己的核心事件回调，activity自己选择是否监听
## 3、ViewPager的切换动画
setPageTransformer：
以view,position通过打印日志找出规律
## 4、规律
scale动画 
  view->(0.75f,1)
  position -> 动画的区间
rotate动画
  注意旋转中心、注意空间
banner动画
  clipChildren
  pageMargin(scale)

