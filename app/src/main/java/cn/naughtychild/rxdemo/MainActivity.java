package cn.naughtychild.rxdemo;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javax.xml.namespace.NamespaceContext;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.GroupedObservable;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        groupBy();
    }

    private void interval() {
        Observable.interval(2, TimeUnit.SECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                Log.d("MainActivity", "call: " + aLong);
            }
        });
    }

    private void range() {
        Observable.range(0, 2).repeat(2).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d("MainActivity", "call: " + integer);
            }
        });
    }

    private void map() {
        final String str = "123";
        Observable.just("456").map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                return str + s;
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d("MainActivity", "call: " + s);
            }
        });
    }

    private void flatMap() {
        ArrayList<String> arrayList = new ArrayList();
        arrayList.add("123");
        arrayList.add("abc");
        arrayList.add("one two three");
        arrayList.add("一 二 三");
        Observable.from(arrayList).flatMap(new Func1<String, Observable<?>>() {
            @Override
            public Observable<?> call(String str) {
                return Observable.just("NaughtyChild-" + str);
            }
        }).cast(String.class).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d("MainActivity", "call: " + s);
            }
        });
    }

    private void flatMapIterable() {
        Observable.just(1, 2, 3).flatMapIterable(new Func1<Integer, Iterable<Integer>>() {
            @Override
            public Iterable<Integer> call(Integer inter) {
                ArrayList<Integer> arrayList = new ArrayList<>();
                arrayList.add(inter + 1);
                return arrayList;
            }
        }).flatMap(new Func1<Integer, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(Integer o) {
                return Observable.just(o + 2);
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d("MainActivity", "call: " + integer);
            }
        });
    }

    private void buffer() {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).buffer(2).subscribe(new Action1<List<Integer>>() {
            @Override
            public void call(List<Integer> integers) {
                for (int i = 0; i < integers.size(); i++) {
                    Log.d("MainActivity", "call: " + integers.get(i));
                }
                Log.d("MainActivity", "call: ============================");
            }
        });
    }

    private void groupBy() {
        SwordMan man1 = (new SwordMan("奇数", 1));
        SwordMan man2 = (new SwordMan("偶数", 2));
        SwordMan man3 = (new SwordMan("奇数", 3));
        SwordMan man4 = (new SwordMan("偶数", 4));
        SwordMan man5 = (new SwordMan("奇数", 5));
        SwordMan man6 = (new SwordMan("偶数", 6));
        SwordMan man7 = (new SwordMan("奇数", 7));
        Observable<GroupedObservable<String, SwordMan>> observable =
                Observable.just(man1, man2, man3, man4, man5).groupBy(new Func1<SwordMan, String>() {
                    @Override
                    public String call(SwordMan man) {
                        return man.getName();
                    }
                });
        Observable.concat(observable).subscribe(new Action1<SwordMan>() {
            @Override
            public void call(SwordMan man) {
                Log.d("MainActivity", "call: " + man.getName() + "---value:" + man.getValue());
            }
        });
    }
}