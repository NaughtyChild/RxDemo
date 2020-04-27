package cn.naughtychild.rxdemo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import rx.Observable
import rx.Subscription
import rx.functions.Func2
import rx.subjects.PublishSubject
import java.util.*

class LoginActivity : AppCompatActivity() {
    val namePublisher: PublishSubject<String> by lazy {
        PublishSubject.create<String>()
    }
    val passwordPublisher: PublishSubject<String> by lazy {
        PublishSubject.create<String>()
    }
    lateinit var dispose: Subscription
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginNameEt.addTextChangedListener(MyTextWatcher(namePublisher))
        loginPasswordEt.addTextChangedListener(MyTextWatcher(passwordPublisher))
        val observable = Observable.combineLatest(namePublisher, passwordPublisher) { nameStr, passWordStr ->
            nameStr.length > 2 && passWordStr.length > 4
        }
        dispose = observable.subscribe {
            if (it) {
                commitTv.text = "校验正确可以登录"
            } else {
                commitTv.text = "校验失败正确禁止登录"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dispose.unsubscribe()
    }
}

class MyTextWatcher constructor(private val publisher: PublishSubject<String>) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        publisher.onNext(s.toString())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}
