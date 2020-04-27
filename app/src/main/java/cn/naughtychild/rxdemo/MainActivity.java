package cn.naughtychild.rxdemo;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.print.PrinterId;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.xml.namespace.NamespaceContext;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.GroupedObservable;
import rx.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toMap();
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
        Observable<GroupedObservable<Boolean, Integer>> observable =
                Observable.just(1, 2, 3, 4, 5).groupBy(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer % 2 == 1;
                    }
                });
        Observable.concat(observable).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d("MainActivity", "call: " + integer);
            }
        });
    }

    private void filter() {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9).filter(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer > 2;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d("MainActivity", "call: " + integer);
            }
        });
    }

    private void elementAt() {
        Observable.just(1, 2, 3, 4).elementAtOrDefault(5, 1000).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d("MainActivity", "call: " + integer);
            }
        });
    }

    private void distinct() {
        Observable.just(1, 2, 2, 3, 3, 4, 4, 4, 5).distinct(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer % 2 == 1;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d("MainActivity", "call: " + integer);
            }
        });
    }

    private void skip() {
        //or take
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8).skipLast(2).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d("MainActivity", "call: " + integer);
            }
        });
    }

    private void throttleFirst() {
        //发射指定一段时间内的首个数据，类似于首例取样
        Observable.interval(100, TimeUnit.MILLISECONDS).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                Log.d("MainActivity", "call: " + aLong);
            }
        });
    }

    private void throttleWithTimeOut() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 10; i++) {
                    subscriber.onNext(i);
                    int sleep = 100;
                    if (i % 3 == 0) {
                        sleep = 300;
                    }
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                subscriber.onCompleted();
            }
        }).throttleWithTimeout(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d("MainActivity", "throttleWithTimeOut:" + integer);
            }
        });
    }

    private void zip() {
        Observable<Integer> observable1 = Observable.just(1, 2, 3, 4);
        Observable<String> observable2 = Observable.just("a", "b", "c");
        Observable.zip(observable1, observable2, new Func2<Integer, String, String>() {
            @Override
            public String call(Integer integer, String s) {
                return integer + s;
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d("MainActivity", "call: " + s);
            }
        });
    }

    private void combineLastTest() {
        Observable<Long> obs1 = Observable.interval(2, TimeUnit.SECONDS);
        Observable<Long> obs2 = Observable.interval(1, TimeUnit.SECONDS);
        Observable.combineLatest(obs1, obs2, new Func2<Long, Long, String>() {
            @Override
            public String call(Long aLong, Long aLong2) {
                return aLong + "---" + aLong2;
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d("MainActivity", "call: " + s);
            }
        });
    }

    private void delay() {
        Observable.just(1, 2, 3, 4, 5, 6).delay(6, TimeUnit.SECONDS).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d("MainActivity", "call: " + integer);
            }
        });
    }

    private void timeOut() {
        Observable.just(1, 2, 3, 4, 5).doOnNext(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                try {
                    Thread.sleep(integer * 100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).timeout(300, TimeUnit.MILLISECONDS, Observable.just(10, 11, 12)).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d("MainActivity", "call: " + integer);
            }
        });
    }

    private void onErrorReturn() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 5; i++) {
                    subscriber.onNext(i);
                    if (i == 4) {
                        Log.d("MainActivity", "call: 发生错误了");
                        subscriber.onError(new Throwable("yuejie"));
                    }
                }
            }
        }).onErrorReturn(new Func1<Throwable, Integer>() {
            @Override
            public Integer call(Throwable throwable) {
                return 5;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d("MainActivity", "call: " + integer);
            }
        });
    }

    private void retryWhen() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 4; i++) {
                    subscriber.onNext(i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i == 2) {
                        subscriber.onError(new Throwable("错误了"));
                    }
                }
            }
        }).retry(2).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.d("MainActivity", "onError: " + e.getMessage());
            }

            @Override
            public void onNext(Integer integer) {
                Log.d("MainActivity", "onNext: " + integer);
            }
        });
    }

    private void all() {
        Observable.just(1, 2, 3, 4, 5, 6).all(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer > 3;
            }
        }).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                Log.d("MainActivity", "call: " + aBoolean);
            }
        });
    }

    private void toMap() {
        Observable.just(1, 2, 3).toMap(new Func1<Integer, String>() {
            @Override
            public String call(Integer integer) {
                String result = "";
                switch (integer) {
                    case 1:
                        result = "one";
                        break;
                    case 2:
                        result = "two";
                        break;
                    case 3:
                        result = "three";
                        break;
                }
                return result;
            }
        }).subscribe(new Action1<Map<String, Integer>>() {
            @Override
            public void call(Map<String, Integer> map) {
                Set<String> keys = map.keySet();
                Iterator<String> iterator = keys.iterator();
                while (iterator.hasNext()) {
                   String key= iterator.next();
                   int value=map.get(key);
                    Log.d("MainActivity", "key="+key+",value="+value);
                }
            }
        });
    }
}