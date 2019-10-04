package io.flow

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import com.yuyakaido.android.cardstackview.*
import io.db.UserSharedPreferences
import io.flow.pickActivity.CardStackAdapter
import io.flow.pickActivity.Spot
import io.flow.pickActivity.SpotDiffCallback

import java.util.*

class PickActivity : AppCompatActivity(), CardStackListener {
    private val cardStackView by lazy { findViewById<CardStackView>(R.id.card_stack_view) }
    private val manager by lazy { CardStackLayoutManager(this, this) }
    private val adapter by lazy { CardStackAdapter(createSpots()) }
    private var backKeyClickTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick)
        setupCardStackView()
        setupButton()
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() > backKeyClickTime + 2000) {
            backKeyClickTime = System.currentTimeMillis()
            Toast.makeText(this, "한번 더 뒤로가기를 누를시 앱이 종료됩니다.", Toast.LENGTH_LONG).show()
            return
        }

        if (System.currentTimeMillis() <= backKeyClickTime + 2000) {
            super.onBackPressed()
            //ActivityCompat.finishAffinity(this)
        }
    }

    private fun setupCardStackView() {
        initialize()
    }

    private fun setupButton() {
        val skip = findViewById<View>(R.id.skip_button)
        skip.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            cardStackView.swipe()
        }

        val rewind = findViewById<View>(R.id.rewind_button)
        rewind.setOnClickListener {
            val setting = RewindAnimationSetting.Builder()
                .setDirection(Direction.Bottom)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(DecelerateInterpolator())
                .build()
            manager.setRewindAnimationSetting(setting)
            cardStackView.rewind()
        }

        val like = findViewById<View>(R.id.like_button)
        like.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            cardStackView.swipe()
        }
    }

    private fun initialize() {
        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.1f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(true)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager.setOverlayInterpolator(LinearInterpolator())
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        cardStackView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    private fun paginate() {
        val old = adapter.getSpots()
        val new = old.plus(createSpots())
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun createSpots(): List<Spot> {
        val spots = ArrayList<Spot>()
        spots.add(
            Spot(
                name = "펑티모",
                age = "27",
                contents = "안녕하세요. 펑티모 입니다. 고양이 소릴내봐 냥냥냥냥냥 멍멍이 소릴내봐 멍멍멍멍멍",
                url = "http://mblogthumb2.phinf.naver.net/MjAxODEwMTZfMjM0/MDAxNTM5NjYxNDAxNDc2.b_IZaY_koH4pA_3y3_32pCcIp5nwuWGLKG-KAywp2tMg.fPwEEpNwK2a4i679Ys7nibElbnVTKAVC1aHL4ttkhrcg.JPEG.yyt1129/%ED%8E%91%ED%8B%B0%EB%AA%A8.jpg?type=w800"
            )
        )
        spots.add(
            Spot(
                name = "Fushimi Inari Shrine",
                age = "27",
                contents = "Kyoto",
                url = "https://source.unsplash.com/NYyCqdBOKwc/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Bamboo Forest",
                age = "27",
                contents = "Kyoto",
                url = "https://source.unsplash.com/buF62ewDLcQ/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Brooklyn Bridge",
                age = "27",
                contents = "New York",
                url = "https://source.unsplash.com/THozNzxEP3g/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Empire State Building",
                age = "27",
                contents = "New York",
                url = "https://source.unsplash.com/USrZRcRS2Lw/600x800"
            )
        )
        spots.add(
            Spot(
                name = "The statue of Liberty",
                age = "27",
                contents = "New York",
                url = "https://source.unsplash.com/PeFk7fzxTdk/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Louvre Museum",
                age = "27",
                contents = "Paris",
                url = "https://source.unsplash.com/LrMWHKqilUw/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Eiffel Tower",
                age = "27",
                contents = "Paris",
                url = "https://source.unsplash.com/HN-5Z6AmxrM/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Big Ben",
                age = "27",
                contents = "London",
                url = "https://source.unsplash.com/CdVAUADdqEc/600x800"
            )
        )
        spots.add(
            Spot(
                name = "Great Wall of China",
                age = "27",
                contents = "China",
                url = "https://source.unsplash.com/AWh9C-QjhE4/600x800"
            )
        )
        return spots
    }


    override fun onCardDragging(direction: Direction, ratio: Float) {
        Log.d("CardStackView", "onCardDragging: d = ${direction.name}, r = $ratio")
    }

    override fun onCardSwiped(direction: Direction) {
        Log.d("CardStackView", "onCardSwiped: p = ${manager.topPosition}, d = $direction")
        if (manager.topPosition == adapter.itemCount - 5) {
            paginate()
        }
    }

    override fun onCardRewound() {
        Log.d("CardStackView", "onCardRewound: ${manager.topPosition}")
    }

    override fun onCardCanceled() {
        Log.d("CardStackView", "onCardCanceled: ${manager.topPosition}")
    }

    override fun onCardAppeared(view: View, position: Int) {
        val textView = view.findViewById<TextView>(R.id.item_name)
        Log.d("CardStackView", "onCardAppeared: ($position) ${textView.text}")
    }

    override fun onCardDisappeared(view: View, position: Int) {
        val textView = view.findViewById<TextView>(R.id.item_name)
        Log.d("CardStackView", "onCardDisappeared: ($position) ${textView.text}")
    }
}
