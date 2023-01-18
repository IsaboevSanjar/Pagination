package uz.sanjar.pagination;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ResponseRecyclerView extends RecyclerView {
    public RecyclerScrollListener scrollListener;
    private Context context;
    private int columnCount;
    private Boolean enablePaging = false;
    private LayoutManager layoutManager;
    private int previousTotalItemCount = 0;
    private boolean loading = false;

    private int page = 0;


    public ResponseRecyclerView(@NonNull Context context) {
        super(context);
        setUp(context);
    }

    public ResponseRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setUp(context);
    }

    public ResponseRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUp(context);
    }

    private void setUp(Context context) {
        if (!isInEditMode()) {
            this.context = context;
            notifyColumnCount();
        }
    }

    private void notifyColumnCount() {
        // checking UI Landscape or Portrait
        if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT) {
            columnCount = 3 * columnCount;
        }
        if (layoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager) layoutManager).setSpanCount(columnCount);
        }
    }

    public void setScrollListener(RecyclerScrollListener scrollListener) {
        this.scrollListener = scrollListener;

    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
        notifyColumnCount();
    }

    public void enablePagination(Boolean enablePaging) {
        this.enablePaging = enablePaging;
    }

    @Override
    public void setLayoutManager(@Nullable LayoutManager layout) {
        super.setLayoutManager(layout);
        this.layoutManager = layout;
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        if (scrollListener != null) {
            if (dy > 0) {
                scrollListener.hideFab();
                if (enablePaging) {
                    endLessScroll();
                }
            } else {
                scrollListener.showFab();
            }
        }
    }

    private void endLessScroll() {
        int lastVisibleItemPosition = 0;
        int totalItemCount = layoutManager.getItemCount();
        if (layoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }

        if (totalItemCount < previousTotalItemCount) {
            previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                loading = true;
            }
        }
        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        if (!loading && lastVisibleItemPosition + 4 > totalItemCount) {
            page++;
            scrollListener.loadRecyclerData(page);
            loading = true;
        }
    }

    interface RecyclerScrollListener {
        public void loadRecyclerData(int page);

        public void showFab();

        public void hideFab();
    }
}
