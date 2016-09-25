package example.raghunandan.databinding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import example.raghunandan.databinding.adapter.FeedAdapter;
import example.raghunandan.databinding.databinding.FeedActivityBinding;
import example.raghunandan.databinding.models.FeedResponse;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Raghunandan on 25-09-2016.
 */

public class FeedActivity extends AppCompatActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();



    DataManager dataManager;

    private FeedAdapter feedAdapter;

    private FeedActivityBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataManager = new DataManager();
        binding = DataBindingUtil.setContentView(this, R.layout.feed_activity);

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.errorTextView.setVisibility(View.GONE);


        binding.recyclerview.setLayoutManager( new LinearLayoutManager(this));
        binding.recyclerview.setHasFixedSize(true);

        feedAdapter = new FeedAdapter();
        binding.recyclerview.setAdapter(feedAdapter);

        fetchFeed();
    }

    public void fetchFeed()
    {
          compositeDisposable.add(dataManager.fetchFeed().subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribeWith(new DisposableObserver<FeedResponse>() {

                      @Override
                      public void onError(Throwable e) {
                          e.printStackTrace();
                          binding.progressBar.setVisibility(View.GONE);
                          binding.recyclerview.setVisibility(View.GONE);
                          binding.errorTextView.setVisibility(View.VISIBLE);
                          //EventBus.getDefault().post(new FeedErrorEvent(e));
                      }

                      @Override
                      public void onComplete() {

                      }

                      @Override
                      public void onNext(FeedResponse feedResponse) {
                          binding.progressBar.setVisibility(View.GONE);
                          binding.recyclerview.setVisibility(View.VISIBLE);
                          feedAdapter.addList(feedResponse.getData());
                      }
                  }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
