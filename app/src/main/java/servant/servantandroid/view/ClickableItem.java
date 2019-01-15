package servant.servantandroid.view;

import android.view.View;

public abstract interface ClickableItem {
    public abstract void onClick(View view);
    public boolean isClickable();
}
