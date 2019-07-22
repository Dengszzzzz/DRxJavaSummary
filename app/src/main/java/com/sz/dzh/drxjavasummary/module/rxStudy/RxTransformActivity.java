package com.sz.dzh.drxjavasummary.module.rxStudy;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.socks.library.KLog;
import com.sz.dzh.drxjavasummary.R;
import com.sz.dzh.drxjavasummary.base.BaseActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by administrator on 2018/11/6.
 * 转化类操作符
 * map()
 * 将被观察者发送的事件转换为任意的类型事件。返回的是结果集，适用于一对一转换。（数据类型转换）
 * flatMap()
 * 将被观察者发送的事件序列进行 拆分 & 单独转换，再合并成一个新的事件序列，最后再进行发送。返回的是包含结果集的Observable，适用于一对多，多对多得场景。
 * concatMap()
 * 和flatMap()类似，区别是flatMap是无序的，concatMap是有序的，它的新事件序列和旧序列顺序一致。具体可以在flatMap生成事件的逻辑里加个延迟看到差异。
 * buffer()
 * 定期从被观察者需要发送的事件中获取一定数量的事件，放到缓存区中，最终发送。两个参数，count是缓存区大小，skip是步长。
 * switchMap()
 * 将Observable发射的数据集合变换为Observables集合，然后只发射这些Observables最近发射的数据
 * scan()
 * 对Observable发射的每一项数据应用一个函数，然后按顺序依次发射每一个值
 * groupBy()
 * 将Observable分拆为Observable集合，将原始Observable发射的数据按Key分组，每一个Observable发射一组不同的数据
 */
public class RxTransformActivity extends BaseActivity {

    @BindView(R.id.tv_compose)
    TextView mTvCompose;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_rx_transform);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("转化类操作符");
    }

    @OnClick({R.id.btn_map, R.id.btn_flatMap, R.id.btn_concatMap, R.id.btn_buffer, R.id.btn_compose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_map:
                map();
                break;
            case R.id.btn_flatMap:
                flatMap();
                break;
            case R.id.btn_concatMap:
                concatMap();
                break;
            case R.id.btn_buffer:
                buffer();
                break;
            case R.id.btn_compose:
                compose();
                break;
        }
    }


    /**
     * Map（）
     * 作用: 将被观察者发送的事件转换为任意的类型事件。
     * 应用场景：数据类型转换
     */
    private void map() {
        //举例： 整型 变换成 字符串类型
        Observable.just(1, 2, 3)
                .map(new Function<Integer, String>() {
                    //将参数中的Integer类型对象转换成一个String类型对象
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return "已转字符串" + integer;
                    }
                }).subscribe(new Consumer<String>() {//事件的参数类型也由Integer类型->String类型
            @Override
            public void accept(String s) throws Exception {
                KLog.d(TAG, s);
            }
        });
    }


    /**
     * flatMap()
     * 作用：将被观察者发送的事件序列进行 拆分 & 单独转换，再合并成一个新的事件序列，最后再进行发送
     * 也就是将一个Observable转换为另一个Observable发射出去,并且可以将多个事件转化为1个，无序。
     * <p>
     * 结论：
     * 由打印结果可知，几乎同时拆分事件
     */
    private void flatMap() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Observable.fromIterable(list)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Integer integer) throws Exception {
                        KLog.d("开始拆分事件" + integer);
                        List<String> list = new ArrayList<>();
                        for (int i = 0; i < 3; i++) {
                            list.add("事件" + integer + "拆分后的子事件" + i);
                            // 通过flatMap中将被观察者生产的事件序列先进行拆分，再将每个事件转换为一个新的发送三个String事件
                        }
                        return Observable.fromIterable(list).delay(2000, TimeUnit.MILLISECONDS);
                    }
                }).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                KLog.d(TAG, s);
            }
        });
    }

    /**
     * concatMap()
     * 和flatMap类似，区别是新事件序列和 被观察者旧序列生成顺序一致
     * 例子里加了delay延迟，可以更明显地看出和flatMap的区别。
     * <p>
     * 结论：
     * 由打印结果可知，事件是依次拆分的，“开始拆分事件2” 是在打印了 “事件1拆分后的子事件2” 之后的。
     */
    private void concatMap() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Observable.fromIterable(list).subscribeOn(Schedulers.io()).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                KLog.d("开始拆分事件" + integer);
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("事件" + integer + "拆分后的子事件" + i);
                }
                //将多个事件合并成1个事件
                return Observable.fromIterable(list).delay(2000, TimeUnit.MILLISECONDS);
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                KLog.d(TAG, s);
            }
        });
    }


    /**
     * buffer()
     * 定期从 被观察者（Obervable）需要发送的事件中 获取一定数量的事件 & 放到缓存区中，最终发送
     */
    private void buffer() {
        Observable.just(1, 2, 3, 4, 5)
                .buffer(3, 1)  //设置缓存区大小 & 步长
                // 缓存区大小 = 每次从被观察者中获取的事件数量
                // 步长 = 每次获取新事件的数量
                .subscribe(new Observer<List<Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Integer> stringList) {
                        KLog.d(TAG, " 缓存区里的事件数量 = " + stringList.size());
                        for (Integer value : stringList) {
                            KLog.d(TAG, " 事件 = " + value);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * compose 和 Transformers
     *
     * 举例：
     * 新开一个线程执行，把compose注销，会奔溃。
     * 把compose打开，不奔溃，说明观察者执行 在主线程设置成功。
     */
    private String result;
    private void compose() {
        result = "";
        new Thread(new Runnable() {
            @Override
            public void run() {
                Observable.just(1, 2, 3)
                        .compose(new SchedulerTransformer<Integer>())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                result = result + integer.toString();
                                mTvCompose.setText("打印：" + result);
                                Log.e(TAG, result);
                            }
                        });
            }
        }).start();
    }


    /**
     * 定义一个Transformers。
     * 1.ObservableTransformer 能够将一个 Observable/Flowable/Single/Completable/Maybe 对象转换成另一个
     * Observable/Flowable/Single/Completable/Maybe 对象
     * <p>
     * 2.当创建Observable/Flowable...时，compose操作符会立即执行，而不像其他的操作符需要在onNext()调用后才执行。
     * <p>
     * 3.不考虑上下流类型，就用泛型T
     **/
    public class SchedulerTransformer<T> implements ObservableTransformer<T, T> {

        @Override
        public ObservableSource<T> apply(Observable<T> upstream) {
            return upstream
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }
}
