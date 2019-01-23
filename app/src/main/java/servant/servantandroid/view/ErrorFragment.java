package servant.servantandroid.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;

import servant.servantandroid.R;
import servant.servantandroid.databinding.ErrorDialogBinding;

public class ErrorFragment extends DialogFragment {
    // constants for the argument names, just a safety measure
    private static final String ERROR_ARG   = "error";
    private static final String DETAILS_ARG = "details";

    /**
     * sets all the arguments and opens the dialog.
     * android sometimes re-instanciates fragments via reflection
     * so the member variables and references are lost
     * which also means the arguments can only be primitive
     * @param manager the fragment manager from the main activity
     * @param error error message to be displayed
     * @param details a more detailed error description [optional]
     */
    static void showErrorFragment(FragmentManager manager, String error, String details) {
        ErrorFragment fragment = new ErrorFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ERROR_ARG, error);
        arguments.putString(DETAILS_ARG, details);

        fragment.setArguments(arguments);
        fragment.show(manager, null);
    }

    @Override @NonNull
    public Dialog onCreateDialog(Bundle savedInstance) {
        if(getArguments() == null) throw new IllegalArgumentException(
            "an error-dialog was opened without any arguments... lol"
        );

        String error   = getArguments().getString(ERROR_ARG);
        String details = getArguments().getString(DETAILS_ARG);

        error   = (error   == null? getString(R.string.not_available) : error);
        details = (details == null? getString(R.string.not_available) : details);

        ErrorDialogBinding binding = DataBindingUtil.inflate(
            LayoutInflater.from(getContext()),
            R.layout.error_dialog, null, false
        );

        binding.errorDescription.setText(error);
        binding.errorDetails.setText(details);

        // make the error details scrollable
        binding.errorDetails.setMovementMethod(new ScrollingMovementMethod());

        return new AlertDialog.Builder(getActivity())
            .setView(binding.getRoot())
            .setPositiveButton(R.string.ok, (dialog, id) -> dialog.dismiss())
            .create();
    }
}
