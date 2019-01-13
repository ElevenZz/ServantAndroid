package servant.servantandroid.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;

import servant.servantandroid.R;

public class AddServerFragment extends DialogFragment {

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface AddServerListener {
        void onAddServerClicked(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    AddServerListener m_listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try { m_listener = (AddServerListener)context; }
        catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " does not implement AddServerListener");
        }
    }

    @Override @NonNull
    public Dialog onCreateDialog(Bundle savedInstance) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        return new AlertDialog.Builder(getActivity()).setView(inflater.inflate(R.layout.addserver_dialog, null))
            .setPositiveButton(R.string.add, (dialog, id) -> m_listener.onAddServerClicked(this))
            .setNegativeButton(R.string.cancel, (dialog, id) -> this.getDialog().cancel())
            .create();
    }
}
