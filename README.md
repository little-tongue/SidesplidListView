# SidesplidListView（仿QQ）

### 使用注意
#### 1.item布局文件最外层设置背景色，否则滑动时会调用系统变色。
#### 2.item布局文件为一个横向LinearLayout包裹两个View，第一个View是宽度match_parent的正常显示项目item，第二个View是侧滑出现的菜单，第二个菜单的宽度必须设置固定数值，例如100dp。
#### 3.使用系统SwipeRefreshLayout包裹SideslipListView的时候侧滑有冲突，使用项目中定义好的RelativeSwipeRefreshLayout


