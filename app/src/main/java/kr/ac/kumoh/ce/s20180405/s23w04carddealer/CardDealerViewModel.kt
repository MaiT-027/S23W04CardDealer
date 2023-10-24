package kr.ac.kumoh.ce.s20180405.s23w04carddealer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class CardDealerViewModel : ViewModel() {
    private var _cards = MutableLiveData(IntArray(5) { -1 })
    private var _rankText: MutableLiveData<String> = MutableLiveData()
    val cards: LiveData<IntArray>
        get() = _cards
    val rankText: LiveData<String>
        get() = _rankText
    fun shuffle() {
        var num: Int
        val newCards = IntArray(5) { -1 }

        for (i in newCards.indices) {
            do {
                num = Random.nextInt(52)
            } while (newCards.contains(num))
            newCards[i] = num
        }

        newCards.sort()
        _cards.value = newCards
    }

    fun checkRank() {
        when {
            isRoyalStraightFlush() -> _rankText.value = "로얄 스트레이트 플러쉬"
            isBackStraightFlush() -> _rankText.value = "백 스트레이트 플러쉬"
            isStraightFlush() -> _rankText.value = "스트레이트 플러쉬"
            isFourCards() -> _rankText.value = "포 카드"
            isFullHouse() -> _rankText.value = "풀 하우스"
            isFlush() -> _rankText.value = "플러시"
            isMountain() -> _rankText.value = "마운틴"
            isBackStraight() -> _rankText.value = "백 스트레이트"
            isStraight() -> _rankText.value = "스트레이트"
            isTriple() -> _rankText.value = "트리플"
            isTwoPair() -> _rankText.value = "투 페어"
            isPair() -> _rankText.value = "원 페어"
            else -> _rankText.value = "탑 카드"
        }
    }

    private fun isRoyalStraightFlush(): Boolean {
        return isMountain() && isFlush()
    }

    private fun isBackStraightFlush(): Boolean {
        return isFlush() && isBackStraight()
    }

    private fun isStraightFlush(): Boolean {
        return isStraight() && isFlush()
    }

    private fun isFullHouse(): Boolean {
        if (isTriple()) {
            val numbers = _cards.value?.map { it % 13 }?.toList()?.sorted()
            if (numbers?.toSet()?.size == 2) {
                return true
            }
        }
        return false
    }

    private fun isStraight(): Boolean {
        val numbers = _cards.value?.map { it % 13 }?.toList()?.sorted()
        Log.i("Straight?", numbers.toString())
        for (i in 0 until 4) {
            if (numbers?.get(i)!! + 1 != numbers[i + 1]) return false
        }
        return true
    }

    private fun isMountain(): Boolean {
        val numbers = _cards.value?.map { it % 13 }?.toList()?.sorted()
        Log.i("Mountain?", numbers.toString())
        return numbers == intArrayOf(0, 9, 10, 11, 12).toList()
    }

    private fun isFlush(): Boolean {
        val shape = _cards.value!!.map { it / 13 }.toList().sorted()
        return shape.all { it == shape[0] }
    }

    private fun isBackStraight(): Boolean {
        val numbers = _cards.value?.map { it % 13 }?.toList()?.sorted()
        return numbers == intArrayOf(0, 1, 2, 3, 4).toList()
    }

    private fun isFourCards(): Boolean {
        val numbers = _cards.value?.map { it % 13 }?.toList()?.sorted()
        for (i in numbers!!) {
            if (numbers.count { it == i } == 4) return true
        }
        return false
    }

    private fun isTriple(): Boolean {
        val numbers = _cards.value?.map { it % 13 }?.toList()?.sorted()
        for (i in numbers!!) {
            if (numbers.count { it == i } == 3) return true
        }
        return false
    }

    private fun isTwoPair(): Boolean {
        val numbers = _cards.value?.map { it % 13 }?.toList()?.sorted()
        return numbers?.toSet()?.size == 3
    }

    private fun isPair(): Boolean {
        val numbers = _cards.value?.map { it % 13 }?.toList()?.sorted()
        return numbers?.toSet()?.size == 4
    }
}