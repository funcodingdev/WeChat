# WeChat
## 1、ViewPager的适配器 <br>
FragmentPagerAdapter:<br>
    onDestoryView,onDesory<br>
    fragment并没有销毁<br>
FragmentStatePagerAdapter:<br>
    onDestoryView,onDesory,onCreate,onCreateView<br>
    fragment被销毁<br>
PageAdapter:<br>
    在简单的页面中可直接使用的话，不必嵌套在Fragment中<br>
## 2、activity与fragment之间的通信<br>
activity->fragment:拿到fragment.xxx方法<br>
fragment->activity:不是fragment要调用activity的方法，而是fragment对外提供自己的核心事件回调，activity自己选择是否监听<br>
## 3、ViewPager的切换动画<br>
setPageTransformer：<br>
以view,position通过打印日志找出规律<br>
## 4、规律<br>
scale动画 <br>
  view->(0.75f,1)<br>
  position -> 动画的区间<br>
rotate动画<br>
  注意旋转中心、注意空间<br>
banner动画<br>
  clipChildren<br>
  pageMargin(scale)<br>

