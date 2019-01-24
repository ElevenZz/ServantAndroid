package servant.servantandroid.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;

import servant.servantandroid.R;
import servant.servantandroid.databinding.AddserverDialogBinding;

/**
 * dialog to add a new server
 */
public class AddServerFragment extends DialogFragment {
    /**
     * interface for allowing the communication between the main activity and the dialog
     * the method gets called when the user clicks the ok button
     */
    public interface AddServerListener {
        void onAddServerClicked(DialogFragment dialog);
    }

    /**
     * the observer for this dialog
     * it gets notified when the user clicks the OK button
     */
    private AddServerListener m_listener;

    /**
     * automatically gets called by the creator of the dialog
     * @param context the creator of the module. must implement AddServerListener
     */
    @Override public void onAttach(Context context) {
        super.onAttach(context);
        try { m_listener = (AddServerListener)context; }
        catch (ClassCastException e) {
            throw new ClassCastException(
                context.toString() + " does not implement AddServerListener"
            );
        }
    }

    /**
     * creating the actual dialog using the add server layout
     * @param savedInstance if this was restored from memory
     * @return the created dialog
     */
    @Override @NonNull public Dialog onCreateDialog(Bundle savedInstance) {
        AddserverDialogBinding binding = DataBindingUtil.inflate(
            LayoutInflater.from(getContext()),
            R.layout.addserver_dialog, null, false
        );

        return new AlertDialog.Builder(getActivity())
            .setView(binding.getRoot())
            .setPositiveButton(R.string.add, (dialog, id) -> m_listener.onAddServerClicked(this))
            .setNegativeButton(R.string.cancel, (dialog, id) -> this.getDialog().cancel())
            .create();
    }
}
