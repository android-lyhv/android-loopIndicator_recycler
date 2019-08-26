Loop Indicator RecyclerView
=====
[ ![Download](https://api.bintray.com/packages/hovanlybkit/android-cycle_indicator/loopindicator/images/download.svg?version=1.0.0) ](https://bintray.com/hovanlybkit/android-cycle_indicator/loopindicator/1.0.0/link)

Gradle:

```gradle
repositories {
    google()
    jcenter()
}

dependencies {
    implementation 'com.lyho:LoopIndicatorLibrary:1.0.0'
}
```

Or Maven:

```xml
<dependency>
	<groupId>com.lyho</groupId>
	<artifactId>LoopIndicatorLibrary</artifactId>
	<version>1.0.0</version>
	<type>pom</type>
</dependency>
```
How do I use Loop Indicator Reyclerview?
-------------------
```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:animateLayoutChanges="true"
    android:orientation="vertical"
    tools:context=".MainActivity">
    
    <com.lyhv.library.CycleRecyclerTabLayout
        android:id="@+id/myRecyclerTabLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:rtl_tabHeight="30dp"
        app:rtl_tabIndicatorColor="@android:color/holo_blue_light"
        app:rtl_tabIndicatorCorner="15dp"
        app:rtl_tabIndicatorHeight="30dp"
        app:rtl_tabPadding="5dp"
        app:rtl_tabTextAppearanceActive="@style/style_tabAppearanceActive"
        app:rtl_tabTextAppearanceInActive="@style/style_tabAppearanceInActive" />
        
    <androidx.viewpager.widget.ViewPager
        android:id="@+id/viewPager"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</LinearLayout>
```

```java
// Adapter
class MyCyclePagerAdapter(fm: FragmentManager, items: List<String>) : CycleFragmentStatePagerAdapter(fm) {
    private val mItemList: ArrayList<String> = ArrayList()

    init {
        mItemList.addAll(items)
    }

    fun setItems(items: List<String>) {
        this.mItemList.clear()
        this.mItemList.addAll(items)
    }

    override fun getRealItemSize(): Int {
        return mItemList.size
    }

    override fun getRealItem(realPosition: Int): Fragment {
        return ItemFragment.newInstance(mItemList[realPosition])
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mItemList[getRealPosition(position)]
    }
}
// Example
        val mMyCyclePagerAdapter = MyCyclePagerAdapter(childFragmentManager, listOf())
        viewPager.adapter = recyclerTabLayout.setUpWithViewPager(context, viewPager, mMyCyclePagerAdapter)
        val titleItems: ArrayList<String> = ArrayList()
        for (index in 0 until 5) {
            titleItems.add("Tab $index")
        }
        mMyCyclePagerAdapter.setItems(titleItems)
        mMyCyclePagerAdapter.notifyDataSetChanged()
        myRecyclerTabLayout.setCenterPositionItem(0)
```
Author
------
@lyhv on GitHub

License
-------
BSD, part MIT and Apache 2.0.
